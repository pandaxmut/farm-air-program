package com.example.common.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(RestHighLevelClient.class)
public class ElasticsearchConfig {

    @Value("${elasticsearch.host:127.0.0.1}")
    private String host;

    @Value("${elasticsearch.port:9200}")
    private int port;

    @Value("${elasticsearch.scheme:http}")
    private String scheme;

    @Bean
    @ConditionalOnMissingBean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(host, port, scheme)
        );
        return new RestHighLevelClient(builder);
    }
}
