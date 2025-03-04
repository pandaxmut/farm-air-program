package com.example.questions.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.example.common.utils.CommonResult;
import com.example.questions.entity.Questions;
import com.example.questions.service.IQuestionsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/questions")
public class QuestionsController {
    @Resource
    private IQuestionsService questionsService;
    //新增问题
    @PostMapping()
    public CommonResult<Boolean> save(@RequestBody Questions questions) {
        //设置id
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);//这里表示，在数据中心1中的第一台机器；
        questions.setId(snowflake.nextIdStr());
        return CommonResult.success(questionsService.save(questions));
    }

    //all
    @GetMapping("/all")
    public CommonResult<List<Questions>> all() {
        return CommonResult.success( questionsService.list());
    }

    //id
    @GetMapping("/{id}")
    public CommonResult<Questions> getById(@PathVariable("id") String id) {
        return CommonResult.success(questionsService.getById(id));
    }
    //部分更新
    @PatchMapping()
    public CommonResult<Boolean> update(@RequestBody Questions questions) {
        return CommonResult.success(questionsService.updateById(questions));
    }




}
