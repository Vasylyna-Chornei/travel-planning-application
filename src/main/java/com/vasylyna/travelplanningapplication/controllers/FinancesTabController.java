package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.TransactionDAO;
import com.vasylyna.travelplanningapplication.database.entity.Transaction;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;
import com.vasylyna.travelplanningapplication.util.SceneLoaderUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class FinancesTabController {
    @FXML
    private HBox header;

    @FXML
    private Label totalBudgetLabel;

    @FXML
    private Label remainingBudgetLabel;

    @FXML
    private ProgressBar budgetProgressBar;

    @FXML
    private ListView<Transaction> listView;


    public void initialize() {
        updateBudgetLabels();
        fillTableWithTransactions();
    }

    @FXML
    protected void onMainTab() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/main-tab/main-tab-view.fxml",
                (Stage) header.getScene().getWindow());
    }

    @FXML
    protected void onExit() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml",
                (Stage) header.getScene().getWindow());
    }

    @FXML
    protected void onWeather() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/weather-tab/weather-tab-view.fxml",
                (Stage) header.getScene().getWindow());
    }

    @FXML
    protected void onJourneys() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/journeys-tab/journeys-tab-view.fxml",
                (Stage) header.getScene().getWindow());
    }

    @FXML
    protected void onAddBudget() {
        onAddTransaction(true);
    }

    @FXML
    protected void onWithdrawBudget() {
        onAddTransaction(false);
    }


    @FXML
    protected void onAddTransaction(boolean isAddition) {
        UpdateBudgetController controller = SceneLoaderUtil.createDialogWindowAndReturnController("/com/vasylyna/travelplanningapplication/update-budget-dialog/update-budget-dialog-view.fxml",
                "Транзакція", (Stage) header.getScene().getWindow(), false);

        if (controller != null) {
            controller.setAddition(isAddition);
            controller.setOnTransactionCompleted(() -> {
                updateBudgetLabels();
                fillTableWithTransactions();
            });
        }
    }

    @FXML
    protected void onChangeRemainingBudget() {
        openBudgetEditDialog("remaining");
    }

    @FXML
    protected void onChangeTotalBudget() {
        openBudgetEditDialog("total");
    }

    @FXML
    protected void openBudgetEditDialog(String budgetType) {
        BudgetEditDialogController controller = SceneLoaderUtil.createDialogWindowAndReturnController("/com/vasylyna/travelplanningapplication/budget-edit-dialog/budget-edit-dialog-view.fxml",
                "Встановити бюджет", (Stage) header.getScene().getWindow(), false);

        if (controller != null) {
            controller.setBudgetType(budgetType);
            controller.setOnTransactionCompleted(this::updateBudgetLabels);
        }
    }

    private void updateBudgetLabels() {
        double total = TransactionDAO.getCurrentBudget(LoginController.currentUserId, "total");
        double remaining = TransactionDAO.getCurrentBudget(LoginController.currentUserId, "remaining");

        totalBudgetLabel.setText(String.format("%.2f", total));
        remainingBudgetLabel.setText(String.format("%.2f", remaining));

        double progress = (total == 0) ? 0 : remaining / total;
        budgetProgressBar.setProgress(progress);
    }

    private void fillTableWithTransactions() {
        List<Transaction> transactions = TransactionDAO.getTransactions(LoginController.currentUserId);
        ObservableList<Transaction> observableTransactions = FXCollections.observableArrayList(transactions);
        listView.setItems(observableTransactions);

        if (observableTransactions.isEmpty()) {
            Label placeholder = new Label("Поки що нема транзакцій");
            placeholder.getStyleClass().add("transactions-placeholder");
            listView.setPlaceholder(placeholder);
        }

        listView.setCellFactory(listView -> new ListCell<Transaction>() {
            @Override
            protected void updateItem(Transaction transaction, boolean empty) {
                super.updateItem(transaction, empty);
                if (empty || transaction == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox cellVBox = new VBox();
                    cellVBox.setSpacing(5);
                    cellVBox.setPadding(new Insets(5));

                    Label categoryLabel = new Label(transaction.getCategory());
                    categoryLabel.getStyleClass().add("category-label");

                    Label descriptionLabel = new Label(transaction.getDescription());
                    descriptionLabel.setWrapText(true);
                    descriptionLabel.setMaxWidth(280);
                    descriptionLabel.getStyleClass().add("description-label");

                    VBox leftBox = new VBox(categoryLabel, descriptionLabel);

                    String formattedAmount = String.format("%.2f", transaction.getAmount());
                    if (transaction.getTransactionType().equals("ADD")) {
                        formattedAmount = "+" + formattedAmount;
                    } else {
                        formattedAmount = "-" + formattedAmount;
                    }
                    Label amountLabel = new Label(formattedAmount);
                    amountLabel.getStyleClass().add(
                            transaction.getTransactionType().equals("ADD") ? "income-label" : "expense-label"
                    );

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                    Label timeLabel = new Label(transaction.getDate().format(timeFormatter));
                    timeLabel.getStyleClass().add("time-label");

                    Label dateLabel = new Label(transaction.getDate().format(dateFormatter));
                    dateLabel.getStyleClass().add("date-label");

                    Button deleteButton = new Button("\uD83D\uDDD1");
                    deleteButton.getStyleClass().clear();
                    deleteButton.getStyleClass().add("delete-button");
                    deleteButton.setOnAction(event -> {
                        boolean confirmed = AlertDialogUtil.showConfirmationDialog("Підтвердження", "Ви впевнені, що хочете видалити цю транзакцію?");

                        if (confirmed) {
                            TransactionDAO.deleteTransaction(transaction);
                            getListView().getItems().remove(transaction);
                            updateBudgetLabels();
                        }
                    });

                    VBox dateAndTime = new VBox(0, timeLabel, dateLabel);
                    dateAndTime.setAlignment(Pos.CENTER_RIGHT);

                    VBox rightBox = new VBox(amountLabel, dateAndTime);
                    rightBox.setAlignment(Pos.CENTER_RIGHT);
                    rightBox.getChildren().add(deleteButton);

                    HBox hBox = new HBox(leftBox, new Region(), rightBox);
                    HBox.setHgrow(hBox.getChildren().get(1), Priority.ALWAYS);

                    cellVBox.getChildren().add(hBox);
                    setGraphic(cellVBox);
                }
            }
        });
    }
}

