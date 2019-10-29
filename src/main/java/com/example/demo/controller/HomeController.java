package com.example.demo.controller;

import com.example.demo.DTO.transfer.WeatherDTO;
import com.example.demo.service.UserService;
import com.example.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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
     * Show Local Weather
     * @param lat
     * @param lon
     * @return
     */
    @GetMapping("/local-weather")
    @ResponseBody
    public WeatherDTO forecastCurrentWeather(@RequestParam String lat, @RequestParam String lon) {
        return weatherService.getJsonLocalWeather(lat, lon);
    }
}

