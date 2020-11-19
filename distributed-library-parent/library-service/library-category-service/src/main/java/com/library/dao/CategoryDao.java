package com.library.dao;

import com.library.pojo.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CategoryDao extends Mapper<Category> {

}
