package com.kensoftph.javafxmedia;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.sql.DriverManager;
import javafx.scene.Scene;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MediaPlayerController {

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

    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlayed = false;

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
        });
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
        // Replace with your logic to retrieve the user ID from your authentication system
        // For example, if you have a User class with a getUserID() method:
        // return User.getUserID();
        return 1; // A placeholder; replace with actual logic
    }
    private int getNextPositionInPlaylist(int playlistId) {
        // Replace with your logic to determine the next position in the playlist
        // You can query the database to find the highest position for the given playlist and increment it by 1.
        // For example, you can execute a SQL query to get the max position for the given playlist and add 1 to it.
        return 1; // A placeholder; replace with actual logic
    }

    public void insertSongIntoPlaylist(ActionEvent actionEvent) {
        int playlistId = 1;
        int songId = 2;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Set up the database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicifydb", "root", "Kavushik@2004");

            // Prepare the SQL statement to insert the song into the playlist
            String insertSQL = "INSERT INTO playlist_songs (playlist_id, song_id, position) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(5, playlistId);
            preparedStatement.setInt(6, songId);
            preparedStatement.setInt(7, 1); // Get the next position

            // Execute the SQL statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Song inserted into playlist: Playlist " + playlistId + ", Song " + songId);
            } else {
                System.out.println("Failed to insert song into playlist.");
            }
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
}
