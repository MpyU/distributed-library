package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.library.dao")

public class MessageMain13001 {
    public static void main(String[] args) {
        SpringApplication.run(MessageMain13001.class,args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate addRestTemplate(){
        return new RestTemplate();
    }
}


