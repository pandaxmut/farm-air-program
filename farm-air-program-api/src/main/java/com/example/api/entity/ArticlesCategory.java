package com.example.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文章分类
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Getter
@Setter
@TableName("articles_category")
public class ArticlesCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父分类ID，根分类为NULL
     */
    private Integer parentId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
