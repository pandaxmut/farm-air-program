package com.example.common.config;

import com.example.common.intercept.UserInfoInterceptHandler;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptHandler());
    }

    //过滤器
    public void addFilter(FilterRegistrationBean filterRegistrationBean){
        filterRegistrationBean.addUrlPatterns("/*");
    }
}
