package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.example.api","com.example.common"})
public class FarmAirProgramApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmAirProgramApiApplication.class, args);
    }
}
