package com.incomm.mahmad.controller;

import com.incomm.mahmad.model.request.Location;
import com.incomm.mahmad.model.response.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RoutingController {

    private static final String APPID = "31576d96eb3acb7333670187bc45f085";

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping(value = "/location")
    public Location getLocation(@RequestParam(value = "lat")String lat, @RequestParam(value = "long")String lon) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + APPID;
        System.out.println(url);
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        addToDatabase(weather.getWeather().get(0).getDescription(), weather.getName().toString().trim(), weather.getMain().getTemp());
        System.out.println(weather.toString());
        return null;
    }

    @GetMapping(value = "/city")
    public Location getCity(@RequestParam(value = "q")String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APPID;
        System.out.println(url);
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        addToDatabase(weather.getWeather().get(0).getDescription(), weather.getName().toString().trim(), weather.getMain().getTemp());
        System.out.println(weather.toString());
        return null;
    }

    public void addToDatabase(String description, String city, Double temp) {
        if (description == null || city == null) {
            System.out.println("Null");
            return;
        }
        String weatherData = description + " " + city + " " + temp;
        System.out.println(jdbcTemplate.update("INSERT INTO weather(weatherData) VALUES (?)", weatherData));
    }
}
