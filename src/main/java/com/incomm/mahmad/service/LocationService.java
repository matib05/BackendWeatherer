package com.incomm.mahmad.service;

import com.incomm.mahmad.model.request.Location;
import com.incomm.mahmad.model.response.Weather;
import com.incomm.mahmad.model.response.WeatherData;

public interface LocationService {
    void addWeatherByCity(Weather weather);
    WeatherData getWeather();
    Location getLocation();
}
