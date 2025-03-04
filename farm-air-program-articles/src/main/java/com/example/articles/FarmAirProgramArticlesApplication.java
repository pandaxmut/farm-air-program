package com.example.articles;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.articles","com.example.common"})
@EnableFeignClients
@MapperScan("com.example.articles.mapper")  // 确保扫描路径正确
public class FarmAirProgramArticlesApplication {
    public static void main(String[] args) {
        SpringApplication.run(FarmAirProgramArticlesApplication.class, args);
    }

}
