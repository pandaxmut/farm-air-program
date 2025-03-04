package com.example.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway/token")
public class TokenAuthController {
    //刷新
    @GetMapping("/refresh")
    public String refresh() {
        return "refresh";
    }
}
