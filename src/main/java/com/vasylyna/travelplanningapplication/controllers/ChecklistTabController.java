package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.ChecklistDAO;
import com.vasylyna.travelplanningapplication.database.entity.ChecklistItem;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;
import com.vasylyna.travelplanningapplication.util.SceneLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ChecklistTabController {

    @FXML
    private VBox checklistContainer;

    @FXML
    private TextField addItemField;

    @FXML
    public void initialize() {
        showAllItems();
    }
    @FXML
    protected void onMainTab() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/main-tab/main-tab-view.fxml",
                (Stage) checklistContainer.getScene().getWindow());
    }

    @FXML
    protected void onExit() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml",
                (Stage) checklistContainer.getScene().getWindow());
    }

    @FXML
    protected void onWeather() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/weather-tab/weather-tab-view.fxml",
                (Stage) checklistContainer.getScene().getWindow());
    }

    @FXML
    protected void onJourneys() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/journeys-tab/journeys-tab-view.fxml",
                (Stage) checklistContainer.getScene().getWindow());
    }

    @FXML
    protected void onFinances() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/finances-tab/finances-tab-view.fxml",
                (Stage) checklistContainer.getScene().getWindow());
    }

    @FXML
    protected void onAddItem() {
        String name = addItemField.getText().trim();

        if (name.isEmpty()) {
            AlertDialogUtil.showErrorDialog("Помилка", "Поле не може бути порожнім. Введіть назву пункту.");
            return;
        }

        boolean success = ChecklistDAO.addItem(name);
        if (success) {
            AlertDialogUtil.showInfoDialog("Успіх", "Значення оновлено", "Пункт успішно додано до чекліста");
            showAllItems();
            addItemField.clear();
        } else {
            AlertDialogUtil.showErrorDialog("Помилка", "Не вдалося додати пункт, спробуйте, будь ласка, пізніше.");
        }
    }

    private void showAllItems() {
        List<ChecklistItem> items = ChecklistDAO.getAllItems();
        checklistContainer.getChildren().clear();
        for (ChecklistItem item : items) {
            HBox row = createChecklistRow(item);
            checklistContainer.getChildren().add(row);
        }
    }

    private HBox createChecklistRow(ChecklistItem item) {
        HBox checklistRow = new HBox();
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("checkbox");
        checkBox.setSelected(item.isChecked());

        Label itemLabel = new Label(item.getItemName());
        itemLabel.setWrapText(true);
        itemLabel.setMaxWidth(350);
        itemLabel.getStyleClass().add(item.isChecked() ? "item-label-checked" : "item-label-unchecked");

        checkBox.setOnAction(e -> {
            boolean newState = checkBox.isSelected();
            ChecklistDAO.toggleItemCheck(item.getId(), newState);

            itemLabel.getStyleClass().clear();
            itemLabel.getStyleClass().add(newState ? "item-label-checked" : "item-label-unchecked");
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteButton = new Button("❌");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(e -> {
            boolean confirmed = AlertDialogUtil.showConfirmationDialog("Підтвердження", "Ви впевнені, що хочете видалити цей пункт?");
            if (confirmed) {
                ChecklistDAO.deleteItem(item.getId());
                checklistContainer.getChildren().remove(deleteButton.getParent());
            }
        });

        checklistRow.getChildren().addAll(checkBox, itemLabel, spacer, deleteButton);
        checklistRow.setSpacing(2);
        checklistRow.getStyleClass().add("checklist-row");
        return checklistRow;
    }
}
