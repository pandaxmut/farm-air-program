package com.example.questions.service;

import com.example.common.utils.CommonResult;
import com.example.questions.entity.Questions;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.questions.vo.QuestionsSearchRespVO;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-25
 */
public interface IQuestionsService extends IService<Questions> {
    //新增
    CommonResult<Boolean> createQuestion(Questions questions);
    //修改
    CommonResult<Boolean> updateQuestion(Questions questions);
    //redis中查找
    CommonResult<Questions> getQuestionByIdFromRedis(String id);
    //es中查找
    CommonResult<List<QuestionsSearchRespVO>> getQuestionByKeywordFromRedis(String keyword, int page, int size) throws IOException;

    CommonResult<List<Questions>> getQuestions();

    void updateAIAnswer(Questions questions);

}
