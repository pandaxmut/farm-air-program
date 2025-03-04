package com.example.articles.vo;

import com.example.api.entity.Users;
import com.example.articles.entity.Articles;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesReqVO extends Articles {
    private List<String> tags;
    private boolean isGood;
    private boolean isCollection;
}
