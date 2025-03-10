package com.example.questions.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionsEsDTO {
    private String id;
    private String title;
    private String content;
    private String aiAnswer;
    private String userId;
    private String username;
    private String avatar;
    private Integer tagId;
    private String tagName;
    private String status;
    private LocalDateTime postTime;
    private Integer readCount;
    private Integer commentCount;
    private Integer topType;
}
