package com.vasylyna.travelplanningapplication.dtos;

import com.vasylyna.travelplanningapplication.util.SpellingUtil;

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
        return SpellingUtil.capitalizeFirstLetter(description);
    }

}
