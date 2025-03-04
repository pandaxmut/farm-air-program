package com.example.api.controller;


import com.example.api.entity.Articles;
import com.example.common.utils.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name="articles-service")
public interface ArticlesApiController {
    @RequestMapping(value = "/articles",method = RequestMethod.GET)
    CommonResult<List<Articles>> getUserArticles();
}
