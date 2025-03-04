package com.example.articles.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesLike {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String articleId;
    private int status;


}
