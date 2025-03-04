package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public PaginationInnerInterceptor  paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }
}
