package com.example.demo.DTO.transfer;

import com.example.demo.DTO.transfer.WeatherPropertyDTO.City;
import com.example.demo.DTO.transfer.WeatherPropertyDTO.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDetailDTO {
    private Integer cod;
    private String message;
    private Integer cnt;
    private java.util.List<List> list;
    private City city;

}
