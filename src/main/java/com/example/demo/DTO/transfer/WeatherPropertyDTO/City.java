package com.example.demo.DTO.transfer.WeatherPropertyDTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class City {
    private Double id;
    private String name;
    private Coord coord;
    private String country;
    private Integer sunrise;
    private Integer sunset;
    private String population;
    private Timestamp timezone;
}
