package com.example.articles.vo;

import com.example.articles.dto.ArticlesEsDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * es
 */

@Data
public class ArticlesSearchRespVO extends ArticlesEsDTO {

    private String highlightTitle;

    private String highlightSummary;

    private String highlightContent;

}
