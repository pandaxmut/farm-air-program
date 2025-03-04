package com.example.articles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesComments;
import com.example.articles.vo.ArticlesCommentsReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 文章分类 Mapper 接口
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Mapper
public interface ArticlesCommentsMapper extends BaseMapper<ArticlesComments> {

    List<ArticlesCommentsReqVO> getArticlesByArticleId(String articleId);

    ArticlesComments getOne(String id);
}
