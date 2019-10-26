package com.example.demo.DTO.display;

import java.time.Instant;

public class ForecastWeatherDetailDTO {
    private int index;
    private Instant day;
    private String image;
    private String tempMin;
    private String tempMax;
    private String description;
    private String wind;
    private String humidity;
    private String pressure;
    private String clouds;

    public ForecastWeatherDetailDTO(int index, Instant day, String image, String tempMin, String tempMax, String description, String wind, String humidity, String pressure, String clouds) {
        this.index = index;
        this.day = day;
        this.image = image;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.description = description;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
        this.clouds = clouds;
    }
}
