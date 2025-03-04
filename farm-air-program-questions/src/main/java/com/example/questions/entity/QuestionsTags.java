package com.example.questions.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-26
 */
@Getter
@Setter
@TableName("questions_tags")
public class QuestionsTags implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private LocalDateTime postTime;

    private LocalDateTime updateTime;
}
