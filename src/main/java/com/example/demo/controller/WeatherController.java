package com.example.demo.controller;

import com.example.demo.DTO.transfer.WeatherDetailDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WeatherEntity;
import com.example.demo.mapper.WeatherDTOConverterToWeatherEntity;
import com.example.demo.service.UserService;
import com.example.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class WeatherController is used to search weather, save weather, delete weather, weather forecast
 */
@Controller
public class WeatherController {

    private static final int days = 8;
    @Autowired
    WeatherService weatherService;

    @Autowired
    UserService userService;

    @Autowired
    WeatherDTOConverterToWeatherEntity weatherConverter;

    @Value("${host.http}")
    private String host_http;
    @Value("${domain}")
    private String domain_http;
    @Value("${tail.icon.path}")
    private String tail_icon_path;
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


    @GetMapping("/")
    public String homeDefault(Model model, Principal principal) {

        return processHome(model, principal);
    }
    /**
     * Search current Weather City
     *
     * @param cityName
     * @param principal
     * @return view Home
     */
    @GetMapping("/search-city")
    public String searchWeather(@RequestParam String cityName, Model model, Principal principal) {
        // Get result search : weather entity
        getWeatherSearch(cityName, principal, model);
        // Forward about page home
        return processHome(model, principal);
    }

    /**
     * Process Data when tranfer to View
     * @param model
     * @param principal
     * @return url page Home and Data weather by User
     */
    public String processHome(Model model, Principal principal) {
        UserEntity user = userService.getUserWithIcon();
        List<WeatherEntity> listCity = weatherService.getListCityByUser(user);
        model.addAttribute("listCities", listCity);
        List<List<WeatherEntity>> weatherGroupByCity = weatherService.getListWeatherGroupByCity(user);
        model.addAttribute("weatherList0", weatherGroupByCity);

        return "page_user/weather_search";
    }

    /**
     * get result search
     * @param cityName
     * @param principal
     */
    private void getWeatherSearch(String cityName, Principal principal, Model model) {
        try {
            // Get weather by rest template
            WeatherEntity weatherSearch = weatherService.getJsonWeatherSearch(cityName);
            // Get list weather by user by name city
            model.addAttribute("currentWeather", weatherSearch);
        } catch (Exception e) {
            // Name City search is not found
            model.addAttribute("msgSearch", "City is not found !");
        }
        WeatherEntity oldWeather = weatherService.filterWeatherByCity(cityName);
        if (oldWeather != null) {
            model.addAttribute("status", "update");
        } else {
            model.addAttribute("status", "add");
        }
    }
//    @GetMapping("/search-city")
//    private String findWeatherByCity(@RequestParam String cityName, Model model) {
//        // Return Weather Data or Not Found
//        try {
//            WeatherEntity currentWeather = weatherService.getJsonWeatherSearch(cityName);
//            model.addAttribute("currentWeather", currentWeather);
//            // Show History Weathers
//            UserEntity user = userService.getUserWithIcon();
//            List<WeatherEntity> listCity = weatherService.getListCityByUser(user);
//            model.addAttribute("listCities", listCity);
//            List<List<WeatherEntity>> weatherGroupByCity = weatherService.getListWeatherGroupByCity(user);
//            model.addAttribute("weatherList0", weatherGroupByCity);
//        } catch (Exception e) {
//            model.addAttribute("message", "City is not found!!!");
//        }
//
//        // Handle Button ADD --> UPDATE by Comparing Current Date and Latest Date of Current Weather
//        WeatherEntity oldWeather = weatherService.filterWeatherByCity(cityName);
//        if (oldWeather != null) {
//            model.addAttribute("status", "update");
//        } else {
//            model.addAttribute("status", "add");
//        }
//        return "page_user/weather_search";
//    }
     /**
     * Get list detail weather entity forecast
     * @param cityName
     * @return list detailweatherEntity forecast
     */
    private List<WeatherEntity> getListForeCast(String cityName) throws ParseException {
        // Get list forecast weather from API
        WeatherDetailDTO detailsWeatherDTO = weatherService.getJsonWeatherDetail(cityName);
        return weatherService.getListDetailsDTO(detailsWeatherDTO.getList());
    }

    /**
     * WEATHER DETAIL
     * 40 records for 5 continuous days => days = 40/5
     *
     * @param cityName
     * @param model
     * @return
     */
    @GetMapping("/weather-detail/{cityName}")
    public String foreCastWeather(@PathVariable String cityName, Model model) {
        try {
            // Get list forecast weather 5 day of city
            List<WeatherEntity> lstForecast = getListForeCast(cityName);
            if(lstForecast != null) {
                model.addAttribute("cityName", cityName.toUpperCase());
                model.addAttribute("timeToday", Instant.now());
                model.addAttribute("detail", lstForecast);
                return "page_user/weather_detail";
            }
        } catch (Exception e) {
            return "redirect:home-weather?message_forecast=notfound";
        }
        return "";
    }


    /**
     * ADD WEATHER
     *
     * @param cityName
     * @return
     */
    @GetMapping("/save-weather/{cityName}")
    private String saveWeather(@PathVariable String cityName) {
        UserEntity user = userService.getUser();
        WeatherEntity currentWeather = weatherService.getJsonWeatherSearch(cityName);

        WeatherEntity weather1 = new WeatherEntity(currentWeather.getIcon(), currentWeather.getCityName(),
                currentWeather.getTemp(), currentWeather.getDescription(), currentWeather.getWind(),
                currentWeather.getHumidity(), currentWeather.getPressure(), currentWeather.getDate());
        weather1.setUser(userService.findUserByUsername(user.getUsername()));
        // Get List Weather by User
        List<WeatherEntity> lstByUser = weatherService.getWeatherByUser(user);
        // Get List City By idUser va cityName
        List<WeatherEntity> lstByUserByCity = lstByUser.stream()
                .filter(weather -> cityName.trim().toLowerCase().equals(weather.getCityName().trim().toLowerCase()))
                .collect(Collectors.toList());
        if (lstByUserByCity.size() < 3) {
            weatherService.saveWeather(weather1);
            return "redirect:/";
        } else {
            lstByUserByCity
                    .sort((WeatherEntity w2, WeatherEntity w1) -> w2.getDate().compareTo(w1.getDate()));
            Optional<WeatherEntity> entity = lstByUserByCity.stream().findFirst();
            weatherService.deleteWeather(entity.get().getWeatherId());
            insertWeather(cityName, user);
            return "redirect:/";
        }
    }

    /**
     * Method saveWeather will call this method to set current Date and User for Current Weather
     *
     * @param cityName
     * @param userEntity
     */
    private void insertWeather(String cityName, UserEntity userEntity) {
        WeatherEntity result = weatherService.getJsonWeatherSearch(cityName);
        result.setUser(userEntity);
        weatherService.saveWeather(result);
    }

    /**
     * UPDATE WEATHER
     *
     * @param cityName
     * @return
     */
    @GetMapping("/update-weather/{cityName}")
    private String updateWeather(@PathVariable String cityName, Model model) {
        UserEntity user = userService.getUser();
        WeatherEntity currentWeather = weatherService.getJsonWeatherSearch(cityName);

        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        WeatherEntity oldWeather = weatherService.filterWeatherByCity(cityName);
        //Set New Record for Current Weather
        oldWeather.setIcon(currentWeather.getIcon());
        oldWeather.setTemp(currentWeather.getTemp());
        oldWeather.setWind(currentWeather.getWind());
        oldWeather.setHumidity(currentWeather.getHumidity());
        oldWeather.setPressure(currentWeather.getPressure());
        oldWeather.setDescription(currentWeather.getDescription());
        oldWeather.setDate(currentWeather.getDate());
        weatherService.saveWeather(oldWeather);

        model.addAttribute("weatherList", weatherList);
        return "redirect:/";
    }

    /**
     * DELETE WEATHER
     *
     * @param id
     * @return
     */
    @PostMapping("/delete-weather")
    private String deleteWeather(@RequestParam long id) {
        weatherService.deleteWeather(id);
        return "redirect:/";
    }
}
