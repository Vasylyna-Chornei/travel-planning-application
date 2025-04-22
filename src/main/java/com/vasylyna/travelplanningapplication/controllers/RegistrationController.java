package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.UserDAO;
import com.vasylyna.travelplanningapplication.util.PasswordUtil;
import com.vasylyna.travelplanningapplication.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label statusLabel;

    private void setStatus(String message, String styleClass) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("success", "error", "info");
        statusLabel.getStyleClass().add("status-label");
        statusLabel.getStyleClass().add(styleClass);
    }

    @FXML
    protected void onRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            setStatus("Будь ласка, заповніть всі поля.", "error");
            return;
        }

        if (!ValidationUtil.isEmailValid(email)) {
            setStatus("Неправильний формат електронної пошти.", "error");
            return;
        }

        if (!password.equals(confirmPassword)) {
            setStatus("Паролі не співпадають.", "error");
            return;
        }

        if (!ValidationUtil.isPasswordStrong(password)) {
            setStatus("Пароль повинен містити хоча б одну велику латинську літеру, одну малу літеру та одну цифру, і бути не менше 8 символів.", "info");
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.registerUser(username, email, hashedPassword);

        if (success) {
            setStatus("Реєстрація успішна!", "success");
        } else {
            setStatus("Такий користувач вже існує.", "error");
        }
    }
}