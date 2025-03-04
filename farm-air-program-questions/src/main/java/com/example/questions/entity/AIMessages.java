package com.example.questions.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_messages")
public class AIMessages {
    private String id;
    private String question;
    private String answer;
    private LocalDateTime postTime;

    public AIMessages() {
    }

    public AIMessages(String id, String question, String answer, LocalDateTime postTime) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.postTime = postTime;
    }
}
