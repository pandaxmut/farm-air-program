package com.example.gateway.filters;


import com.example.gateway.config.AuthProperties;
import com.example.gateway.config.JwtConfig;
import com.example.gateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

/**
 * 请求权限校验token
 * 放行策略：没有token不放行，有token都放行
 * 刷新token 设置放行策略就行
 */
@Component
@Slf4j
public class MyGlobalFilters implements GlobalFilter, Ordered {

    @Resource
    private JwtConfig jwtConfig;
    @Resource
    private AuthProperties authProperties;
    @Resource
    private AntPathMatcher antPathMatcher;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        //1.可排除路径
        String path = request.getURI().getPath();
        System.out.println("当前请求的路径:"+path);
        if (isExcludePath(path)) {
            return chain.filter(exchange);
        }
        //2.判断用户是否有权限
        List<String> authorization = request.getHeaders().get("authorization");
        log.info("判断用户是否有权限");
        if (authorization==null || authorization.isEmpty()){
            log.info("没有权限，请去登录");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //2. 有权限， 获取token
        String token = authorization.get(0).replace("Bearer ", "");
        //3.解析token，判断是否有效（有效、过期、错误token）
        String userId = null;
        try {
            Claims claims = JwtUtils.parseJwt(token, jwtConfig.getKey());
            Date expiration = claims.getExpiration();
            System.out.println("过期时间:"+expiration);

            //获取用户id
            userId = (String)claims.get("userId");
            log.info("userid:{}",userId);
            System.out.println("token:"+token);

            //4.传递用户信息 + 注意：原来的authorization也会被保存并发送
            String userInfo = userId;
            ServerWebExchange swe = exchange.mutate()
                    .request(builder -> builder.header("userinfo", userInfo))
                    .build();
            return chain.filter(swe);
        }catch (ExpiredJwtException e){
            //token过期重新登录
            log.info("token过期");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            DataBufferFactory bufferFactory = response.bufferFactory();
            DataBuffer dataBuffer = bufferFactory.wrap("{\"code\":402,\"message\":\"token过期\",\"data\":null}".getBytes());
            return response.writeWith(Mono.just(dataBuffer)).doOnError(error -> DataBufferUtils.release(dataBuffer));
        }catch (Exception e){
            //直接返回错误码
            log.info("token错误");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            DataBufferFactory bufferFactory = response.bufferFactory();
            DataBuffer dataBuffer = bufferFactory.wrap("{\"code\":401,\"message\":\"token错误\",\"data\":null}".getBytes());
            return response.writeWith(Mono.just(dataBuffer)).doOnError(error -> DataBufferUtils.release(dataBuffer));
        }
    }

    private boolean isExcludePath(String path) {
        for(String pathPattern : authProperties.getExcludePaths()){
            if(antPathMatcher.match(pathPattern,path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
