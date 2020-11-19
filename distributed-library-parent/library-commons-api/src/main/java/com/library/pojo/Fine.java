package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Fine implements Serializable {
    /**
     id int primary key auto_increment comment "ID",
     uid int comment "用户ID",
     price double comment "罚款金额",
     fine_desc varchar(255) comment "罚款描述即原因",
     pay_date date comment "付款时间"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer uid;
    private Double price;
    private String fineDesc;
    private String payDate;
}
