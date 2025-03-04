package com.example.files.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Files {
    private Integer id;
    private String fileName;
    private String filePath;
    private String fileUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
