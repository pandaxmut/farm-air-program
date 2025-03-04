package com.example.articles.service.impl;

import com.example.articles.entity.Articles;
import com.example.articles.mapper.ArticlesMapper;
import com.example.articles.service.IArticlesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章信息表 服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Service
public class ArticlesServiceImpl extends ServiceImpl<ArticlesMapper, Articles> implements IArticlesService {

    @Override
    public Articles getArticlesById(String id) {
        return this.getById(id);
    }
}
