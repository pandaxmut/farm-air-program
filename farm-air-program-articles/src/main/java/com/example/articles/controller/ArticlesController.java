package com.example.articles.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.articles.dto.ArticlesEsDTO;
import com.example.articles.entity.Articles;
import com.example.articles.entity.ArticlesLike;
import com.example.articles.entity.ArticlesTags;
import com.example.articles.service.ArticlesSearchService;
import com.example.articles.service.IArticlesLikeService;
import com.example.articles.service.IArticlesService;
import com.example.articles.service.IArticlesTagsService;
import com.example.articles.vo.ArticlesReqVO;
import com.example.articles.vo.ArticlesSearchRespVO;
import com.example.common.utils.CommonResult;
import com.example.common.utils.UserContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 文章信息表 前端控制器
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@RestController
@RequestMapping("/articles")
@Slf4j
public class ArticlesController {
    @Resource
    private IArticlesService articlesService;
    @Resource
    private IArticlesTagsService articlesTagsService;
    @Resource
    private IArticlesLikeService articlesLikeService;
    @Resource
    private ArticlesSearchService articlesSearchService;

    //创建文章
    @Transactional
    @PostMapping()
    public CommonResult<Articles> createArticles(@RequestBody ArticlesReqVO articlesReqVO) {
        log.info("createArticles");
        log.info("getuserinfo:{}",articlesReqVO);
        articlesReqVO.setUserId(UserContext.getUserId());
        log.info("tags:{}",articlesReqVO.getTags().size());
        //保存信息z
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);//这里表示，在数据中心1中的第一台机器；
        articlesReqVO.setId(snowflake.nextIdStr());

        Articles articles = new Articles();
        BeanUtil.copyProperties(articlesReqVO, articles);

        log.info("articles:{}", articles);
        articlesService.save(articles);
        //判断是否新增标签，新增则保存 TODO:将遍历插入数据信息进行优化？一次性插入？
        if (articlesReqVO.getTags() != null && articlesReqVO.getTags().size() > 0) {
            for (String tag : articlesReqVO.getTags()) {
                ArticlesTags articlesTags = new ArticlesTags();
                articlesTags.setArticleId(articles.getId());
                articlesTags.setTagName(tag);
                articlesTagsService.save(articlesTags);
            }
        }
        //es 保存
        ArticlesEsDTO articlesEsDTO = new ArticlesEsDTO();
        BeanUtil.copyProperties(articlesReqVO,articlesEsDTO);
        try {
            articlesService.saveOrUpdateArticleToEs(articlesEsDTO);
            log.info("es数据保存成功！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return CommonResult.success(articlesReqVO);
    }

    //获取用户文章列表
    @GetMapping()
    public CommonResult<List<ArticlesReqVO>> getUserArticles() throws IOException {
        log.info("getUserArticles");
        log.info("getuserinfo:{}", UserContext.getUserId());
        //1.获取文章信息列表
        LambdaQueryWrapper<Articles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Articles::getUserId, UserContext.getUserId());
        List<Articles> list = articlesService.list(wrapper);
        List<ArticlesReqVO> targetList = list.stream().map(item -> {
            ArticlesReqVO articlesReqVO = new ArticlesReqVO();
            BeanUtil.copyProperties(item, articlesReqVO);
            return articlesReqVO;
        }).toList();
        //2.关联tags标签内容
        List<ArticlesTags> ArticlesTagsList = articlesTagsService.list();
        targetList.forEach(item->{item.setTags(ArticlesTagsList.stream().filter(
                tag-> tag.getArticleId().equals(item.getId()))
                        .map(ArticlesTags::getTagName)
                        .toList());});
        //3.关联作者信息点赞状态
        targetList.forEach(item->{
            item.setGood(articlesLikeService.getOne(
                    new LambdaQueryWrapper<ArticlesLike>()
                            .eq(ArticlesLike::getArticleId, item.getId())
                            .eq(ArticlesLike::getUserId, UserContext.getUserId())
                            .eq(ArticlesLike::getStatus, 1)) != null);
        });

        log.info("完整的文章信息列表：targetList:{}", targetList);
        return CommonResult.success(targetList);
//        return articlesSearchService.searchArticles("redis", 1, 10);
    }

    //获取指定文章
    @GetMapping("/{id}")
    public CommonResult<ArticlesReqVO> getArticlesById(@PathVariable("id") String id) {
        log.info("getArticlesById");
        Articles articles = articlesService.getArticlesById(id);
        //获取标签
        List<ArticlesTags> articlesTags = articlesTagsService.list(new LambdaQueryWrapper<ArticlesTags>().eq(ArticlesTags::getArticleId, id));
        ArticlesReqVO articlesReqVO = new ArticlesReqVO();
        BeanUtil.copyProperties(articles, articlesReqVO);
        articlesReqVO.setTags(articlesTags.stream().map(ArticlesTags::getTagName).toList());
        //获取用户对文章的状态
        articlesReqVO.setGood(articlesLikeService.getOne(
                new LambdaQueryWrapper<ArticlesLike>()
                        .eq(ArticlesLike::getArticleId, id)
                        .eq(ArticlesLike::getUserId, UserContext.getUserId())
                        .eq(ArticlesLike::getStatus, 1)) != null);

        return CommonResult.success(articlesReqVO);
    }

    @GetMapping("/page")
    public CommonResult<List<Articles>> getArticlesPage(@RequestParam("pageNo") Integer pageNo,
                                                        @RequestParam("pageSize") Integer pageSize) {
        IPage<Articles> page = new Page<>(pageNo, pageSize);
        IPage<Articles> pages = articlesService.page(page, new LambdaQueryWrapper<Articles>().eq(Articles::getStatus, 1));
        List<Articles> records = pages.getRecords();
        log.info("records:{}", records.size());
        return CommonResult.success(records);
    }

    //获取文章列表 （ 关键词检索 + status状态 + 按时间排序：由近及远）
    @GetMapping("/search")
    public CommonResult<List<Articles>> getArticlesSearch(@RequestParam("articleName") String articleName,
                                                          @RequestParam("status") Integer status) {
        log.info("getArticlesSearch");
        log.info("articleName:{}", articleName);
        log.info("status:{}", status);
        //1.获取文章信息列表
        LambdaQueryWrapper<Articles> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Articles::getTitle, articleName);
        wrapper.eq(Articles::getStatus, status);
        wrapper.orderByDesc(Articles::getPostTime);
        List<Articles> list = articlesService.list(wrapper);

        return CommonResult.success(list);
    }



    //es搜索
    @GetMapping("/search/es")
    public CommonResult<List<ArticlesSearchRespVO>>  search(@RequestParam("keyword") String keyword,
                                                            @RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size) throws IOException {
        CommonResult<List<ArticlesSearchRespVO>> listCommonResult = articlesSearchService.searchArticles(keyword, page, size);
        return listCommonResult;

    }
}
