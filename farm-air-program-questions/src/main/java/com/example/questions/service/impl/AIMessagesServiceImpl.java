package com.example.questions.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.questions.entity.AIMessages;
import com.example.questions.entity.Questions;
import com.example.questions.mapper.AIMessagesMapper;
import com.example.questions.mapper.QuestionsMapper;
import com.example.questions.service.IAIMessagesService;
import com.example.questions.service.IQuestionsService;
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
public class AIMessagesServiceImpl extends ServiceImpl<AIMessagesMapper, AIMessages> implements IAIMessagesService {

}
