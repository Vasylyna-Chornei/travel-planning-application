package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.dtos.WeatherForecast;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;
import com.vasylyna.travelplanningapplication.util.SceneLoaderUtil;
import com.vasylyna.travelplanningapplication.util.SpellingUtil;
import com.vasylyna.travelplanningapplication.util.WeatherParserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.control.TextField;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



public class WeatherTabController {

    @FXML
    private TextField cityInput;
    @FXML
    private Label cityLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label descLabel;
    @FXML
    private VBox weatherBox;
    @FXML
    private HBox forecastBox;

    private static final String API_KEY = "6cfb88f418a1355e9b79593e3bd30430";

    @FXML
    protected void onMainTab() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/main-tab/main-tab-view.fxml",
                (Stage) cityInput.getScene().getWindow());
    }

    @FXML
    protected void onFinances() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/finances-tab/finances-tab-view.fxml",
                (Stage) cityInput.getScene().getWindow());

    }

    @FXML
    protected void onExit() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml",
                (Stage) cityInput.getScene().getWindow());
    }

    @FXML
    protected void onJourneys() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/journeys-tab/journeys-tab-view.fxml",
                (Stage) cityInput.getScene().getWindow());
    }

    @FXML
    protected void onChecklist() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/checklist-tab/checklist-tab-view.fxml",
                (Stage) cityInput.getScene().getWindow());
    }

    @FXML
    protected void onSearchCity() {
        String city = cityInput.getText().trim();
        String capitalizeCity = SpellingUtil.capitalizeFirstLetter(city);

        if (capitalizeCity.isEmpty()) {
            return;
        }

        try {
            String urlString = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&appid=%s&lang=ua",
                    capitalizeCity, API_KEY);

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }
            input.close();

            List<WeatherForecast> forecasts = WeatherParserUtil.parseForecastFromJson(response.toString());

            if (!forecasts.isEmpty()) {
                WeatherForecast thisDay = forecasts.get(0);
                cityLabel.setText(capitalizeCity);
                int temp = thisDay.getTemperature();
                tempLabel.setText(temp + "°C");
                tempLabel.getStyleClass().clear();
                tempLabel.getStyleClass().add("temperature-label");

                if (temp > 35) tempLabel.getStyleClass().add("very-hot-temperature");
                else if (temp > 29) tempLabel.getStyleClass().add("hot-temperature");
                else if (temp > 19) tempLabel.getStyleClass().add("warm-temperature");
                else if (temp < 0) tempLabel.getStyleClass().add("cold-temperature");
                else tempLabel.getStyleClass().add("coolly-temperature");

                descLabel.setText(thisDay.getDescription());

                weatherBox.setVisible(true);
                showFiveDayForecast(forecasts);
            }


        } catch (Exception e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Не вдалося отримати прогноз погоди для міста " + city + ". Перевірте, будь ласка, правопис або спробуйте пізніше");
        }
    }

    private void showFiveDayForecast(List<WeatherForecast> forecasts) {
        forecastBox.getChildren().clear();

        String currentDate = null;
        VBox dayCard = null;

        for (WeatherForecast forecast : forecasts) {
            String formattedDate = LocalDateTime.parse(forecast.getDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            if (currentDate == null || !formattedDate.equals(currentDate)) {
                currentDate = formattedDate;
                dayCard = new VBox(5);
                dayCard.getStyleClass().add("day-card");

                Label dateLabel = new Label(formattedDate);
                dateLabel.getStyleClass().add("date-label");

                dayCard.getChildren().add(dateLabel);
                forecastBox.getChildren().add(dayCard);
            }

            String time = LocalDateTime.parse(forecast.getDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .toLocalTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));

            int temp = forecast.getTemperature();
            Text timeAndDescText = new Text(time + " – " + forecast.getDescription() + ", ");
            Text tempText = new Text(temp + "°C");

            tempText.getStyleClass().clear();
            tempText.getStyleClass().add("temperature-forecast");
            if (temp > 35) tempText.getStyleClass().add("very-hot-temperature-forecast");
            else if (temp > 29) tempText.getStyleClass().add("hot-temperature-forecast");
            else if (temp > 19) tempText.getStyleClass().add("warm-temperature-forecast");
            else if (temp < 0) tempText.getStyleClass().add("cold-temperature-forecast");
            else tempText.getStyleClass().add("coolly-temperature-forecast");

            TextFlow forecastLabel = new TextFlow(timeAndDescText, tempText);
            forecastLabel.getStyleClass().add("forecast-label");

            dayCard.getChildren().add(forecastLabel);
            forecastBox.setVisible(true);
        }
    }
}
