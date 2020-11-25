package com.library.service.impl;

import com.library.dao.MessageDao;
import com.library.dao.NoticeUserDao;
import com.library.pojo.Notice;
import com.library.pojo.NoticeUser;
import com.library.service.NoticeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class NoticeUserServiceImpl implements NoticeUserService {
    @Autowired
    NoticeUserDao noticeUserDao;
    @Autowired
    MessageDao messageDao;
    //    @Override
//    public Integer setReadByUserId(Integer userId, Integer noticeId) {
//        Example example=new Example(NoticeUser.class);
//        example.createCriteria().andEqualTo("nid",noticeId).andEqualTo("uid",userId);
//        NoticeUser noticeUser=noticeUserDao.selectOneByExample(example);
//        noticeUser.setStatus(1);
//        System.out.println("noticeUser:"+noticeUser);
//        int result=noticeUserDao.updateByExample(noticeUser,example);
//        return result;
//    }

    @Override
    public int insert(NoticeUser noticeUser) {
        return noticeUserDao.insertSelective(noticeUser);
    }

    @Override
    public List<Notice> selectunReadMsgByUserId(Integer userId) {
        List<Notice> list=messageDao.selectAll();
        List<Integer> hasRead=noticeUserDao.selectHasRead(userId);
        Iterator<Notice> it = list.iterator();
        while (it.hasNext()) {
            Notice notice=it.next();
            if(hasRead.contains(notice.getId())){
               it.remove();
            }
        }
        return list;
    }
}
