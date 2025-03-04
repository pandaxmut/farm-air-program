package com.example.articles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.articles.entity.Articles;
import com.example.articles.entity.ArticlesCategory;
import com.example.articles.entity.ArticlesColumns;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface ArticlesColumnsMapper extends BaseMapper<ArticlesColumns> {

    //新增专栏 （关系表中）
    boolean saveArticleColumnsShare(@Param("columnsId") String columnsId, @Param("articles") List<Articles> articles);


    //删除专栏
    boolean deleteArticleColumnsShare(@Param("columnsId") String columnsId);

    List<ArticlesColumns> getColumnsByCategoryId(@Param("categoryId") Integer categoryId, @Param("status") Integer status);
    //获取专栏相关的文章信息 （关系表中）
    List<Articles> getArticleSearchByColumnsId(@Param("columnsId") String columnsId);

}
