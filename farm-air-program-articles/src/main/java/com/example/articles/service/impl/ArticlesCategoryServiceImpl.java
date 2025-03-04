package com.example.articles.service.impl;

import com.example.articles.entity.ArticlesCategory;
import com.example.articles.mapper.ArticlesCategoryMapper;
import com.example.articles.service.IArticlesCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class ArticlesCategoryServiceImpl extends ServiceImpl<ArticlesCategoryMapper, ArticlesCategory> implements IArticlesCategoryService {

}
