package com.library.service;

import com.library.pojo.Notice;
import com.library.pojo.NoticeUser;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface NoticeUserService {
    int insert(NoticeUser noticeUser);

    List<Notice> selectunReadMsgByUserId(Integer userId);
//    Integer setReadByUserId(Integer userId, Integer noticeId);

}
