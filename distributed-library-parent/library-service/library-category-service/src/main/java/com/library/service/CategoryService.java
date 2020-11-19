package com.library.service;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Category;

public interface CategoryService {
    Category get(Integer id);
    PageInfo<Category> selectAll(Integer currentPage, Integer pageSize);
    PageInfo<Category> selectAllByCondition(Integer currentPage, Integer pageSize, Category category);
    int save(Category category);
    int update(Category category);
    int delete(Integer id);
}
