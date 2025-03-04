package com.example.questions.controller;

import com.example.common.utils.CommonResult;
import com.example.questions.entity.QuestionsTags;
import com.example.questions.service.IQuestionsTagsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-26
 */
@RestController
@RequestMapping("/questionsTags")
public class QuestionsTagsController {
    @Resource
    private IQuestionsTagsService questionsTagsService;

    //获取所有tag
    @GetMapping("/all")
    public CommonResult<List<QuestionsTags>> all() {
        List<QuestionsTags> list = questionsTagsService.list();
        return CommonResult.success(list);
    }
}
