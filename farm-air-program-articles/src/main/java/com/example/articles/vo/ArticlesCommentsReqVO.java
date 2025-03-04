package com.example.articles.vo;

import com.example.articles.entity.Articles;
import com.example.articles.entity.ArticlesComments;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticlesCommentsReqVO extends ArticlesComments {
    /**
     * 评论者昵称
     */
    private String username;

    /**
     * 评论者头像
     */
    private String avatarUrl;


    /**
     * 接收者昵称
     */
    private String receiverUsername;

    /**
     * 接收者头像
     */
    private String receiverAvatarUrl;

    private List<ArticlesCommentsReqVO> children;

    public ArticlesCommentsReqVO() {
    }

    public ArticlesCommentsReqVO(String id, String parentId, String articleId, String commentText, LocalDateTime createTime, String userId, String receiverId, String username, String avatarUrl, String receiverUsername, String receiverAvatarUrl, List<ArticlesCommentsReqVO> children) {
        super(id, parentId, articleId, commentText, createTime, userId, receiverId);
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.receiverUsername = receiverUsername;
        this.receiverAvatarUrl = receiverAvatarUrl;
        this.children = children;
    }

    public ArticlesCommentsReqVO(String username, String avatarUrl, String receiverUsername, String receiverAvatarUrl, List<ArticlesCommentsReqVO> children) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.receiverUsername = receiverUsername;
        this.receiverAvatarUrl = receiverAvatarUrl;
        this.children = children;
    }

}
