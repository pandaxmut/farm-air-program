package com.example.articles.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesComments;
import com.example.articles.service.IArticlesCategoryService;
import com.example.articles.service.IArticlesCommentsService;
import com.example.articles.vo.ArticlesCommentsReqVO;
import com.example.common.utils.CommonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章评论 前端控制器
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@RestController
@RequestMapping("/articlesComments")
public class ArticlesCommentsController {
    @Resource
    private IArticlesCommentsService articlesCommentsService;

    @PostMapping()
    public CommonResult<String> save(@RequestBody ArticlesCommentsReqVO articlesCommentsReqVO) {
        System.out.println(articlesCommentsReqVO.getCommentText());
        System.out.println(articlesCommentsReqVO.getArticleId());
        ArticlesComments articlesComments = new ArticlesComments();
        BeanUtil.copyProperties(articlesCommentsReqVO, articlesComments);
        //设置雪花ID
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);//这里表示，在数据中心1中的第一台机器；
        articlesComments.setId(snowflake.nextIdStr());

        articlesCommentsService.save(articlesComments);
        return CommonResult.success("ok");
    }

    @GetMapping("/{articleId}")
    public CommonResult<List<ArticlesCommentsReqVO>> getArticlesCommentsById(@PathVariable("articleId") String articleId) {
        return CommonResult.success(articlesCommentsService.getArticlesByArticleId(articleId));

    }


}
