package com.collabcampus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CollabCampusApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollabCampusApplication.class, args);
    }
}
