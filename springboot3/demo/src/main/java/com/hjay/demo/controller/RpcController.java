package com.hjay.demo.controller;


import com.hjay.demo.service.WeatherInterface;
import com.hjay.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpc")
public class RpcController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public String weather(@RequestParam("city") String city){
        return weatherService.weather(city);
    }


    @GetMapping("/get")
    public String getWeather(@RequestParam("city") String city){
        return weatherService.getWeather(city);
    }

    @GetMapping("/proxy")
    public String getWeatherProxy(@RequestParam("city") String city){
        return weatherService.getWeatherInterface(city);
    }
}
