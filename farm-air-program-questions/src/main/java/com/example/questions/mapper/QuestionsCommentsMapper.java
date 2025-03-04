package com.example.questions.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.questions.entity.QuestionsComments;
import com.example.questions.vo.QuestionsCommentsReqVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-25
 */
public interface QuestionsCommentsMapper extends BaseMapper<QuestionsComments> {
    List<QuestionsCommentsReqVO> getArticlesByArticleId(String articleId);

    QuestionsComments getOne(String id);
}
