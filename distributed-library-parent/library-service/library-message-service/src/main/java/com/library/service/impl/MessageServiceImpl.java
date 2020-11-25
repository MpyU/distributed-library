package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.MessageDao;
import com.library.dao.NoticeUserDao;
import com.library.pojo.*;
import com.library.service.MessageService;
import com.library.utils.ConvertJsonToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    RestTemplate restTemplate;
    private static String USER_URL = "http://USER-SERVICE/user/";

    @Autowired
    private MessageDao messageDao;



    @Autowired
    private NoticeUserDao noticeUserDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Notice get(Integer id) {
        return messageDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Notice> selectAll(Integer currentPage, Integer pageSize) {
        List<Notice> list = null;
        PageHelper.startPage(currentPage,pageSize);
        list = messageDao.selectAll();
        redisTemplate.opsForValue().set("noticeList",list,60*10, TimeUnit.SECONDS);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Notice> selectAllByCondition(Integer currentPage, Integer pageSize, Fine fine) {
        return null;
    }

    @Override
    public int save(Notice t) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        t.setPublishDate(date);
        t.setStatus(0);
//        Result result=restTemplate.getForObject(USER_URL+"selectAll", Result.class);
//        List list= (List<User>) result.getData();
//       System.out.println("list:"+list);
        int insertResult=messageDao.insert(t);
//        for (Object obj:list) {
//            User user=ConvertJsonToBean.convertMapToBean((Map<String, Object>) obj,User.class);
//            noticeUserDao.insert(new NoticeUser(user.getId(),t.getId()));
//        }
        return insertResult;
    }

    @Override
    public int update(Notice t) {
        t.setStatus(1);
        return messageDao.updateByPrimaryKeySelective(t);
    }

    @Override
    public int delete(Integer id) {
        return messageDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Notice> getNoticeByUid(Integer uid,Integer currentPage,Integer pageSize) {

        List<Notice> list = null;
        PageHelper.startPage(currentPage,pageSize);
        list = messageDao.select(new Notice().setUid(uid));
        return new PageInfo<>(list);
    }

//    @RabbitListener(queues = "email_queue")
//    @RabbitHandler
//    public void receiveMsg(String code){
//
//    }
  @Override
  public List<Notice> selectByMessage(Integer pageSize, Integer currentPage, String message) {
    message="%"+message+"%";
    PageHelper.startPage(currentPage,pageSize);
    Example example=new Example(Notice.class);
    example.createCriteria().andLike("message",message);
    return messageDao.selectByExample(example);
  }
}
