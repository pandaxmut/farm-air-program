package com.example.articles.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.articles.entity.Articles;
import com.example.articles.entity.ArticlesColumns;
import com.example.articles.entity.ArticlesColumnsReq;
import com.example.articles.service.IArticlesColumnsService;
import com.example.common.utils.CommonResult;
import com.example.common.utils.UserContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/articlesColumns")
@Slf4j
public class ArticlesColumnsController {

    @Resource
    private IArticlesColumnsService articlesColumnsService;
    //创建专栏
    @PostMapping()
    public CommonResult<Boolean> createColumn(@RequestBody ArticlesColumnsReq articlesColumns){
        log.info("所有文章：",articlesColumns);
        //获取用户id
        String userId = UserContext.getUserId();
        articlesColumns.setUserId(userId);
        //添加专栏id
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);//这里表示，在数据中心1中的第一台机器；
        articlesColumns.setId(snowflake.nextIdStr());
        articlesColumns.setStatus(1);
        ArticlesColumns articlesColumns1 = new ArticlesColumns();
        BeanUtil.copyProperties(articlesColumns,articlesColumns1);
        articlesColumnsService.save(articlesColumns1);
        log.info(articlesColumns.getId());
        boolean b = articlesColumnsService.saveArticleColumnsShare(articlesColumns.getId(), articlesColumns.getArticles());
        return CommonResult.success(b);
    }
    //获取某用户创建的专栏
    @GetMapping()
    public CommonResult<List<ArticlesColumns>> getColumnByUserId(){
        String userId = UserContext.getUserId();
        List<ArticlesColumns> columnsByUserId = articlesColumnsService.getColumnsByUserId(userId);
        return CommonResult.success(columnsByUserId);
    }

    //获取文章列表 （ 关键词检索 + status状态 + 按时间排序：由近及远）
    @GetMapping("/search")
    public CommonResult<List<ArticlesColumns>> getColumnsSearch(@RequestParam("columnsName") String columnsName,
                                                          @RequestParam("status") Integer status) {
        log.info("getColumnsSearch");
        log.info("columnsName:{}", columnsName);
        log.info("status:{}", status);
        //1.获取文章信息列表
        LambdaQueryWrapper<ArticlesColumns> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ArticlesColumns::getTitle, columnsName);
        wrapper.eq(ArticlesColumns::getStatus, status);
        wrapper.orderByDesc(ArticlesColumns::getPostTime);
        return CommonResult.success(articlesColumnsService.list(wrapper));
    }


    @GetMapping("/searchByCategory")
    public CommonResult<List<ArticlesColumns>> getColumnsSearchByCategory(@RequestParam("categoryId") Integer categoryId,
                                                                          @RequestParam("status") Integer status) {
        log.info("getColumnsSearchByCategory");
        log.info("categoryId:{}", categoryId);
        log.info("status:{}", status);
        //1.获取文章信息列表
        return CommonResult.success(articlesColumnsService.getColumnsByCategoryId(categoryId, status));
    }

    @GetMapping("searchByColumns")
    public CommonResult<ArticlesColumnsReq> getColumnsSearchByColumns(@RequestParam("columnsId") String columnsId,
                                                                            @RequestParam("status") Integer status) {
        log.info("getColumnsSearchByColumns");
        log.info("columnsId:{}", columnsId);
        //1.获取专栏信息
        LambdaQueryWrapper<ArticlesColumns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlesColumns::getId, columnsId);
        wrapper.eq(ArticlesColumns::getStatus, status);
        ArticlesColumns articlesColumns = articlesColumnsService.getOne(wrapper);
        //2.获取文章信息列表
        List<Articles> articlesList = articlesColumnsService.getArticleSearchByColumnsId(columnsId);
        ArticlesColumnsReq articlesColumnsReq = new ArticlesColumnsReq();
        BeanUtil.copyProperties(articlesColumns, articlesColumnsReq);
        articlesColumnsReq.setArticles(articlesList);
        log.info(articlesColumnsReq.toString());
        return CommonResult.success(articlesColumnsReq);
    }
}
