package com.example.demo.controller;

import com.example.demo.DTO.display.ForecastWeatherDetailDTO;
import com.example.demo.DTO.transfer.WeatherDetailDTO;
import com.example.demo.DTO.transfer.WeatherPropertyDTO.ListDetail;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WeatherEntity;
import com.example.demo.mapper.WeatherDTOConverterToWeatherEntity;
import com.example.demo.service.UserService;
import com.example.demo.service.WeatherService;
import com.example.demo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class WeatherController is used to 
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

    /**
     * SEARCH WEATHER
     *
     * @param cityName
     * @param model
     * @param modelMap
     * @return
     */
    @GetMapping("/search-city")
    private String findWeatherByCity(@RequestParam String cityName, Model model, ModelMap modelMap) {
        // Return Weather Data or Not Found
        try {
            WeatherEntity currentWeather = weatherService.getJsonWeatherSearch(cityName);
            model.addAttribute("currentWeather", currentWeather);
            // Show History Weathers
            UserEntity user = userService.getAuthUser();
            List<WeatherEntity> listCity = weatherService.getCitiesByUser(user);
            model.addAttribute("listCities", listCity);
            List<List<WeatherEntity>> weatherGroupByCity = weatherService.weatherGroupByCity(user);
            model.addAttribute("weatherList0", weatherGroupByCity);
        } catch (Exception e) {
            model.addAttribute("message", "City is not found!!!");
        }

        // Handle Button ADD --> UPDATE by Comparing Current Date and Latest Date of Current Weather
        WeatherEntity oldWeather = weatherService.filterWeatherByCity(cityName);
        if (oldWeather != null) {
            modelMap.addAttribute("status", "update");
        } else {
            modelMap.addAttribute("status", "add");
        }
        return "page_user/weather_search";
    }

    private List<ForecastWeatherDetailDTO> getListDetailsDTO(List<ListDetail> listDetail){
        int SIZE_WEATHER_REPEAT = 8;
        List<ForecastWeatherDetailDTO> lstForecast = new ArrayList<ForecastWeatherDetailDTO>();
        for (int i = 0; i < listDetail.size(); i += SIZE_WEATHER_REPEAT) {
            lstForecast.add(new ForecastWeatherDetailDTO(i, listDetail.get(i).getDt_txt(),
                    listDetail.get(i).getWeather().get(0).getIcon(),
                    listDetail.get(i).getMain().getTempMin(),
                    listDetail.get(i).getMain().getTempMax(),
                    listDetail.get(i).getWeather().get(0).getDescription(),
                    listDetail.get(i).getWind().getSpeed(),
                    listDetail.get(i).getMain().getHumidity(),
                    listDetail.get(i).getMain().getPressure(),
                    listDetail.get(i).getClouds().getAll()));
        }
        return lstForecast;
    }
//    /**
//     * Get list detail weather entity forecast
//     * @param cityName
//     * @return list detailweatherEntity forecast
//     */
//    private List<ForecastWeatherDetailDTO> getListDetails5Day(String cityName){
//        // Get list forecast weather from API
//        WeatherDetailDTO detailsWeatherDTO = weatherService.getJsonWeatherDetail(cityName);
//        return getListDetailsDTO(detailsWeatherDTO.getListWeather());
//    }
//
//
//    @GetMapping("/weather-detail/{cityName}")
//    public String foreCast5Day(@PathVariable String cityName, Model model) {
//        WeatherEntity currentWeather = weatherService.getJsonWeatherSearch(cityName);
//        model.addAttribute("currentWeather", currentWeather);
//        try {
//            // Get list forecast weather 5 day of city
//            List<ForecastWeatherDetailDTO> lstForecast = getListDetails5Day(cityName);
//            if(lstForecast != null) {
//                model.addAttribute("detail", lstForecast);
//                return "page_user/weather_detail";
//            }
//        } catch (Exception e) {
//            return "redirect:home-weather?message_forecast=notfound";
//        }
//        return "";
//    }
    /**
     * WEATHER DETAIL
     * 40 records for 5 continuous days => days = 40/5
     *
     * @param cityName
     * @param model
     * @return
     */
    @GetMapping("/weather-detail/{cityName}")
    private String getWeatherDetail(@PathVariable String cityName, Model model) throws ParseException {
        WeatherEntity currentWeather = weatherService.getJsonWeatherSearch(cityName);
        model.addAttribute("currentWeather", currentWeather);
        WeatherDetailDTO futureWeather = weatherService.getJsonWeatherDetail(cityName);
        List<WeatherEntity> futureWeatherList = new ArrayList<WeatherEntity>();
        for (int j = 7; j < 40; j = j + days) {
            String icon = (host_http + domain_http + "/img/w/" + futureWeather.getList().get(j).getWeather().get(0).getIcon() + tail_icon_path);
            String clouds = futureWeather.getList().get(j).getWeather().get(0).getDescription();
            String humidity = futureWeather.getList().get(j).getMain().getHumidity();
            String pressure = futureWeather.getList().get(j).getMain().getPressure();
            String wind = futureWeather.getList().get(j).getWind().getSpeed();
            Double temp = futureWeather.getList().get(j).getMain().getTemp();
            String temp_min = futureWeather.getList().get(j).getMain().getTemp_min();
            String temp_max = futureWeather.getList().get(j).getMain().getTemp_max();
            String city1 = futureWeather.getCity().getName();
            Date date = CommonUtil.stringToDate(futureWeather.getList().get(j).getDt_txt());
            WeatherEntity detail = new WeatherEntity(icon, city1, temp, clouds, wind, humidity, pressure, date);
            futureWeatherList.add(detail);
        }
        model.addAttribute("detail", futureWeatherList);
        return "page_user/weather_detail";
    }

    /**
     * ADD WEATHER
     *
     * @param cityName
     * @return
     */
    @GetMapping("/save-weather/{cityName}")
    private String saveWeather(@PathVariable String cityName) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByUsername(authUser.getName());
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
                    .sort((WeatherEntity w1, WeatherEntity w2) -> w1.getDate().compareTo(w2.getDate()));
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
    public void insertWeather(String cityName, UserEntity userEntity) {
        WeatherEntity result = weatherService.getJsonWeatherSearch(cityName);
        result.setDate(new Date());
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
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        WeatherEntity oldWeather = weatherService.filterWeatherByCity(cityName);
        WeatherEntity currentWeather = weatherService.getJsonWeatherSearch(cityName);
        //Set New Record for Current Weather
        oldWeather.setIcon(currentWeather.getIcon());
        oldWeather.setTemp(currentWeather.getTemp());
        oldWeather.setWind(currentWeather.getWind());
        oldWeather.setHumidity(currentWeather.getHumidity());
        oldWeather.setPressure(currentWeather.getPressure());
        oldWeather.setDescription(currentWeather.getDescription());
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
    @GetMapping("/delete-weather/{id}")
    private String deleteWeather(@PathVariable Long id) {
        weatherService.deleteWeather(id);
        return "redirect:/";
    }
}
