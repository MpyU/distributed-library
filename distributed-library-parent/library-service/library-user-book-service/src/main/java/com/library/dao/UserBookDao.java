package com.library.dao;

import com.library.pojo.UserBook;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserBookDao extends Mapper<UserBook> {
}
