package com.library.dao;

import com.library.pojo.Notice;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface MessageDao extends Mapper<Notice> {
}
