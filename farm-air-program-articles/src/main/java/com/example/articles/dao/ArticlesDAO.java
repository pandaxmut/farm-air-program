package com.example.articles.dao;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ArticlesDAO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private String id;

    /**
     * 文章分类名
     */
    private String categoryName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 内容
     */
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 发布时间
     */
    private LocalDateTime postTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 阅读数量
     */
    private Integer readCount;

    /**
     * 点赞数
     */
    private Integer goodCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 0未置顶 1:已置顶
     */
    private Integer topType;

    /**
     * -1已删除 0:待审核 1:已审核
     */
    private Integer status;
}
