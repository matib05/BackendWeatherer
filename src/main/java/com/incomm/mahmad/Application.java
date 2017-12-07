package com.incomm.mahmad;

import com.incomm.mahmad.model.response.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private static final Logger log  = LoggerFactory.getLogger(Application.class);
    static String city = "Atlanta";
    private static final String APPID = "31576d96eb3acb7333670187bc45f085";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE weather IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE weather(weatherData VARCHAR(700))");
        log.info("calling api");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                log.info("run");
                runTimerTask();
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 5*1000, 5*1000);
    }


    public static String getCityName() {
        log.info("here in getCityName");
        String fileName = "C:\\Users\\matib05\\Desktop\\Coding\\BackendWeatherer\\src\\main\\java\\com\\incomm\\mahmad\\city_list.txt";
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("splitting content");
        String[] cont = content.split("\n");
        for (int i = 0 ; i < cont.length; i++) {
            log.info(cont[i]);
            if (cont[i].equalsIgnoreCase(city.toLowerCase())) {
                log.info("cityName= " + cont[i]);
                return cont[i];
            }
        }
        return "";
    }

    public static void runTimerTask() {
        RestTemplate restTemplate = new RestTemplate();
        log.info("calling getCityName");
        String cityName = getCityName();
        if (cityName =="") {
            log.info("it was \"\"");
            cityName = "Atlanta";
        }
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + APPID;
        log.info(url);
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        //addToDatabase(weather.getWeather().get(0).getDescription(), weather.getName().trim(), weather.getMain().getTemp());
        System.out.println(weather.toString());
    }

    public static void addToDatabase(String description, String city, Double temp) {
        if (description == null || city == null) {
            System.out.println("Null");
            return;
        }
        String weatherData = description + " " + city + " " + temp;
        //System.out.println(jdbcTemplate.update("INSERT INTO weather(weatherData) VALUES (?)", weatherData));
    }
}
