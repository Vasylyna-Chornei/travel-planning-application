package com.vasylyna.travelplanningapplication;

import com.vasylyna.travelplanningapplication.util.SceneLoaderUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneLoaderUtil.createWindow("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml", "Trip planner", stage);
    }

    public static void main(String[] args) {
        launch();
    }
}