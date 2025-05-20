package com.vasylyna.travelplanningapplication.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vasylyna.travelplanningapplication.dtos.WeatherForecast;

import java.util.ArrayList;
import java.util.List;

public class WeatherParserUtil {

    public static List<WeatherForecast> parseForecastFromJson(String json) {
        List<WeatherForecast> forecasts = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode list = root.path("list");

            for (JsonNode item : list) {
                String dateTime = item.path("dt_txt").asText();
                String temp = item.path("main").path("temp").asText();
                String description = item.path("weather").get(0).path("description").asText();

                forecasts.add(new WeatherForecast(dateTime, temp, description));
            }
        } catch (Exception e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Не вдалося отримати дані про погоду. Спробуйте пізніше");
        }

        return forecasts;
    }
}
