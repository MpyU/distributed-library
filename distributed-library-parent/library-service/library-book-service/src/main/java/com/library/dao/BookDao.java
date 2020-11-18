package com.library.dao;

import com.library.pojo.Book;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface BookDao extends Mapper<Book> {

}
