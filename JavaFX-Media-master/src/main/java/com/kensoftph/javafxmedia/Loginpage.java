package com.kensoftph.javafxmedia;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));

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

    private void handleLogin(String username, String password) {
        // Implement your login logic here
        boolean isAuthenticated = dbConnection.authenticateUser(username, password);

        if (isAuthenticated) {
            // User is authenticated, close the login stage and open the main stage
            System.out.println("Authentication successful");
            mainStage.close();

            // Create and open the main stage
            MediaPlayerController mediaPlayerController = new MediaPlayerController();
            Stage mediaPlayerStage = new Stage();
            mediaPlayerController.start(mediaPlayerStage);
        } else {
            System.out.println("Authentication failed");
        }
    }
}