package com.cs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdvertisementManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvertisementManagementSystemApplication.class, args);
    }
}
