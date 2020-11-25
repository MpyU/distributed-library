package com.library.service;

import com.library.pojo.UserEmail;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "email_queue")
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @RabbitHandler
    public void receiveEmail(UserEmail userEmail){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(userEmail.getFrom());
        simpleMailMessage.setTo(userEmail.getTo());
        simpleMailMessage.setSubject(userEmail.getSubject());
        simpleMailMessage.setText(userEmail.getText());
        javaMailSender.send(simpleMailMessage);
    }

}

