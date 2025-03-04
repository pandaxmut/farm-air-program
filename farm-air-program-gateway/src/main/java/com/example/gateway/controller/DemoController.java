package com.example.gateway.controller;

import com.example.gateway.api.UserControllerApi;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoController {
    @Resource
    private UserControllerApi userControllerApi;

    @RequestMapping("hello")
    public String hello()
    {
        return "hello";
    }
}
