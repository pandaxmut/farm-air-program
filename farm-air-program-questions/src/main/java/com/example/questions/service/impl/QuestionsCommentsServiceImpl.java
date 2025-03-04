package com.example.questions.service.impl;

import com.example.questions.entity.QuestionsComments;
import com.example.questions.mapper.QuestionsCommentsMapper;
import com.example.questions.service.IQuestionsCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.questions.vo.QuestionsCommentsReqVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-25
 */
@Service
public class QuestionsCommentsServiceImpl extends ServiceImpl<QuestionsCommentsMapper, QuestionsComments> implements IQuestionsCommentsService {
    @Resource
    private QuestionsCommentsMapper questionsCommentsMapper;

    @Override


    public List<QuestionsCommentsReqVO> getArticlesByArticleId(String questionId) {
        // 获取所有评论
        List<QuestionsCommentsReqVO> allComments = questionsCommentsMapper.getArticlesByArticleId(questionId);

        // 筛选出一级评论（父评论）
        List<QuestionsCommentsReqVO> parentComments = allComments.stream()
                .filter(comment -> comment.getParentId() == null)
                .collect(Collectors.toList());

        // 筛选出所有子评论（二级及之后的评论）
        List<QuestionsCommentsReqVO> childComments = allComments.stream()
                .filter(comment -> comment.getParentId() != null)
                .collect(Collectors.toList());

        // 将所有子评论直接归类到对应的一级评论下
        for (QuestionsCommentsReqVO parentComment : parentComments) {
            List<QuestionsCommentsReqVO> children = new ArrayList<>();
            for (QuestionsCommentsReqVO childComment : childComments) {
                // 如果子评论的父 ID 等于当前一级评论的 ID，则将其归类到当前一级评论下
                if (childComment.getParentId().equals(parentComment.getId())) {
                    children.add(childComment);
                }
            }
            parentComment.setChildren(children); // 将所有子评论直接归类到一级评论下
        }

        // 返回结果：一级评论列表，每个一级评论包含所有直接子评论（无论几级）
        return parentComments;
    }
}
