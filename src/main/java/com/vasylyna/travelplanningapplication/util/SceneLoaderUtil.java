package com.vasylyna.travelplanningapplication.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneLoaderUtil {

    private static final Image APP_ICON = new Image(SceneLoaderUtil.class.getResourceAsStream("/com/vasylyna/travelplanningapplication/images/plane-globe.png"));

    public static void createWindow(String fxmlPath, String title, Stage currentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneLoaderUtil.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.getIcons().add(APP_ICON);
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            if (currentStage != null) {
                currentStage.close();
            }

        } catch (Exception e) {
            AlertDialogUtil.showErrorDialog("Помилка завантаження вікна", "Сталася помилка при завантаженні вікна. Спробуйте ще раз пізніше.");
            e.printStackTrace();
        }
    }

    public static <T> T createDialogWindowAndReturnController(String fxmlPath, String title, Stage owner, boolean resizable) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneLoaderUtil.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initOwner(owner);
            stage.setResizable(resizable);

            Image icon = new Image(SceneLoaderUtil.class.getResourceAsStream("/com/vasylyna/travelplanningapplication/images/accounting.png"));
            stage.getIcons().add(icon);

            stage.show();

            return fxmlLoader.getController();

        } catch (Exception e) {
            AlertDialogUtil.showErrorDialog("Помилка завантаження вікна", "Сталася помилка при завантаженні вікна. Спробуйте ще раз пізніше.");
            return null;
        }
    }

    public static void loadScene(String fxmlPath, Stage currentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneLoaderUtil.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(),
                    Screen.getPrimary().getVisualBounds().getHeight());
            currentStage.setScene(scene);

        } catch (Exception e) {
            AlertDialogUtil.showErrorDialog(
                    "Помилка завантаження вікна",
                    "Сталася помилка при завантаженні вікна. Спробуйте ще раз пізніше."
            );
        }
    }
}
