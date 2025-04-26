package com.vasylyna.travelplanningapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Реєстрація");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}