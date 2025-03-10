package com.example.articles.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticlesEsDTO {
    private String id;
    private Integer categoryId;
    private String categoryName;
    private String userId;
    private String username;
    private String avatarUrl;
    private String title;
    private String content;
    private String summary;
    private List<String> tags;
    private LocalDateTime postTime;
    private Integer readCount;
    private Integer goodCount;
    private Integer collectionCount;
    private Integer commentCount;
    private Integer topType;
    private Integer status;
}
