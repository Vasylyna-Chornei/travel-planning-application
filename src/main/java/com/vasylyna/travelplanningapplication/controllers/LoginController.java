package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.TransactionDAO;
import com.vasylyna.travelplanningapplication.database.UserDAO;
import com.vasylyna.travelplanningapplication.util.PasswordUtil;
import com.vasylyna.travelplanningapplication.util.SceneLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameOrEmailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    public static int currentUserId;

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
            currentUserId = userDAO.getUserID(usernameOrEmail);
            TransactionDAO.createInitialBudgetForUser(currentUserId);

            SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/main-tab/main-tab-view.fxml",
                    (Stage) usernameOrEmailField.getScene().getWindow());
        } else {
            setStatus("Неправильний логін або пароль.", "error");
        }
    }

    @FXML
    protected void onRegister() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml",
                (Stage) usernameOrEmailField.getScene().getWindow());
    }
}
