package com.incomm.mahmad.service;

import com.incomm.mahmad.model.request.Location;
import com.incomm.mahmad.model.response.Weather;
import com.incomm.mahmad.model.response.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "locations")
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationDao locationDao;


    @Override
    public void addWeatherByCity(Weather weather) {
        locationDao.addWeatherByCity(weather);
    }

    @Override
    public WeatherData getWeather() {
        return locationDao.getWeather();
    }

    @Override
    public Location getLocation() {
        Location location = locationDao.getLocation();
        System.out.println(location);
        return location;
    }
}
