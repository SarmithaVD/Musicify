package com.kensoftph.javafxmedia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("media-player.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Musicify");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        Connection connection = DatabaseManager.connect();

        DatabaseManager.close(connection);
    }
}