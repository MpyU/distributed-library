package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "sys_user")
public class User implements Serializable {
    /**
     * id int primary key auto_increment comment "ID",
     * 	username varchar(20) comment "用户名(账户)",
     * 	password varchar(20) comment "用户密码",
     * 	telephone varchar(12) comment "联系方式",
     * 	email varchar(30) comment "邮箱",
     * 	head_image varchar(100) comment "头像",
     * 	status int default 0 comment "身份状态---0表示普通用户，1表示管理员",
     * 	register_date date comment "注册时间"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String telephone;
    private String email;
    private String headImage;
    private Integer status;
    private String registerDate;

    public User(Integer id){
        this.id = id;
    }
}
