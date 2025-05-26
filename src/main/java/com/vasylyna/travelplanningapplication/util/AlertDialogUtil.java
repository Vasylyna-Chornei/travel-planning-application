package com.vasylyna.travelplanningapplication.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AlertDialogUtil {

    public static void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().clear();
        try (var stream = AlertDialogUtil.class.getResourceAsStream("/com/vasylyna/travelplanningapplication/images/error.png")) {
            if (stream != null) {
                stage.getIcons().add(new Image(stream));
            }
        } catch (Exception e) {
            showErrorDialog("Помилка", "Виникла помилка при завантаженні іконки. Спробуйте, будь ласка, пізніше.");
        }

        alert.showAndWait();
    }

    public static void showInfoDialog(String title, String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().clear();
        try (
             var smallIconStream = AlertDialogUtil.class.getResourceAsStream("/com/vasylyna/travelplanningapplication/images/success-small.png");
             var mainIconStream = AlertDialogUtil.class.getResourceAsStream("/com/vasylyna/travelplanningapplication/images/success-main.png");
        ) {
            if (smallIconStream != null) {
                stage.getIcons().add(new Image(smallIconStream));
            }

            if (mainIconStream != null) {
                ImageView icon = new ImageView(new Image(mainIconStream));
                alert.getDialogPane().setGraphic(icon);
            }
        } catch (Exception e) {
            showErrorDialog("Помилка", "Виникла помилка при завантаженні іконки. Спробуйте, будь ласка, пізніше.");
        }

        alert.showAndWait();
    }

    public static boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().clear();
        try (var stream = AlertDialogUtil.class.getResourceAsStream("/com/vasylyna/travelplanningapplication/images/question.png")) {
            if (stream != null) {
                stage.getIcons().add(new Image(stream));
            }
        } catch (Exception e) {
            showErrorDialog("Помилка", "Виникла помилка при завантаженні іконки. Спробуйте, будь ласка, пізніше.");
        }

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }

}
