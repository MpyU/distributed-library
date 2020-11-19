package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.UserDao;
import com.library.pojo.User;
import com.library.service.UserService;
import com.library.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public User getUserById(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public User get(User user) {
        return userDao.selectOne(user);
    }

    @Override
    public User getByPWD(User user) {
        String encode = Md5Utils.encode(user.getPassword());
        user.setPassword(encode);
        return userDao.getByPWD(user);
    }

    @Override
    public PageInfo<User> selectAll(Integer pageSize, Integer currentPage) {
        PageInfo<User> pageInfo = null;
        List<User> users = null;

        if(redisTemplate.opsForValue().get("userList") == null){
            users = userDao.selectAll();
            redisTemplate.opsForValue().set("userList",users);
        }else{
            PageHelper.startPage(currentPage,pageSize);
            users = (List<User>) redisTemplate.opsForValue().get("userList");
        }
        pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @Override
    public int save(User user) {
        String encode = Md5Utils.encode(user.getPassword());
        user.setPassword(encode);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        return userDao.insert(user.setRegisterDate(date));
    }

    @Override
    public int update(User user) {
        return userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public int delete(Integer id) {
        return userDao.deleteByPrimaryKey(id);
    }



}
