package com.incomm.mahmad.service;

import com.incomm.mahmad.model.response.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TimerTask;

public class ApiCaller extends TimerTask {


    String city = "Atlanta";

    private static final String APPID = "31576d96eb3acb7333670187bc45f085";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getCityId() {
        String cityId = "";
        String fileName = "city_list.txt";
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] cont = content.split("\n");
        for (int i = 0 ; i < cont.length; i++) {
            if (cont[i].equalsIgnoreCase(city.toLowerCase())) {
                cityId= cont[i].split(",")[0];
            }
        }
        return cityId.trim();
    }


    @Override
    public void run() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/weather?id=" + getCityId() + "&appid=" + APPID;
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        addToDatabase(weather.getWeather().get(0).getDescription(), weather.getName().toString().trim(), weather.getMain().getTemp());
        System.out.println(weather.toString());
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
