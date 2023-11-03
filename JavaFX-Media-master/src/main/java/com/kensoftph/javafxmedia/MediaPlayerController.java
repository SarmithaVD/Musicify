package com.kensoftph.javafxmedia;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.image.Image;

import java.io.IOException;
import java.sql.DriverManager;
import javafx.scene.Scene;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class MediaPlayerController {

    private String currentSongTitle;
    private String currentArtistId;
    private String currentGenre;

    @FXML
    private Button btnPlay;
    @FXML
    private Label lblDuration;
    @FXML
    private MediaView mediaView;
    @FXML
    private Slider slider;
    @FXML
    private Button createPlaylistButton;
    @FXML
    private TextField playlistNameField;
    @FXML
    private BorderPane borderPane;

    public void initialize() {
        borderPane.setMinHeight(514.0);
        borderPane.setMinWidth(1080.0);
        borderPane.setPrefHeight(514.0);
        borderPane.setPrefWidth(1080.0);
        borderPane.setBackground(new Background(background));
    }

    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlayed = false;

    // Load the background image
    Image backgroundImage = new Image("file:./images/musicplayer.jpg");

    // Set the desired size for the background image
    double backgroundImageWidth = 1080;
    double backgroundImageHeight = 514;

    // Create a BackgroundSize with the desired width and height
    BackgroundSize backgroundSize = new BackgroundSize(backgroundImageWidth, backgroundImageHeight, true, true, true, false);

    // Create a BackgroundImage
    BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            backgroundSize);

    public void start(Stage mediaPlayerStage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("media-player.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            mediaPlayerStage.setTitle("Musicify");
            mediaPlayerStage.setScene(scene);
            mediaPlayerStage.show();
        }
    public MediaPlayerController() {
    }

    @FXML
    void btnPlay(MouseEvent event) {
        if (!isPlayed) {
            btnPlay.setText("Pause");
            mediaPlayer.play();
            isPlayed = true;
        } else {
            btnPlay.setText("Play");
            mediaPlayer.pause();
            isPlayed = false;
        }
    }

    @FXML
    void btnStop(MouseEvent event) {
        btnPlay.setText("Play");
        mediaPlayer.stop();
        isPlayed = false;
    }

    @FXML
    void selectMedia(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            playMedia(selectedFile.toURI().toString());
        }
    }

    private void playMedia(String url) {
        media = new Media(url);
        mediaPlayer = new MediaPlayer(media);

        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.currentTimeProperty().addListener((observableValue, oldValue, newValue) -> {
            slider.setValue(newValue.toSeconds());
            lblDuration.setText("Duration: " + (int) slider.getValue() + " / " + (int) media.getDuration().toSeconds());
        });

        mediaPlayer.setOnReady(() -> {
            Duration totalDuration = media.getDuration();
            slider.setMax(totalDuration.toSeconds());
            lblDuration.setText("Duration: 00 / " + (int) media.getDuration().toSeconds());

            // Get details of the currently playing song from the media file
            currentSongTitle = getSongTitleFromMedia(url);
            currentArtistId = getArtistIdFromDatabase(currentSongTitle);
            currentGenre = getGenreFromDatabase(url);
        });
    }

    private String getSongTitleFromMedia(String url) {
        File file = new File(url);
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();

            return tag.getFirst(FieldKey.TITLE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getArtistIdFromDatabase(String songTitle) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String artistId = null;

        try {
            // Set up the database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicifydb", "root", "Kavushik@2004");

            // Prepare the SQL statement to retrieve artist ID based on song title
            String query = "SELECT artist_id FROM songs WHERE title = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, songTitle);

            // Execute the SQL statement
            resultSet = preparedStatement.executeQuery();

            // Retrieve the artist ID from the result set
            if (resultSet.next()) {
                artistId = resultSet.getString("artist_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return artistId;
    }

    private String getGenreFromDatabase(String songTitle) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String genre = null;

        try {
            // Set up the database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicifydb", "root", "Kavushik@2004");

            // Prepare the SQL statement to retrieve genre based on song title
            String query = "SELECT genre FROM songs WHERE title = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, songTitle);

            // Execute the SQL statement
            resultSet = preparedStatement.executeQuery();

            // Retrieve the genre from the result set
            if (resultSet.next()) {
                genre = resultSet.getString("genre");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return genre;
    }

    @FXML
    void selectNextSong(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Next Song");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String url = selectedFile.toURI().toString();
            playMedia(url);
            btnPlay.setText("Play");
        }
    }

    public void sliderPressed(MouseEvent mouseEvent) {
        mediaPlayer.seek(Duration.seconds(slider.getValue()));
    }

    @FXML
    void createPlaylist(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Playlist");
        dialog.setHeaderText("Enter a name for your playlist:");
        dialog.setContentText("Playlist Name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(playlistName -> {
            int playlistId = insertPlaylistIntoDatabase(playlistName);

            if (playlistId > 0) {
                // Add similar songs to the playlist
                addSimilarSongsToPlaylist(playlistId);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Playlist created successfully!");

                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to create the playlist. Please try again.");

                alert.showAndWait();
            }
        });
    }

    private void addSimilarSongsToPlaylist(int playlistId) {
        // Get the currently playing song details
        String currentSongTitle = "Song Title"; // Replace with actual song title
        int currentArtistId = 1; // Replace with actual artist ID
        String currentGenre = "Pop"; // Replace with actual genre

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Set up the database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicifydb", "root", "Kavushik@2004");

            // Prepare the SQL statement to insert similar songs into the playlist
            String insertSimilarSongsSQL = "INSERT INTO playlist_songs (playlist_id, song_id, position) " +
                    "SELECT ?, song_id, 0 FROM songs " +
                    "WHERE artist_id = ? AND genre = ? AND song_id <> (SELECT song_id FROM songs WHERE title = ?) " +
                    "ORDER BY RAND() LIMIT 5";

            preparedStatement = connection.prepareStatement(insertSimilarSongsSQL);
            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, currentArtistId);
            preparedStatement.setString(3, currentGenre);
            preparedStatement.setString(4, currentSongTitle);

            // Execute the SQL statement
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int insertPlaylistIntoDatabase(String playlistName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int generatedPlaylistId = -1;

        try {
            // Set up the database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicifydb", "root", "Kavushik@2004");

            // Prepare the SQL statement to insert the playlist
            String insertSQL = "INSERT INTO playlists (title, user_id, creation_date) VALUES (?, ?, NOW())";
            preparedStatement = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, playlistName);
            preparedStatement.setInt(2, getUserIdForCurrentUser()); // Get the user ID

            // Execute the SQL statement
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                // Insertion failed
                return -1;
            }

            // Retrieve the generated playlist_id
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedPlaylistId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return generatedPlaylistId;
    }
    private int getUserIdForCurrentUser() {
        return 1;
    }


}
