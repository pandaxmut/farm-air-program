package com.example.questions.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-25
 */
@Getter
@Setter
@TableName("questions_comments")
public class QuestionsComments implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String questionId;
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

    public QuestionsComments() {
    }

    public QuestionsComments(String id, String parentId, String questionId, String commentText, LocalDateTime createTime, String userId, String receiverId) {
        this.id = id;
        this.parentId = parentId;
        this.questionId = questionId;
        this.commentText = commentText;
        this.createTime = createTime;
        this.userId = userId;
        this.receiverId = receiverId;
    }
}
