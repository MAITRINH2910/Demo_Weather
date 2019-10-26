package com.example.demo.DTO.transfer.WeatherPropertyDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class List {
    private Long dt;
    private MainDetail main;
    private java.util.List<Weather> weather = null;
    private Clouds clouds;
    private Wind wind;
    private RainDetail rain;
    private SysDetail sys;
    private String dt_txt;
}
