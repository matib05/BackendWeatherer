package com.incomm.mahmad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Timer;

@SpringBootApplication
public class Application implements CommandLineRunner{

    private static final Logger log  = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Timer timer = new Timer();
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE weather IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE weather("
                + "weatherData VARCHAR(700))");
    }
}
