package com.kensoftph.javafxmedia;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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

        // Load the background image
        Image backgroundImage = new Image("file:./images/loginpage.png");

        // Set the desired size for the background image
        double backgroundImageWidth = 1080;
        double backgroundImageHeight = 608;

        // Create a BackgroundSize with the desired width and height
        BackgroundSize backgroundSize = new BackgroundSize(backgroundImageWidth, backgroundImageHeight, true, true, true, false);

        // Create a BackgroundImage
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                javafx.scene.layout.BackgroundPosition.DEFAULT,
                backgroundSize);

        // Create the username and password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Create a login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));

        // Add styling to the login button
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setPadding(new javafx.geometry.Insets(10));

        // Create a title label
        Label titleLabel = new Label("Musicify Login");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Create a GridPane to hold the login elements
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(titleLabel, 0, 0);
        gridPane.add(usernameField, 0, 1);
        gridPane.add(passwordField, 0, 2);
        gridPane.add(loginButton, 0, 3);

        // Create a StackPane to overlay the background and the login elements
        StackPane stackPane = new StackPane();
        stackPane.setBackground(new javafx.scene.layout.Background(background)); // Set the background image
        stackPane.getChildren().addAll(gridPane);

        // Add padding to the StackPane
        stackPane.setPadding(new javafx.geometry.Insets(20));

        // Create the scene
        Scene scene = new Scene(stackPane, 1080, 608);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String username, String password) {
        // Implement your login logic here
        boolean isAuthenticated = dbConnection.authenticateUser(username, password);

        if (isAuthenticated) {
            // User is authenticated, you can navigate to another scene or perform further actions.
            System.out.println("Authentication successful");
        } else {
            System.out.println("Authentication failed");
        }
    }
}