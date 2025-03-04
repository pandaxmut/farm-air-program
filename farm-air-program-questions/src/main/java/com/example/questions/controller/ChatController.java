package com.example.questions.controller;

import cn.hutool.Hutool;
import cn.hutool.core.util.StrUtil;
import com.example.common.utils.CommonResult;
import com.example.questions.entity.Questions;
import com.example.questions.service.IAIMessagesService;
import com.example.questions.service.IQuestionsService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final OpenAiChatModel chatModel;

    @Resource
    private IQuestionsService questionsService;

    @Resource
    private IAIMessagesService aiMessagesService;

    @Autowired
    public ChatController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        String call = this.chatModel.call(message);
        System.out.println("call = " + call);
        return Map.of("generation", call);
    }

//    @GetMapping(value = "/ai/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        Prompt prompt = new Prompt(new UserMessage(message));
//        return this.chatModel.stream(prompt);
//    }
// 流式调用 将produces声明为文本事件流
    @PostMapping(value = "/ai/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestBody String message) {
        return chatModel.stream(message)
                .map(text -> "data: " + text + "\n\n")
                .concatWithValues("data: [DONE]\n\n")
                .onErrorResume(e -> Flux.just("data: [ERROR]\n\n"));
    }


    //角色扮演  流式调用 需要传入role,task,message
    @PostMapping(value = "/ai/generateStream/role", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamRole(@RequestBody Map map) {
        String role = map.get("role").toString();
        String task = map.get("task").toString();
        String question = map.get("question").toString();
        System.out.println("role = " + role);
        Prompt prompt = new Prompt(
                List.of(
                        new SystemMessage(role),
                        new SystemMessage("这是用户正在了解的内容："+task),
                        new UserMessage(question)));
//        System.out.println("prompt = " + prompt);
//        String content = StrUtil.format("{},{}",map);
//        System.out.println("content = " + content);
        return chatModel.stream(map.toString())
                .map(text -> text)
                .concatWithValues("[DONE]")
                .onErrorResume(e -> Flux.just("[ERROR]"));
    }






}