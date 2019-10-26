package com.example.demo.DTO.transfer.WeatherPropertyDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class MainDetail {

    private Double temp;

    private String humidity;

    private String pressure;

    private Double temp_min;

    private Double temp_max;

    private String tempMax;

    private String temp_kf;

    private String sea_level;

    private String grnd_level;
}


