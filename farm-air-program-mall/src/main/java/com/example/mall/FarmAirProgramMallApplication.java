package com.example.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@ComponentScan(basePackages = {"com.example.mall","com.example.common"})
@SpringBootApplication
public class FarmAirProgramMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmAirProgramMallApplication.class, args);
    }

}
