package com.example.questions.service.impl;

import com.example.questions.entity.Questions;
import com.example.questions.mapper.QuestionsMapper;
import com.example.questions.service.IQuestionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linyunhong
 * @since 2025-02-25
 */
@Service
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions> implements IQuestionsService {

}
