package com.incomm.mahmad.controller;

import com.incomm.mahmad.model.request.Location;
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
    public Location getLocation(@RequestParam(value = "lat")String lat, @RequestParam(value = "long")String lon) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + APPID;
        System.out.println(url);
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        //addToDatabase(weather.getWeather().get(0).getDescription(), weather.getName().toString().trim(), weather.getMain().getTemp());
        System.out.println(weather.toString());
        return null;
    }

    /*
    @GetMapping(value = "/city")
    public Location getCity(@RequestParam(value = "q")String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APPID;
        System.out.println(url);
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        //addToDatabase(weather.getWeather().get(0).getDescription(), weather.getName().toString().trim(), weather.getMain().getTemp());
        System.out.println(weather.toString());
        return null;
    }
    */

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
}
