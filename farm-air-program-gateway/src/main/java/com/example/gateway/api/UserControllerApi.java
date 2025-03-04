package com.example.gateway.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="admin-service")
public interface  UserControllerApi {
    @RequestMapping(value = "/admin/user",method = RequestMethod.GET)
    String name();
}
