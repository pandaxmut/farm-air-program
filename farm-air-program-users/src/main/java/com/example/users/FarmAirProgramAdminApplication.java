package com.example.users;

import com.example.api.intercept.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.example.users","com.example.common"})
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.example.api.controller"},defaultConfiguration = DefaultFeignConfig.class)
public class FarmAirProgramAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmAirProgramAdminApplication.class, args);
    }

}
