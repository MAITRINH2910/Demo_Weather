package com.example.demo.controller;

import com.example.demo.DTO.transfer.WeatherDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WeatherEntity;
import com.example.demo.service.UserService;
import com.example.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Class HomeController is used to illustrate:
 * Weather Local Information
 * Weather Search Information
 * History Weather
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    public UserService userService;

    @Autowired
    public WeatherController weatherController;

    @Autowired
    public WeatherService weatherService;

    /**
     * History Weather
     * @param model
     * @return
     */
    @GetMapping
    public String homePage(Model model) {
        UserEntity user = userService.getAuthUser();
        List<WeatherEntity> listCity = weatherService.getCitiesByUser(user);
        model.addAttribute("listCities", listCity);
        List<List<WeatherEntity>> weatherGroupByCity = weatherService.weatherGroupByCity(user);
        model.addAttribute("weatherList0", weatherGroupByCity);
        return "page_user/weather_search";
    }

    /**
     * Show Local Weather
     * @param lat
     * @param lon
     * @return
     */
    @GetMapping("/local-weather")
    @ResponseBody
    public WeatherDTO forecastCurrentWeather(@RequestParam String lat, @RequestParam String lon) {
        return weatherService.getCurrentLocalWeather(lat, lon);
    }
}

