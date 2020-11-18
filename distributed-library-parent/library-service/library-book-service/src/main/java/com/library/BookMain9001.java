package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.library.dao")
public class BookMain9001 {

    public static void main(String[] args) {
        SpringApplication.run(BookMain9001.class,args);
    }

}