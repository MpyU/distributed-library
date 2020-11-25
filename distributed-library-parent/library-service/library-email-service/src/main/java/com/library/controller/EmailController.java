package com.library.controller;

import com.library.pojo.Result;
import com.library.pojo.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

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

    @GetMapping("/sendCode")
    public Result<Integer> sendCode(String email){
        if(StringUtils.isEmpty(email)){

            return new Result(ResultCode.FAIL,"请先输入邮箱");
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("PinyLib图书馆");
//        int code = (int) (Math.random()*10000);
        int code = new Random().nextInt(9999);
        redisTemplate.opsForValue().set("code:"+email,code+"",90, TimeUnit.SECONDS);
        simpleMailMessage.setText("修改密码验证码已发送至邮箱，请登录邮箱查看....您的验证码为: "+code+" , 有效期为一分钟，请及时修改密码");
        javaMailSender.send(simpleMailMessage);
        return new Result(ResultCode.SUCCESS,"发送成功！",110);
    }
}
