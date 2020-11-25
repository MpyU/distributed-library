package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import java.io.Serializable;

/**
 create table notice_user(
 id int(11) primary key auto_increment comment "id",
 nid int(11) comment "消息id",
 uid int(11) comment "用户id",
 status int comment "0为未读，1为已读"

 );
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "notice_user")
public class NoticeUser implements Serializable {

    Integer id;
    Integer uid;
    Integer nid;
    Integer status;

    public NoticeUser(Integer uid,Integer nid){
        this.nid=nid;
        this.uid=uid;
    }
    public NoticeUser(Integer uid,Integer nid,Integer status){
        this.status=status;
        this.nid=nid;
        this.uid=uid;
    }
}
