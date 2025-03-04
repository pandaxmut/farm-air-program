package com.example.articles.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章信息表
 * </p>
 *
 * @author linyunhong
 * @since 2024-11-26
 */
@Data
@Mapper
public class Articles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private String id;

    /**
     * 分类ID
     */
    private Integer categoryId;

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
     * 头像
     */
    private String avatarUrl;

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
     * 收藏数
     */
    private Integer collectionCount;

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

    public Articles() {
    }

    public Articles(String id, Integer categoryId, String categoryName, String userId, String username, String avatarUrl, String title, String cover, String content, String summary, LocalDateTime postTime, LocalDateTime lastUpdateTime, Integer readCount, Integer goodCount, Integer collectionCount, Integer commentCount, Integer topType, Integer status) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.userId = userId;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.cover = cover;
        this.content = content;
        this.summary = summary;
        this.postTime = postTime;
        this.lastUpdateTime = lastUpdateTime;
        this.readCount = readCount;
        this.goodCount = goodCount;
        this.collectionCount = collectionCount;
        this.commentCount = commentCount;
        this.topType = topType;
        this.status = status;
    }
}
