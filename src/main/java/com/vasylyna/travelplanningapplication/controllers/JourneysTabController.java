package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.CountryDAO;
import com.vasylyna.travelplanningapplication.database.entity.Country;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;
import com.vasylyna.travelplanningapplication.util.SceneLoaderUtil;
import com.vasylyna.travelplanningapplication.util.SpellingUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class JourneysTabController {

    @FXML
    private TextField visitedCountryNameField;

    @FXML
    private TextField desiredCountryNameField;

    @FXML
    private VBox visitedCountriesList;

    @FXML
    private VBox desiredCountriesList;

    public void initialize() {
        showAllVisitedCountries();
        showAllDesiredCountries();
    }

    @FXML
    protected void onFinances() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/finances-tab/finances-tab-view.fxml",
                (Stage) visitedCountriesList.getScene().getWindow());
    }

    @FXML
    protected void onExit() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml",
                (Stage) visitedCountriesList.getScene().getWindow());
    }

    @FXML
    protected void onWeather() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/weather-tab/weather-tab-view.fxml",
                (Stage) visitedCountriesList.getScene().getWindow());
    }

    @FXML
    protected void onMainTab() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/main-tab/main-tab-view.fxml",
                (Stage) visitedCountriesList.getScene().getWindow());
    }

    @FXML
    protected void onChecklist() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/checklist-tab/checklist-tab-view.fxml",
                (Stage) visitedCountriesList.getScene().getWindow());
    }

    @FXML
    protected void onAddVisitedCountry() {
        String countryName = visitedCountryNameField.getText().trim();

        if (countryName.isEmpty()) {
            AlertDialogUtil.showErrorDialog("Помилка", "Це поле не може залишатися пустим. Введіть назву країни, будь ласка");
            return;
        }

        String countryCode = CountryDAO.getCountryCodeByName(SpellingUtil.capitalizeFirstLetter(countryName));
        if (countryCode != null) {
            boolean success = CountryDAO.addCountry(SpellingUtil.capitalizeFirstLetter(countryName),countryCode, "visited_countries");
            if (success) {
                AlertDialogUtil.showInfoDialog("Успіх", "Значення оновлено", "Країну успішно додано до відвіданих");
                showAllVisitedCountries();
            }
        } else {
            AlertDialogUtil.showErrorDialog("Помилка", "Країну не знайдено. Перевірте правопис або спробуйте, будь ласка, пізніше.");
        }
    }

    protected void showAllVisitedCountries() {
        List<Country> visitedCountries = CountryDAO.getAllCountries(LoginController.currentUserId, "visited_countries");
        visitedCountriesList.getChildren().clear();
        visitedCountryNameField.clear();
        for (Country country : visitedCountries) {
            String countryName = country.getName();
            Label label = new Label(countryName);
            label.getStyleClass().add("country-label");
            javafx.application.Platform.runLater(() -> {
                double labelWidth = label.getWidth();
                label.setMinWidth(labelWidth);
                label.setMaxWidth(labelWidth);
            });

            label.setOnMouseEntered(e -> {
                label.setText("\u274C");
                label.setStyle("-fx-background-color: #d9dce1;");
            });
            label.setOnMouseExited(e -> {
                label.setText(countryName);
                label.setStyle("-fx-background-color: #e9ebf1;");
            });

            label.setOnMouseClicked(e -> {
                boolean confirmed = AlertDialogUtil.showConfirmationDialog("Підтвердження", "Ви впевнені, що хочете видалити цю країну із відвіданих?");
                if (confirmed) {
                    CountryDAO.deleteCountry(country.getId(), "visited_countries");
                    visitedCountriesList.getChildren().remove(label);
                }
            });

            visitedCountriesList.getChildren().add(label);
        }
    }

    @FXML
    protected void onAddDesiredCountry() {
        String countryName = desiredCountryNameField.getText().trim();

        if (countryName.isEmpty()) {
            AlertDialogUtil.showErrorDialog("Помилка", "Це поле не може залишатися пустим. Введіть назву країни, будь ласка");
            return;
        }

        String countryCode = CountryDAO.getCountryCodeByName(SpellingUtil.capitalizeFirstLetter(countryName));
        if (countryCode != null) {
            boolean success = CountryDAO.addCountry(SpellingUtil.capitalizeFirstLetter(countryName),countryCode, "desired_countries");
            if (success) {
                AlertDialogUtil.showInfoDialog("Успіх", "Значення оновлено", "Країну успішно додано до списку.");
                showAllDesiredCountries();
            }
        } else {
            AlertDialogUtil.showErrorDialog("Помилка", "Країну не знайдено. Перевірте правопис або спробуйте, будь ласка, пізніше.");
        }
    }

    protected void showAllDesiredCountries() {
        List<Country> desiredCountries = CountryDAO.getAllCountries(LoginController.currentUserId, "desired_countries");
        desiredCountriesList.getChildren().clear();
        desiredCountryNameField.clear();
        for (Country country : desiredCountries) {
            String countryName = country.getName();
            Label label = new Label(countryName);
            label.getStyleClass().add("country-label");

            javafx.application.Platform.runLater(() -> {
                double labelWidth = label.getWidth();
                label.setMinWidth(labelWidth);
                label.setMaxWidth(labelWidth);
            });

            label.setOnMouseEntered(e -> {
                label.setText("\u274C");
                label.setStyle("-fx-background-color: #d9dce1;");
            });
            label.setOnMouseExited(e -> {
                label.setText(countryName);
                label.setStyle("-fx-background-color: #e9ebf1;");
            });

            Label checkMark = new Label("\u2713");
            checkMark.getStyleClass().add("check-mark");

            checkMark.setOnMouseClicked(e -> {
                boolean confirmed = AlertDialogUtil.showConfirmationDialog("Підтвердження", "Ви впевнені, що хочете перенести цю країну у відвідані?");
                if (confirmed) {
                    CountryDAO.deleteCountry(country.getId(), "desired_countries");
                    CountryDAO.addCountry(countryName, country.getCode(), "visited_countries");

                    showAllVisitedCountries();
                    showAllDesiredCountries();
                }
            });

            HBox row = new HBox(checkMark, label);
            row.getStyleClass().add("country-row");
            row.setSpacing(10);

            label.setOnMouseClicked(e -> {
                boolean confirmed = AlertDialogUtil.showConfirmationDialog("Підтвердження", "Ви впевнені, що хочете видалити цю країну?");
                if (confirmed) {
                    CountryDAO.deleteCountry(country.getId(), "desired_countries");
                    desiredCountriesList.getChildren().remove(row);
                }
            });
            desiredCountriesList.getChildren().add(row);
        }
    }
}
