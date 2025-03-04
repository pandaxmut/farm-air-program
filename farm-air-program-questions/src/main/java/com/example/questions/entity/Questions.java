package com.example.questions.entity;

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
 * @since 2025-02-25
 */
@Getter
@Setter
public class Questions implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private Integer commentCount;

    private Integer readCount;

    private Integer topType;
}
