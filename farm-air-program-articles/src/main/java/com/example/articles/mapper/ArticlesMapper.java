package com.example.articles.mapper;

import com.example.articles.entity.Articles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章信息表 Mapper 接口
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Mapper
public interface ArticlesMapper extends BaseMapper<Articles> {

}
