package com.example.files.controller;

import com.example.common.utils.CommonResult;
import com.example.files.entity.Files;
import com.example.files.service.FilesService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("files")
public class FilesController {

    @Resource
    private FilesService filesService;
    @PostMapping("create")
    public CommonResult<Files> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        Files files = filesService.uploadFile(file);
        return CommonResult.success(files);
    }
}
