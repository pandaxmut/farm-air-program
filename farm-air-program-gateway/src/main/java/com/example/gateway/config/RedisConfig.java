package com.example.gateway.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching   //开启缓存功能，作用于缓存配置类上或者作用于springboot启动类上
@Configuration
public class RedisConfig {

    /**
     * 1.创建redisTemplate实例，用来操作redis数据库
     * 2.序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //加载连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //TODO 可优化点 （自定义序列化器和jackson2） 我们这里没有引用jackson2 的jar包，所以这里使用jdk的序列化器
        //设置key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置value的序列化器
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        //设置hash的key序列化器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //设置hash的value序列化器
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());


        return redisTemplate;

    }
}
