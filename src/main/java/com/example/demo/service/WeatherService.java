package com.example.demo.service;

import com.example.demo.DTO.transfer.WeatherDTO;
import com.example.demo.DTO.transfer.WeatherDetailDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WeatherEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This WeatherService Interface is used to create method for WeatherServiceImpl
 */
@Service
public interface WeatherService {
    void saveWeather(WeatherEntity weatherEntity);

    void deleteWeather(Long id);

    List<WeatherEntity> getWeatherByUser(UserEntity user);

    // Query Database to find City
    List<WeatherEntity> findCityByUserId(Long userId);

    // Query Database to find weather which is grouped by city
    List<WeatherEntity> findWeatherGroupByCity(String city, Long userId);

    WeatherDTO getCurrentLocalWeather(String lat, String lon);

    WeatherDetailDTO getJsonWeatherDetail(String cityName);

    WeatherEntity getJsonWeatherSearch(String cityName);

    List<WeatherEntity> getCitiesByUser(UserEntity user);

    List<List<WeatherEntity>> weatherGroupByCity(UserEntity user);

    WeatherEntity filterWeatherByCity(String cityName);
}
