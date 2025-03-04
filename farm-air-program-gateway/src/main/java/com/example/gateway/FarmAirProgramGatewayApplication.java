package com.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@CrossOrigin
@EnableFeignClients
@SpringBootApplication
public class FarmAirProgramGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmAirProgramGatewayApplication.class, args);
    }

    @Configuration
    public static class RestTemplateConfiguration{
        @Bean
        public RestTemplate restTemplate(){
            return new RestTemplate();
        }
    }


    @RestController
    static class  TestController{
        @Autowired
        private DiscoveryClient discoveryClient;

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private LoadBalancerClient loadBalancerClient;

        @RequestMapping(path= "/hello")
        public String hello(){
            // <1> 获得服务的一个实例
            ServiceInstance instance;
            //获取服务列表
            if (true){
                //获取 my_erp_admin 对应的实例列表
                List<ServiceInstance> instances =  discoveryClient.getInstances("admin-service");
                //负载均衡 选择第一个
                instance = instances.size() > 0 ? instances.get(0) : null;
            }else{
                instance = loadBalancerClient.choose("admin-service");
            }
            // <2> 发送调用
            if (instance == null){
                throw new IllegalStateException("没有可用的实例");
            }
            String uri = instance.getUri().toString();
            System.out.println("uri:"+uri);
            String result = restTemplate.getForObject(uri + "/admin/user", String.class);
            return result;
        }
    }

}
