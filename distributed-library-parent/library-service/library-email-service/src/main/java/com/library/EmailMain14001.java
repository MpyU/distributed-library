package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EmailMain14001 {
    public static void main(String[] args) {
        SpringApplication.run(EmailMain14001.class,args);
    }
}
