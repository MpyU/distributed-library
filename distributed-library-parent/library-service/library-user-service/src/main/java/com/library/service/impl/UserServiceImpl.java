package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.UserDao;
import com.library.pojo.User;
import com.library.service.UserService;
import com.library.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public User getUserById(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public User get(User user) {
        return userDao.selectOne(user);
    }

    @Override
    public User getUser(User user) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("email",user.getEmail());
        List<User> users = userDao.selectByExample(example);
        if(users.size() > 0){
            return users.get(0);
        }
        return null;
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
        PageHelper.startPage(currentPage,pageSize);
        users = userDao.selectAll();
        pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @Override
    public int save(User user) {
        String encode = Md5Utils.encode(user.getPassword());
        user.setPassword(encode);
        System.out.println("status: "+user.getStatus());
        if(user.getStatus() == null){
            user.setStatus(0);
        }
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
        int row = userDao.deleteByPrimaryKey(id);
        if(row > 0){
            Map<String,Integer> map = new HashMap<>();
            map.put("uid",id);
            restTemplate.delete("http://user-book-service/userBook/deleteByUid/{uid}",map);
        }
        return row;
    }

    @Override
    public User getUserByUsername(User user) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",user.getUsername());
        List<User> users = userDao.selectByExample(example);
        if(users.size() > 0){
            return users.get(0);
        }
        return null;
    }


}
