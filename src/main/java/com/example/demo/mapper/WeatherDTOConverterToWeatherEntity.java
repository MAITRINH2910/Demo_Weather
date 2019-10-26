package com.example.demo.mapper;

import com.example.demo.DTO.transfer.WeatherDTO;
import com.example.demo.entity.WeatherEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WeatherDTOConverterToWeatherEntity {

    ModelMapper modelMapper = new ModelMapper();

    public WeatherEntity weatherEntityConverter(WeatherDTO weatherDTO) {
        WeatherEntity weatherEntity = modelMapper.map(weatherDTO, WeatherEntity.class);
        weatherEntity.setIcon(weatherDTO.getWeather().get(0).getIcon());
        weatherEntity.setCityName(weatherDTO.getName());
        weatherEntity.setDate(weatherDTO.getTimezone());
        weatherEntity.setTemp(weatherDTO.getMain().getTemp());
        weatherEntity.setDescription(weatherDTO.getWeather().get(0).getDescription());
        weatherEntity.setWind(weatherDTO.getWind().getSpeed());
        weatherEntity.setHumidity(weatherDTO.getMain().getHumidity());
        weatherEntity.setPressure(weatherDTO.getMain().getPressure());
        return weatherEntity;
    }



}
