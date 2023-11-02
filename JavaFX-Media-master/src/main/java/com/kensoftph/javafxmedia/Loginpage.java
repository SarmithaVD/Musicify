package com.kensoftph.javafxmedia;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;

public class Loginpage extends Application {
    private final DBconnection dbConnection; // Database connection instance

    public Loginpage() {
        dbConnection = new DBconnection();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        // Create the username and password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Create a login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            try {
                handleLogin(usernameField.getText(), passwordField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Create a GridPane to hold the login elements
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(usernameField, 0, 0);
        gridPane.add(passwordField, 0, 1);
        gridPane.add(loginButton, 0, 2);

        // Create a StackPane to overlay the background and the login elements
        StackPane stackPane = new StackPane();
        stackPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null))); // Set the background color
        stackPane.getChildren().addAll(gridPane);

        // Create the scene
        Scene scene = new Scene(stackPane, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void handleLogin(String username, String password) throws IOException {

        boolean isAuthenticated = dbConnection.authenticateUser(username, password);

        if (isAuthenticated) {
            // User is authenticated, you can navigate to another scene or perform further actions.
            System.out.println("Authentication successful");

            // Create and open the main stage (music player)
            MediaPlayerController mediaPlayerController = new MediaPlayerController();
            Stage mediaPlayerStage = new Stage();
            mediaPlayerController.start(mediaPlayerStage);
        } else {
            System.out.println("Authentication failed");
        }
    }
}