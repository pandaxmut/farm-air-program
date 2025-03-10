package com.example.questions.vo;

import com.example.questions.dto.QuestionsEsDTO;
import com.example.questions.entity.Questions;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionsSearchRespVO extends Questions {

    private String highlightTitle;
    private String highlightContent;
    private String highlightAiAnswer;

}
