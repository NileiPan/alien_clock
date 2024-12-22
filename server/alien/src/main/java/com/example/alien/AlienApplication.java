package com.example.alien;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AlienApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlienApplication.class, args);
    }

}
