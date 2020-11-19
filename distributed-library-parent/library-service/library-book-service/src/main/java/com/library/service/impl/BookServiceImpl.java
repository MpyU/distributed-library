package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.BookDao;
import com.library.pojo.Book;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Book get(Integer id) {
        return bookDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Book> selectAllByCondition(Integer currentPage, Integer pageSize, Book book) {
        PageInfo<Book> pageInfo = null;
        List<Book> books = null;
        if(redisTemplate.opsForValue().get("bookList") == null){
            if(!StringUtils.isEmpty(book.getBookName())){
                book.setBookName("%"+book.getBookName()+"%");
            }
            books = bookDao.select(book);
            redisTemplate.opsForValue().set("bookList",books);
        }else{
            PageHelper.startPage(currentPage,pageSize);
            books = (List<Book>) redisTemplate.opsForValue().get("bookList");

        }
        pageInfo = new PageInfo<>(books);
        return pageInfo;
    }

    @Override
    public PageInfo<Book> selectAll(Integer currentPage, Integer pageSize) {
        PageInfo<Book> pageInfo = null;
        List<Book> books = null;
        PageHelper.startPage(currentPage,pageSize);
        if(redisTemplate.opsForValue().get("bookList") == null){
            books = bookDao.selectAll();
            redisTemplate.opsForValue().set("bookList",books);
        }else{
            books = (List<Book>) redisTemplate.opsForValue().get("bookList");
        }
        pageInfo = new PageInfo<>(books);
        return pageInfo;
    }

    @Override
    public int save(Book t) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        t.setPublishDate(date);
        int row = bookDao.insert(t);
        return row;
    }

    @Override
    public int update(Book t) {
        return bookDao.updateByPrimaryKeySelective(t);
    }

    @Override
    public int delete(Integer id) {
        return bookDao.deleteByPrimaryKey(id);
    }
}
