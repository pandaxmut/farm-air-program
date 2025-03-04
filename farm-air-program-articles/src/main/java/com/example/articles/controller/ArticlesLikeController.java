package com.example.articles.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.articles.entity.Articles;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesLike;
import com.example.articles.service.IArticlesCategoryService;
import com.example.articles.service.IArticlesLikeService;
import com.example.articles.service.IArticlesService;
import com.example.articles.vo.ArticlesReqVO;
import com.example.common.utils.CommonResult;
import com.example.common.utils.UserContext;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章点赞表 前端控制器
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@RestController
@RequestMapping("/articlesLike")
public class ArticlesLikeController {

    @Resource
    private IArticlesLikeService articlesLikeService;
    @Resource
    private IArticlesService articlesService;

    //TODO 点赞 ——> 判断status = 0 不感兴趣，1是点赞，2是取消点赞，
    /**
     * 设计逻辑：redis + mysql ：
     * 在redis中保存用户的点赞信息，在mysql中保存点赞总数，并且同步redis的点赞信息到mysql的article_like表中
     * 需求：避免重复点赞，唯一性，考虑性能
     */
    @PostMapping()
    public CommonResult<String> createArticlesLike(@RequestBody ArticlesReqVO articlesReqVO) {
        //1. 判断数据库中是否有记录存在并且status = 0
        int status = 0;
        if (articlesReqVO.isGood() == true) status= 1;

        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<ArticlesLike>()
                .eq(ArticlesLike::getUserId, UserContext.getUserId())
                .eq(ArticlesLike::getArticleId, articlesReqVO.getId());

        ArticlesLike articlesLike = articlesLikeService.getOne(queryWrapper);
        if (articlesLike == null) {
            //2.1 如果不存在，则新增点赞状态
            articlesLikeService.save(new ArticlesLike(UserContext.getUserId(), articlesReqVO.getId(), status));
            //文章点赞数减一
            articlesService.update(
                    new LambdaUpdateWrapper<Articles>()
                            .eq(Articles::getId, articlesReqVO.getId())
                            .setSql("good_count = good_count + 1")

            );
            return CommonResult.success();
        }
        //修改记录
        articlesLikeService.update(
                new LambdaUpdateWrapper<ArticlesLike>()
                        .eq(ArticlesLike::getUserId, UserContext.getUserId())
                        .eq(ArticlesLike::getArticleId, articlesReqVO.getId())
                        .set(ArticlesLike::getStatus, status)
        );
        if(status == 0){
            //文章点赞数减一
            articlesService.update(
                    new LambdaUpdateWrapper<Articles>()
                            .eq(Articles::getId, articlesReqVO.getId())
                            .gt(Articles::getGoodCount, 0) // 确保good_count大于0
                            .setSql("good_count = good_count - 1")

            );
        } else if (status == 1){
            //文章点赞数加一
            articlesService.update(
                    new LambdaUpdateWrapper<Articles>()
                            .eq(Articles::getId, articlesReqVO.getId())
                            .setSql("good_count = good_count + 1")

            );
        }
        //2. 存在，则更新点赞状态，不存在，则新增点赞记录
        return CommonResult.success();
    }



}
