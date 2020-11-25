package com.library.service;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Book;

public interface BookService {

    Book get(Integer id);
    PageInfo<Book> selectAllByCondition(Integer currentPage, Integer pageSize, String keyword);
    PageInfo<Book> selectAll(Integer currentPage,Integer pageSize);
    int save(Book t);
    int update(Book t);
    int delete(Integer id);

    PageInfo<Book> guess(Integer uid);

    public PageInfo<Book> categoryBookList(Integer categoryId,Integer pageSize,Integer currentPage);

    public PageInfo<Book> guess();

    int deleteByCid(Integer cid);
}
