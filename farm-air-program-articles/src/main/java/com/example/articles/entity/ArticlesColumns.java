package com.example.articles.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

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
@Data
@TableName("articles_columns")
public class ArticlesColumns implements Serializable {

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

    public ArticlesColumns() {
    }

    public ArticlesColumns(String id, Integer categoryId, String categoryName, String userId, String username, String avatarUrl, String title, String summary, String cover, LocalDateTime postTime, LocalDateTime lastUpdateTime, Integer readCount, Integer collectionCount, Integer status) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.userId = userId;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.summary = summary;
        this.cover = cover;
        this.postTime = postTime;
        this.lastUpdateTime = lastUpdateTime;
        this.readCount = readCount;
        this.collectionCount = collectionCount;
        this.status = status;
    }
}
