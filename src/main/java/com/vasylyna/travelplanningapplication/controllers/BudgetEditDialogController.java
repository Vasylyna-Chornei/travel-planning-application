package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.TransactionDAO;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BudgetEditDialogController {

    @FXML
    private TextField amountField;

    private String budgetType;

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public Runnable onTransactionCompleted;

    public void setOnTransactionCompleted(Runnable onTransactionCompleted) {
        this.onTransactionCompleted = onTransactionCompleted;
    }

    @FXML
    protected void handleConfirm() {
        String amountText = amountField.getText();

        if (amountText == null || amountText.isEmpty()) {
            AlertDialogUtil.showErrorDialog("Помилка", "Це поле не може бути пустим. Будь ласка, введіть правильне числове значення.");
            return;
        };

        try {
            double amount = Double.parseDouble(amountText);
            if (amount < 0) {
                AlertDialogUtil.showErrorDialog("Помилка", "Сума не може бути менше нуля.");
                return;
            }

            String budgetTypeName = budgetType.equals("total") ? "загальний бюджет" : "залишок";
            boolean confirmed = AlertDialogUtil.showConfirmationDialog("Підтвердження",
                    "Ви впевнені, що хочете змінити " + budgetTypeName + " без створення транзакції?");

            if (!confirmed) {
                return;
            }

            boolean updated = TransactionDAO.updateBudgetDirectly(amount, LoginController.currentUserId, budgetType);
            if (updated) {
                AlertDialogUtil.showInfoDialog("Успіх", "Бюджет оновлено", "Нове значення збережено");

                if (onTransactionCompleted != null) {
                    onTransactionCompleted.run();
                }

                ((Stage) amountField.getScene().getWindow()).close();
            } else {
                AlertDialogUtil.showErrorDialog("Помилка", "Не вдалося оновити бюджет. Спробуйте пізніше");
            }
        } catch (NumberFormatException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Неправильний формат введення. Будь ласка, введіть правильне числове значення.");
        }
    }

    @FXML
    protected void handleCancel() {
        ((Stage) amountField.getScene().getWindow()).close();
    }
}
