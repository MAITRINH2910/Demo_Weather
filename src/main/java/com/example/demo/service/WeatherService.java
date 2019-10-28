package com.example.demo.service;

import com.example.demo.DTO.transfer.WeatherDTO;
import com.example.demo.DTO.transfer.WeatherDetailDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WeatherEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * This WeatherService Interface is used to create method for WeatherServiceImpl
 */
@Service
public interface WeatherService {
    void saveWeather(WeatherEntity weatherEntity);

    void deleteWeather(Long id);

    List<WeatherEntity> getWeatherByUser(UserEntity user);

    WeatherDTO getJsonLocalWeather(String lat, String lon);

    WeatherDetailDTO getJsonWeatherDetail(String cityName);

    WeatherEntity getJsonWeatherSearch(String cityName);

    // Query Database to find City
    List<WeatherEntity> getListCityByUserId(Long userId);

    // Query Database to find weather which is grouped by city
    List<WeatherEntity> getListWeatherGroupByUserAndCity(String city, Long userId);

    List<WeatherEntity> getListCityByUser(UserEntity user);

    List<List<WeatherEntity>> getListWeatherGroupByCity(UserEntity user);

    WeatherEntity filterWeatherByCity(String cityName);

    List<WeatherEntity> getListDetailsDTO(List<com.example.demo.DTO.transfer.WeatherPropertyDTO.List> listDetail) throws ParseException;
}
