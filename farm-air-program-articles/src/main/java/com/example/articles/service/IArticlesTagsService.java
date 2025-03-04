package com.example.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesTags;
import com.example.articles.mapper.ArticlesTagsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章分类 服务类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
public interface IArticlesTagsService extends IService<ArticlesTags> {

}
