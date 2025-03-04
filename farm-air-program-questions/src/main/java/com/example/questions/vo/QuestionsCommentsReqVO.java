package com.example.questions.vo;

import com.example.questions.entity.QuestionsComments;
import lombok.Data;

import java.util.List;

@Data
public class QuestionsCommentsReqVO extends QuestionsComments {
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

    private List<QuestionsCommentsReqVO> children;

    public QuestionsCommentsReqVO() {
    }

    public QuestionsCommentsReqVO(String username, String avatarUrl, String receiverUsername, String receiverAvatarUrl, List<QuestionsCommentsReqVO> children) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.receiverUsername = receiverUsername;
        this.receiverAvatarUrl = receiverAvatarUrl;
        this.children = children;
    }


}
