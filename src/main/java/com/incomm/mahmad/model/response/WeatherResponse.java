package com.incomm.mahmad.model.response;

public class WeatherResponse {
    private String temp;
    private String city;
    private String description;

    public WeatherResponse() {
    }

    public WeatherResponse(String temp, String city, String description) {
        this.temp = temp;
        this.city = city;
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "temp='" + temp + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
