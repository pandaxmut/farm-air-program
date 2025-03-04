package com.example.questions.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.example.common.utils.CommonResult;
import com.example.questions.entity.AIMessages;
import com.example.questions.service.impl.AIMessagesServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/aiMessage")
public class AIMessagesController {
    @Resource
    private AIMessagesServiceImpl aiMessagesService;

    @PostMapping()
    public CommonResult<Boolean> save(@RequestBody AIMessages aiMessages) {
        //id
        System.out.println("aiMessages = " + aiMessages.getQuestion());
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);//这里表示，在数据中心1中的第一台机器；
        aiMessages.setId(snowflake.nextIdStr());
        aiMessages.setPostTime(LocalDateTime.now());
        return CommonResult.success(aiMessagesService.save(aiMessages)) ;

    }
}
