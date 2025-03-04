package com.example.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesLike {

    private static final long serialVersionUID = 1L;

    private int id;
    private String userId;
    private String articleId;
    private int status;


}
