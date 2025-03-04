package com.example.files.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="users-service")
public interface UserControllerApi {
    @RequestMapping(value = "/users/users",method = RequestMethod.GET)
    String name();
}
