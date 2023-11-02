package com.kensoftph.javafxmedia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBase {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/musicifydb";
        String username = "root";
        String password = "Kavushik@2004";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            if (connection != null) {
                System.out.println("Connected to the database");
                //Example: Insert a User
                insertUser(connection,"Kavushik","ME");

                // Example: Insert a song
                insertSong(connection, "Song Title", 1, 1, 180, "E:\\OD.mp3", "2023-01-01", "Pop");

                // Example: Insert an artist
                insertArtist(connection, "Artist Name");

                // Example: Insert an album
                insertAlbum(connection, "Album Title", 1, "2023-01-01", "Pop");

                // Example: Insert a playlist
                insertPlaylist(connection, "Playlist Title", 1, "2023-01-01", "Description");

                // Example: Insert a song into a playlist
                insertPlaylistSong(connection, 1, 1, 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void insertUser(Connection connection, String username, String password) throws SQLException {
        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User inserted: " + username);
            }
        }
    }

    private static void insertSong(Connection connection, String title, int artistId, int albumId, int duration, String filePath, String releaseDate, String genre) throws SQLException {
        String insertSQL = "INSERT INTO songs (title, artist_id, album_id, duration, file_path, release_date, genre) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, artistId);
            preparedStatement.setInt(3, albumId);
            preparedStatement.setInt(4, duration);
            preparedStatement.setString(5, filePath);
            preparedStatement.setString(6, releaseDate);
            preparedStatement.setString(7, genre);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Song inserted: " + title);
            }
        }
    }

    private static void insertArtist(Connection connection, String name) throws SQLException {
        String insertSQL = "INSERT INTO artists (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, name);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Artist inserted: " + name);
            }
        }
    }

    private static void insertAlbum(Connection connection, String title, int artistId, String releaseDate, String genre) throws SQLException {
        String insertSQL = "INSERT INTO albums (title, artist_id, release_date, genre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, artistId);
            preparedStatement.setString(3, releaseDate);
            preparedStatement.setString(4, genre);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Album inserted: " + title);
            }
        }
    }

    private static void insertPlaylist(Connection connection, String title, int userId, String creationDate, String description) throws SQLException {
        String insertSQL = "INSERT INTO playlists (title, user_id, creation_date, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, creationDate);
            preparedStatement.setString(4, description);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Playlist inserted: " + title);
            }
        }
    }

    private static void insertPlaylistSong(Connection connection, int playlistId, int songId, int position) throws SQLException {
        String insertSQL = "INSERT INTO playlist_songs (playlist_id, song_id, position) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(1, songId);
            preparedStatement.setInt(1, position);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Song inserted into playlist: Playlist " + playlistId + ", Song " + songId);
            }
        }
    }
}