package com.library.service;

import com.github.pagehelper.PageInfo;
import com.library.pojo.UserBook;

public interface UserBookService {
    UserBook get(Integer id);
    PageInfo<UserBook> selectAll(Integer currentPage, Integer pageSize);
    int save(UserBook t);
    int update(UserBook t);
    int delete(Integer id);

    PageInfo<UserBook> getByUidOrBid(Integer currentPage, Integer pageSize, UserBook userBook);

    int lendBook(Integer uid,Integer bookId);
    int returnBook(Integer id);
}
