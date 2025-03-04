package com.example.articles.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesComments {
    /**
     * 评论id
     */
    private String id;

    /**
     * 父评论id
     */
    private String parentId;

    /**
     * 文章id
     */
    private String articleId;
    /**
     * 评论内容
     */
    private String commentText;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 发送者
     */
    private String userId;

    /**
     * 接收者
     */
    private String receiverId;

}
