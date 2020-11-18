package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice implements Serializable {
    /**
     id int primary key auto_increment comment "ID",
     message varchar(255) comment "消息",
     status int comment "为0的话，就表示全发，不为0就发用户",
     publish_date date comment "发布消息时间"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message;
    private Integer status;
    private String publishDate;

    private User user;
}
