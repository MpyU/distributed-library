package com.library.service;

import com.github.pagehelper.PageInfo;
import com.library.pojo.UserBook;

public interface UserBookService {
    UserBook get(Integer id);
    PageInfo<UserBook> selectAll(Integer currentPage, Integer pageSize);
    int save(UserBook t);
    int update(UserBook t);
    int delete(Integer id);
    int deleteByUid(Integer uid);
    int deleteByBid(Integer bid);

    PageInfo<UserBook> getByUid(Integer currentPage, Integer pageSize, Integer uid);

    int lendBook(Integer uid,Integer bookId);
    int returnBook(Integer id);

    //查询出该用户借的这本书的一条记录，从而得到这条记录的id，然后根据id还书
    public UserBook selectOneByUidAndBookId(Integer uid, Integer bid);
}
