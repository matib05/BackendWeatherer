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

    static int i = 74071;

    private static final String APPID = "31576d96eb3acb7333670187bc45f085";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE weather IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE weather(city VARCHAR(100), description VARCHAR(100), temp VARCHAR(100))");
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
        String fileName = "C:\\Users\\mahmad\\IdeaProjects\\BackendWeatherer\\src\\main\\java\\com\\incomm\\mahmad\\city_list.txt";
        String document = null;
        try {
            document = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] line = document.split("\n");
        while (i > 73971) {
            String[] elements = line[i].split("\t");
            log.info("cityName= " + elements[1]);
            System.out.println(i--);
            return elements[1];

        }
        return "";
    }

    public void runTimerTask() {
        RestTemplate restTemplate = new RestTemplate();
        String cityName = getCityName();
        assert cityName != null;
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + APPID;
        log.info(url);
        WeatherData weather = restTemplate.getForObject(url, WeatherData.class);
        addToDatabase(weather.getWeather().get(0).getDescription(), weather.getName().trim(), weather.getMain().getTemp());
        log.info(weather.toString());
    }

    public void addToDatabase(String description, String city, Double temp) {
        if (description == null || city == null) {
            System.out.println("Null");
            return;
        }
        String weatherData = city + " " + description + " " + temp;
        log.info(weatherData);
        System.out.println(jdbcTemplate.update("INSERT INTO weather(city, description, temp) VALUES (?, ?, ?)", city, description, temp));

    }
}
