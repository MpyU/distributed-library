package com.library.dao;

import com.library.pojo.Fine;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface FineDao extends Mapper<Fine> {
}
