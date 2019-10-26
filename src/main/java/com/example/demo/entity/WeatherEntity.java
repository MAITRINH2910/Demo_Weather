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

    public Long getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Long weatherId) {
        this.weatherId = weatherId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getCityName() {
        return cityName;
    }

    public void getCityName(String nameCity) {
        this.cityName = nameCity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

}
