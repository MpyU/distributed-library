package com.library.service;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Fine;

public interface FineService {
    Fine get(Integer id);
    PageInfo<Fine> selectAll(Integer currentPage, Integer pageSize);
    PageInfo<Fine> selectAllByCondition(Integer currentPage, Integer pageSize, Fine fine);
    int save(Fine t);
    int update(Fine t);
    int delete(Integer id);
}
