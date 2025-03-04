package com.example.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.articles.entity.Articles;
import com.example.articles.entity.ArticlesColumns;

import java.util.List;

/**
 * <p>
 * 文章专栏 服务类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
public interface IArticlesColumnsService extends IService<ArticlesColumns> {


    boolean saveArticleColumnsShare(String columnsId, List<Articles> articles) ;

    List<ArticlesColumns> getColumnsByUserId(String userId);

    List<ArticlesColumns> getColumnsByCategoryId(Integer categoryId, Integer status);

    List<Articles> getArticleSearchByColumnsId(String columnsId);
}
