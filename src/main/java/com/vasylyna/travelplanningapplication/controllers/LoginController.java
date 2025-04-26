package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.UserDAO;
import com.vasylyna.travelplanningapplication.util.PasswordUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameOrEmailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    private void setStatus(String message, String styleClass) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("success", "error", "info");
        statusLabel.getStyleClass().add("status-label");
        statusLabel.getStyleClass().add(styleClass);
    }

    @FXML
    protected void onLogin() {
        String usernameOrEmail = usernameOrEmailField.getText();
        String password = passwordField.getText();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            setStatus("Будь ласка, заповніть всі поля.", "error");
            return;
        }

        UserDAO userDAO = new UserDAO();
        String storedHashedPassword = userDAO.getPasswordHash(usernameOrEmail);
        if (storedHashedPassword != null && PasswordUtil.checkPassword(password, storedHashedPassword)) {
            setStatus("Вхід успішний", "success");

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Головна сторінка");
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
                stage.show();

                Stage currentStage = (Stage) usernameOrEmailField.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                setStatus("Сталася помилка при відкритті програми.", "error");
            }
        } else {
            setStatus("Неправильний логін або пароль.", "error");
        }
    }

    @FXML
    protected void onRegister() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Реєстрація");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            Stage currentStage = (Stage) usernameOrEmailField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            setStatus("Сталася помилка при відкритті вікна реєстрації.", "error");
        }
    }
}
