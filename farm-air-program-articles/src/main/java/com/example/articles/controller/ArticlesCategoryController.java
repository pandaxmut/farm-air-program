package com.example.articles.controller;

import com.example.articles.entity.ArticlesCategory;
import com.example.articles.service.IArticlesCategoryService;
import com.example.common.utils.CommonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章分类 前端控制器
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@RestController
@RequestMapping("/articlesCategory")
public class ArticlesCategoryController {
    @Resource
    private IArticlesCategoryService articlesCategoryService;

    @GetMapping("/all")
    public CommonResult<Map<Integer, String>> getAll() {
        List<ArticlesCategory> list = articlesCategoryService.list();
        Map<Integer, String> map =list.stream().collect(Collectors.toMap(ArticlesCategory::getId, ArticlesCategory::getCategoryName));
        for (Map.Entry entry : map.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
        return CommonResult.success(map);
    }

    @GetMapping("hello")
    public String hello() {

        return "hello";
    }


}
