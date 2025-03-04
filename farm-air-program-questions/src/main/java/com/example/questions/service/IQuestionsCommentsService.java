package com.example.questions.service;

import com.example.questions.entity.QuestionsComments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.questions.vo.QuestionsCommentsReqVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-25
 */
public interface IQuestionsCommentsService extends IService<QuestionsComments> {
    List<QuestionsCommentsReqVO> getArticlesByArticleId(String questionId);

}
