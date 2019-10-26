package com.example.demo.entity;

import javax.persistence.*;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "weather")
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long weatherId;

    @Column(name = "icon")
    private String icon;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "date")
    private Date date;

    @Column(name = "temp")
    private Double temp;

    @Column(name = "description")
    private String description;

    @Column(name = "wind")
    private String wind;

    @Column(name = "humidity")
    private String humidity;

    @Column(name = "pressure")
    private String pressure;


    @Column(name = "tempMin")
    private Double tempMin;

    @Column(name = "tempMax")
    private Double tempMax;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public WeatherEntity() {
    }

    public WeatherEntity(String icon, String cityName, Double temp, String description, String wind, String humidity, String pressure, Date date) {
        this.icon = icon;
        this.cityName = cityName;
        this.temp = temp;
        this.description = description;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
        this.date = date;
    }

    public WeatherEntity(String icon, String cityName, String description, String wind, String humidity, String pressure, Date date, Double tempMin, Double tempMax) {
        this.icon = icon;
        this.cityName = cityName;
        this.date = date;
        this.description = description;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }
}
