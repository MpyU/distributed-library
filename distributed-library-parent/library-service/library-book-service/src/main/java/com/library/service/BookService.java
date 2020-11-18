package com.library.service;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Book;

public interface BookService {

    Book get(Integer id);
    PageInfo<Book> selectAllByCondition(Integer currentPage, Integer pageSize, Book book);
    PageInfo<Book> selectAll(Integer currentPage,Integer pageSize);
    int save(Book t);
    int update(Book t);
    int delete(Integer id);

}
