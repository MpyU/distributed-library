package com.library.dao;


import com.library.pojo.User;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface UserDao extends Mapper<User> {

    User getByPWD(User user);
}
