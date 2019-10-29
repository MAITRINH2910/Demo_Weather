package com.example.demo.service.Impl;

import com.example.demo.DTO.transfer.WeatherDTO;
import com.example.demo.DTO.transfer.WeatherDetailDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WeatherEntity;
import com.example.demo.mapper.WeatherDTOConverterToWeatherEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WeatherRepository;
import com.example.demo.service.WeatherService;
import com.example.demo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class WeatherServiceImpl is used to implement WeatherService
 * Handle Logic Business Related to Weather: find Weather, find City
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    @Value("${host.http}")
    private String host_http;
    @Value("${domain}")
    private String domain;
    @Value("${ver.app}")
    private String ver_app;
    @Value("${key}")
    private String key;
    @Value("${key_local_weather}")
    private String key_local_weather;
    @Value("${weather.url.current}")
    private String weatherCurrent;
    @Value("${weather.url.forecast}")
    private String weatherForecast;
    @Value("${tail.icon.path}")
    private String tail_icon_path;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WeatherRepository weatherRepository;

    @Autowired
    WeatherDTOConverterToWeatherEntity weatherConverter;

    /**
     * This method to save weather
     *
     * @param weatherEntity
     */
    @Override
    public void saveWeather(WeatherEntity weatherEntity) {
        weatherRepository.save(weatherEntity);
    }

    /**
     * This method to delete weather by id
     *
     * @param id
     */
    @Override
    public void deleteWeather(Long id) {
        WeatherEntity weatherEntity = weatherRepository.findByWeatherId(id);
        weatherRepository.delete(weatherEntity);
    }

    /**
     * Find Weather By User Id by Query to Database
     *
     * @param userId
     * @return
     */
    @Override
    public List<WeatherEntity> getListCityByUserId(Long userId) {
        return weatherRepository.findCity(userId);
    }

    /**
     * Find Weather Group By City by Query to Database
     *
     * @param city
     * @return
     */
    @Override
    public List<WeatherEntity> getListWeatherGroupByUserAndCity(String city, Long userId) {
        return weatherRepository.findWeatherGroupByCity(city, userId);
    }

    /**
     * Find Weather By UserEntity
     *
     * @return
     */
    @Override
    public List<WeatherEntity> getWeatherByUser(UserEntity user) {
        return weatherRepository.findAllByUser(user);
    }

    /**
     * Convert String to StringBuilder
     *
     * @return
     */
    public StringBuilder convertStringToStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder;
    }

    /**
     * Get Url Api Local Weather by lat and lon
     *
     * @param lat
     * @param lon
     * @return
     */
    private StringBuilder urlApiGetCurrentLocation(String lat, String lon) {
        return convertStringToStringBuilder().append(host_http + "api." + domain + "/data/" + ver_app + weatherCurrent + "lat=" + lat + "&lon=" + lon + "&APPID=" + key_local_weather + "&&units=metric");
    }

    /**
     * Get Current Local Weather
     *
     * @param lat
     * @param lon
     * @return
     */
    @Override
    public WeatherDTO getJsonLocalWeather(String lat, String lon) {
        RestTemplate restTemplate = new RestTemplate();
        WeatherDTO weatherDTO = restTemplate.getForObject(urlApiGetCurrentLocation(lat, lon).toString(), WeatherDTO.class);
        return weatherDTO;
    }

    /**
     * @param cityName
     * @return
     */
    private StringBuilder urlApiGetWeather(String cityName) {
        return convertStringToStringBuilder().append(host_http + "api." + domain + "/data/" + ver_app + weatherCurrent + "q=" + cityName + "&units=metric" + "&APPID=" + key);
    }

    /**
     * Get Weather Data(WeatherDTO) via RestTemplate Object
     * Convert WeatherDTO to Weather Entity
     * Return WeatherEntity type
     *
     * @return
     */
    @Override
    public WeatherEntity getJsonWeatherSearch(String cityName) {
        RestTemplate restTemplate = new RestTemplate();
        WeatherDTO weatherDTO = restTemplate.getForObject(urlApiGetWeather(cityName).toString(), WeatherDTO.class);
        return weatherConverter.weatherEntityConverter(weatherDTO);
    }

    /**
     * URL get forecast weather by city name
     *
     * @param cityName
     * @return
     */
    private StringBuilder urlApiGetForecast(String cityName) {
        return convertStringToStringBuilder().append(host_http + "api." + domain + "/data/" + ver_app + weatherForecast + "q=" + cityName + "&units=metric" + "&APPID=" + key);
    }

    /**
     * Get Weather Detail Data(WeatherDetailDTO) via ResTemplate Object
     * Return WeatherDetailDTO type
     *
     * @param cityName
     * @return
     */
    public WeatherDetailDTO getJsonWeatherDetail(String cityName) {
        RestTemplate restTemplate = new RestTemplate();
        WeatherDetailDTO futureWeather = restTemplate.
                getForObject(urlApiGetForecast(cityName).toString(), WeatherDetailDTO.class);
        return futureWeather;
    }

    /**
     * Get List City By User base on weatherService
     *
     * @param user
     * @return
     */
    public List<WeatherEntity> getListCityByUser(UserEntity user) {
        List<WeatherEntity> cityList = weatherRepository.findCity(user.getId());
        return cityList;
    }

    /**
     * Get List Weather By City of User
     *
     * @param user
     * @return
     */
    public List<List<WeatherEntity>> getListWeatherGroupByCity(UserEntity user) {
        List<WeatherEntity> cityList = weatherRepository.findCity(user.getId());
        List<List<WeatherEntity>> weatherGroupByCity = new ArrayList<>();
        for (int i = 0; i < cityList.size(); i++) {
            weatherGroupByCity.add((weatherRepository.findWeatherGroupByCity(cityList.get(i).getCityName(), user.getId())));
        }
        return weatherGroupByCity;
    }

    /**
     * Get Current Weather by filtering city and date to handle button Add <-> Update
     *
     * @param cityName
     * @return
     */
    public WeatherEntity filterWeatherByCity(String cityName) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication(); //get Auth User in SecurityContextHolder
        UserEntity user = userRepository.findByUsername(authUser.getName());
        // Get List Weather
        List<WeatherEntity> weatherList = weatherRepository.findAllByUser(user);
        // filter weather by city
        List<WeatherEntity> listWeatherByCity = weatherList.stream()
                .filter(x -> x.getCityName().equals(cityName)).collect(Collectors.toList());
        // filter weather by comparing old date and new date
        WeatherEntity curWeather = listWeatherByCity.stream().
                filter(x -> CommonUtil.dateToString(x.getDate()).equalsIgnoreCase(CommonUtil.dateToString(new Date()))).findAny().orElse(null);

        return curWeather;
    }

    /**
     * Get List Weather Forecast
     * @param listDetail
     * @return
     * @throws ParseException
     */
    @Override
    public List<WeatherEntity> getListDetailsDTO(List<com.example.demo.DTO.transfer.WeatherPropertyDTO.List> listDetail) throws ParseException {
        int SIZE_WEATHER_REPEAT = 7;
        List<WeatherEntity> futureWeatherList = new ArrayList<WeatherEntity>();
        for (int i = 3; i < 40; i += SIZE_WEATHER_REPEAT) {
            futureWeatherList.add(new WeatherEntity((host_http + domain + "/img/w/" + listDetail.get(i).getWeather().get(0).getIcon() + tail_icon_path),
                    listDetail.get(i).getWeather().get(0).getDescription(),
                    listDetail.get(i).getWind().getSpeed(),
                    listDetail.get(i).getMain().getHumidity(),
                    listDetail.get(i).getMain().getPressure(),
                    CommonUtil.stringToDate(listDetail.get(i).getDt_txt()),
                    listDetail.get(i).getMain().getTemp_min(),
                    listDetail.get(i).getMain().getTemp_min()));
        }
        return futureWeatherList;
    }

}
