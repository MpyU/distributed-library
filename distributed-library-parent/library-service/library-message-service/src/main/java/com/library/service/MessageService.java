package com.library.service;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Fine;
import com.library.pojo.Notice;

public interface MessageService {

    Notice get(Integer id);
    PageInfo<Notice> selectAll(Integer currentPage, Integer pageSize);
    PageInfo<Notice> selectAllByCondition(Integer currentPage, Integer pageSize, Fine fine);
    int save(Notice t);
    int update(Notice t);
    int delete(Integer id);
    PageInfo<Notice> getNoticeByUid(Integer uid,Integer currentPage,Integer pageSize);
}
