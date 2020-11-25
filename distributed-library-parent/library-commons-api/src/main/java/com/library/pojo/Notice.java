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
public class Notice implements Serializable {
    /**
     id int primary key auto_increment comment "ID",
     message varchar(255) comment "消息",
     uid int comment "为0的话，就表示全发，不为0就发用户",
     publish_date date comment "发布消息时间"
     status int comment "是否已读",
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message;
    private Integer uid;
    private String publishDate;
    private Integer status;

    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Notice notice = (Notice) o;

        if (id != null ? !id.equals(notice.id) : notice.id != null) return false;
        if (message != null ? !message.equals(notice.message) : notice.message != null) return false;
        if (uid != null ? !uid.equals(notice.uid) : notice.uid != null) return false;
        if (publishDate != null ? !publishDate.equals(notice.publishDate) : notice.publishDate != null) return false;
        if (status != null ? !status.equals(notice.status) : notice.status != null) return false;
        return user != null ? user.equals(notice.user) : notice.user == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (publishDate != null ? publishDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
