package com.incomm.mahmad.service;

import com.incomm.mahmad.model.request.Location;
import com.incomm.mahmad.model.response.Weather;
import com.incomm.mahmad.model.response.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationDaoImp extends JdbcDaoSupport implements LocationDao {
    int id  = 0;

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void addWeatherByCity(Weather weather) {
        String sql = "INSERT INTO location " +
                "(coord, weather, main, wind, clouds, dt, sys, id, name, cod) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                WeatherData weather = new WeatherData();
                ps.setString(1, weather.getCoord().toString());
                ps.setString(2, weather.getWeather().toString());
                ps.setString(4, weather.getMain().toString());
                ps.setString(5, weather.getClouds().toString());
                ps.setString(6, weather.getDt().toString());
                ps.setString(7, weather.getSys().toString());
                ps.setString(8, weather.getId().toString());
                ps.setString(9, weather.getName());
                ps.setString(10, weather.getCod().toString());
            }

            @Override
            public int getBatchSize() {
                return 0;
            }
        });
    }

    @Override
    public WeatherData getWeather() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }
}
