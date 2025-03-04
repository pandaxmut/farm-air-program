package com.example.articles.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesTags;
import com.example.articles.mapper.ArticlesCategoryMapper;
import com.example.articles.mapper.ArticlesTagsMapper;
import com.example.articles.service.IArticlesCategoryService;
import com.example.articles.service.IArticlesTagsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章分类 服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Service
public class ArticlesTagsServiceImpl extends ServiceImpl<ArticlesTagsMapper, ArticlesTags> implements IArticlesTagsService {

}
