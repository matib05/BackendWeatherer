package com.incomm.mahmad.controller;

import com.incomm.mahmad.model.response.WeatherData;
import com.incomm.mahmad.model.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
public class RoutingController {

    private static final String APPID = "31576d96eb3acb7333670187bc45f085";

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping(value = "/location")
    public String[] getLocation(@RequestParam(value = "lat")String lat, @RequestParam(value = "long")String lon) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + APPID;
        System.out.println(url);
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        String[] response = new String[3];
        response[0] = weather.getWeather().get(0).getDescription();
        response[1] = weather.getName();
        response[2] = weather.getMain().getTemp().toString();
        addToDatabase(response[0], response[1], response[2]);
        System.out.println(weather.toString());
        return response;
    }


    @GetMapping(value = "/city")
    public String[] getCityLocation(@RequestParam(value = "q")String city) {
        String[] response = checkCache(city);
        if (response == null) {
            response = apiCall(city);
        }
        return response;
    }

    public void addToDatabase(String city, String description, String temp) {
        System.out.println("IN ADDTODATABASE");
        if (city == null || description == null ||  temp == null) {
            System.out.println("ADDTODATABASE: either city, description, or temp is null");
            return;
        }
        System.out.println("ADDTODATABASE: inserting city " + city
                + ", description" + description + ", and temp" + temp + " into db");
        jdbcTemplate.update("INSERT INTO weather(city, description, temp) VALUES (?, ?, ?)", city, description, temp);
    }

    public String[] checkCache(String city) {
        System.out.println("CHECKCACHE CALL");
        String sql = "SELECT * FROM weather WHERE city = ?";
        List<WeatherResponse> data =  jdbcTemplate.query(sql, new Object[]{city}, new RowMapper<WeatherResponse>() {
            @Override
            public WeatherResponse mapRow(ResultSet rs, int i) throws SQLException {
                System.out.println("CHECKCACHE CALL querying data with following query: " + sql);
                WeatherResponse weatherResponse = new WeatherResponse();
                weatherResponse.setCity(rs.getString("city"));
                weatherResponse.setDescription(rs.getString("description"));
                weatherResponse.setTemp(rs.getString("temp"));
                System.out.println("CHECKCACHE CALL: " + weatherResponse.toString());
                return weatherResponse;
            }
        });
        if (data.isEmpty() || data == null || data.size() ==  0) {
            System.out.println("CHECKCACHE: data is empty");
            return null;
        }
        System.out.println("CHECKCACHE: data is NOT empty");
        return new String[] {data.get(0).getDescription(), data.get(0).getCity(), data.get(0).getTemp()};
    }

    public String[] apiCall(String city) {
        System.out.println("IN APICALL");
        String[] response = new String[3];
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APPID;
        System.out.println("APICALL: making restTemplate call with url " + url);
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        response[0] = weather.getWeather().get(0).getDescription();
        response[1] = weather.getName();
        response[2] = weather.getMain().getTemp().toString();
        addToDatabase(response[0], response[1], response[2]);
        return response;
    }
    /*
    @GetMapping(value = "/city")
    public String[] getCity(@RequestParam(value = "q")String city) {
        String sql = "SELECT * FROM weather WHERE city = ?";


        List<WeatherResponse> data =  jdbcTemplate.query(sql, new Object[]{city}, new RowMapper<WeatherResponse>() {
            @Override
            public WeatherResponse mapRow(ResultSet rs, int i) throws SQLException {
                WeatherResponse weatherResponse = new WeatherResponse();
                weatherResponse.setCity(rs.getString("city"));
                weatherResponse.setDescription(rs.getString("description"));
                weatherResponse.setTemp(rs.getString("temp"));
                return weatherResponse;
            }
        });
        System.out.println(data.toString());
        String[] response = new String[3];
        response[0] = data.get(0).getCity();
        response[1] = data.get(0).getDescription();
        response[2] = data.get(0).getTemp();
        System.out.println(response[0] + " "+ response[1] + " " + response[2]);
        return response;
    }
    */
}
