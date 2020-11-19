package com.library.controller;

import com.library.pojo.Result;
import com.library.pojo.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @GetMapping("/send")
    public Result<Integer> send(String email){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("PinyLib图书馆注册信息");
        simpleMailMessage.setText("恭喜您成功注册PinyLib图书馆，点击链接可直接跳转首页: http://10.10.102.166:8080/");
        javaMailSender.send(simpleMailMessage);
        return new Result(ResultCode.SUCCESS,"发送成功！",110);
    }
}
