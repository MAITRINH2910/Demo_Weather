package com.example.demo.DTO.transfer.WeatherPropertyDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class ListDetail {
    private Instant dt_txt;
    private Main main;
    private java.util.List<Weather> weather;
    private Clouds clouds;
    private Wind wind;

    @JsonProperty("dt")
    public Instant  getDt_txt() {
        return dt_txt;
    }
}
