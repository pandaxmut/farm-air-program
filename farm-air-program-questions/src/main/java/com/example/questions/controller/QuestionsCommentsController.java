package com.example.questions.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.example.common.utils.CommonResult;
import com.example.questions.entity.QuestionsComments;
import com.example.questions.service.IQuestionsCommentsService;
import com.example.questions.vo.QuestionsCommentsReqVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@RequestMapping("/questionsComments")
public class QuestionsCommentsController {
    @Resource
    private IQuestionsCommentsService questionsCommentsService;

    @PostMapping()
    public CommonResult<String> save(@RequestBody QuestionsCommentsReqVO questionsCommentsReqVO) {
        System.out.println(questionsCommentsReqVO.getCommentText());
        System.out.println(questionsCommentsReqVO.getQuestionId());
        QuestionsComments questionsComments = new QuestionsComments();
        BeanUtil.copyProperties(questionsCommentsReqVO, questionsComments);
        //设置雪花ID
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);//这里表示，在数据中心1中的第一台机器；
        questionsComments.setId(snowflake.nextIdStr());

        questionsCommentsService.save(questionsComments);
        return CommonResult.success("ok");
    }

    @GetMapping("/{questionId}")
    public CommonResult<List<QuestionsCommentsReqVO>> getArticlesCommentsById(@PathVariable("questionId") String questionId) {
        return CommonResult.success(questionsCommentsService.getArticlesByArticleId(questionId));

    }
}
