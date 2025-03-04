package com.example.articles.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesColumnsReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Integer categoryId;
    private String categoryName;
    private String userId;
    private String username;
    private String avatarUrl;
    private String title;
    private String summary;
    private String cover;
    private LocalDateTime postTime;
    private LocalDateTime lastUpdateTime;
    private Integer readCount;
    private Integer collectionCount;
    private Integer status;
    private List<Articles> articles;
}
