package com.vasylyna.travelplanningapplication.dtos;

public class WeatherForecast {

    private String dateTime;
    private String temperature;
    private String description;

    public WeatherForecast(String dateTime, String temperature, String description) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getTemperature() {
        return (int) Math.round(Double.parseDouble(temperature));
    }

    public String getDescription() {
        return capitalizeFirstLetter(description);
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
