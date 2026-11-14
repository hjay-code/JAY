package com.hjay.demo.service;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

/**
 * httpservice 代理模式
 */
public interface WeatherInterface {

    /**
     * 转发get请求
     * @return
     */
    @GetExchange(url = "/simpleWeather/query", accept = MediaType.APPLICATION_JSON_VALUE)
    String getWeather(@RequestParam("city")  String city,
                      @RequestParam("key") String apiKey);

    @GetExchange(url = "https://apis.juhe.cn/simpleWeather/query", accept = MediaType.APPLICATION_JSON_VALUE)
    String getWeatherProxy(@RequestParam("city")  String city,
                      @RequestParam("key") String apiKey);
}
