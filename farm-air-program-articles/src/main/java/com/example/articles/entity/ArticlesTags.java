package com.example.articles.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticlesTags {
    private Integer id;
    private String articleId;
    private String tagName;
}
