package com.library.service;

import com.github.pagehelper.PageInfo;
import com.library.pojo.User;

public interface UserService {
    User getUserById(Integer id);
    User get(User user);
    User getUser(User user);
    User getByPWD(User user);
    PageInfo<User> selectAll(Integer pageSize, Integer currentPage);
    int save(User user);
    int update(User user);
    int delete(Integer id);

    User getUserByUsername(User user);
}
