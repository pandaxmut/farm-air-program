package com.example.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesComments;
import com.example.articles.vo.ArticlesCommentsReqVO;

import java.util.List;

/**
 * <p>
 * 文章分类 服务类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
public interface IArticlesCommentsService extends IService<ArticlesComments> {

    List<ArticlesCommentsReqVO> getArticlesByArticleId(String articleId);
}
