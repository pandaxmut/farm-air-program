package com.example.articles.service;

import com.example.articles.dto.ArticlesEsDTO;
import com.example.articles.entity.Articles;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

/**
 * <p>
 * 文章信息表 服务类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
public interface IArticlesService extends IService<Articles> {
    //文章id获取文章
    Articles getArticlesById(String id);

    //es 保存文章
    void saveOrUpdateArticleToEs(ArticlesEsDTO articleEsDTO) throws IOException;
    //es 删除文章
    void deleteArticleFromEs(String id) throws IOException;
}
