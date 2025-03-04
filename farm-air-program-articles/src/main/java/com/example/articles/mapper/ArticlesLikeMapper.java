package com.example.articles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章分类 Mapper 接口
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Mapper
public interface ArticlesLikeMapper extends BaseMapper<ArticlesLike> {

}
