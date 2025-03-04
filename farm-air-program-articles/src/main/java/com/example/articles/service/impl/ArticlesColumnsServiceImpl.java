package com.example.articles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.articles.entity.Articles;
import com.example.articles.entity.ArticlesColumns;
import com.example.articles.mapper.ArticlesColumnsMapper;
import com.example.articles.service.IArticlesColumnsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文章专栏 服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Service
public class ArticlesColumnsServiceImpl extends ServiceImpl<ArticlesColumnsMapper, ArticlesColumns> implements IArticlesColumnsService {


    public boolean saveArticleColumnsShare(String columnsId, List<Articles> articles) {
        return baseMapper.saveArticleColumnsShare(columnsId, articles);
    }

    @Override
    public List<ArticlesColumns> getColumnsByUserId(String userId) {
        LambdaQueryWrapper<ArticlesColumns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlesColumns::getUserId, userId);
        List<ArticlesColumns> articlesColumns = baseMapper.selectList(wrapper);
        return articlesColumns;
    }

    @Override
    public List<ArticlesColumns> getColumnsByCategoryId(Integer categoryId, Integer status) {
        return baseMapper.getColumnsByCategoryId(categoryId, status);
    }

    @Override
    public List<Articles> getArticleSearchByColumnsId(String columnsId) {
        return baseMapper.getArticleSearchByColumnsId(columnsId);
    }
}
