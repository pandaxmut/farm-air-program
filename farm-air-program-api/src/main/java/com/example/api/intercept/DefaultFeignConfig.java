package com.example.api.intercept;

import com.example.common.utils.UserContext;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public RequestInterceptor userInfoInterceptor(){
        System.out.println("DefaultFeignConfig设置header userinfo");
        return requestTemplate -> requestTemplate.header("userinfo", UserContext.getUserId());
    }
}