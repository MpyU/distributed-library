package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.CategoryDao;
import com.library.pojo.Category;
import com.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Category get(Integer id) {
        return categoryDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Category> selectAll(Integer currentPage, Integer pageSize) {

        List<Category> list = null;
        if(redisTemplate.opsForValue().get("categoryList") != null){
            list = (List<Category>) redisTemplate.opsForValue().get("categoryList");
        }else{
            PageHelper.startPage(currentPage,pageSize);
            list=categoryDao.selectAll();
            redisTemplate.opsForValue().set("categoryList",list);
        }
        PageInfo<Category> pageInfo=new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<Category> selectAllByCondition(Integer currentPage, Integer pageSize, Category category) {

        return null;
    }

    @Override
    public int save(Category category) {
        return categoryDao.insert(category);
    }

    @Override
    public int update(Category category) {
        return categoryDao.updateByPrimaryKeySelective(category);
    }

    @Override
    public int delete(Integer id) {
        return categoryDao.deleteByPrimaryKey(id);
    }

}
