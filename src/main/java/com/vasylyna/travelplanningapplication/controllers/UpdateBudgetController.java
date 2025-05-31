package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.DatabaseManager;
import com.vasylyna.travelplanningapplication.database.TransactionDAO;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateBudgetController {

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField descriptionField;

    private boolean isAddition = true;

    public void setAddition(boolean isAddition) {
        this.isAddition = isAddition;
    }

    public Runnable onTransactionCompleted;

    public void setOnTransactionCompleted(Runnable onTransactionCompleted) {
        this.onTransactionCompleted = onTransactionCompleted;
    }

    @FXML
    protected void handleConfirm() {
        String amountText = amountField.getText();
        String category = categoryComboBox.getValue();
        String description = descriptionField.getText();

        if (amountText == null || amountText.isEmpty() || category == null) {
            AlertDialogUtil.showErrorDialog("Помилка", "Поля не можуть бути пустими. Будь ласка, введіть суму та виберіть категорію.");
            return;
        }

        Connection connection = null;
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                AlertDialogUtil.showErrorDialog("Помилка", "Сума не може бути менше або дорівнювати нулю.");
                return;
            }
            String transactionType = isAddition ? "ADD" : "WITHDRAW";

            connection = DatabaseManager.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean budgetUpdated = TransactionDAO.updateRemainingBudget(amount, LoginController.currentUserId, transactionType, connection);
            if (!budgetUpdated) {
                AlertDialogUtil.showErrorDialog("Помилка", "Виникла помилка при оновленні бюджету. Спробуйте пізніше");
                connection.rollback();
                return;
            }

            boolean success = TransactionDAO.addTransaction(amount, category, description, LoginController.currentUserId, transactionType, connection);
            if (success) {
                connection.commit();
                AlertDialogUtil.showInfoDialog("Успіх", "Транзакцію додано", "Операцію виконано успішно");

                if (onTransactionCompleted != null) {
                    onTransactionCompleted.run();
                }

                ((Stage) amountField.getScene().getWindow()).close();
            } else {
                connection.rollback();
                AlertDialogUtil.showErrorDialog("Помилка", "Виникла помилка при збереженні транзакції. Спробуйте пізніше");
            }
        } catch (NumberFormatException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Неправильний формат введення. Будь ласка, введіть правильне числове значення.");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {}
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {}
            }
        }
    }
}
