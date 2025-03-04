package com.example.users.message;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class Provider {
    @Autowired
    private JavaMailSender javaMailSender;

    public void send() throws MessagingException, UnsupportedEncodingException {
        //创建消息实例化对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
         // 发件人邮箱和名称
        helper.setFrom("linyhpanda@163.com", "Farm Air Program平台");
        // 收件人邮箱
        helper.setTo("linyh_xmcu_edu@163.com");
        // 邮件标题
        helper.setSubject("Farm Air Program平台验证码");
        // 邮件正文，第二个参数表示是否是HTML正文
        helper.setText("<h2>Farm Air Program平台验证码</h2>  验证码 <strong>9999</strong>！ 5分钟之后过期", true);
        // 发送
        javaMailSender.send(mimeMessage);
    }
}
