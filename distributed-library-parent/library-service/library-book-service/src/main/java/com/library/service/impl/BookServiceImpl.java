package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.BookDao;
import com.library.pojo.*;
import com.library.service.BookService;
import com.library.utils.ConvertJsonToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Book get(Integer id) {
        return bookDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Book> selectAllByCondition(Integer currentPage, Integer pageSize, String keyword) {
        PageInfo<Book> pageInfo = null;
        Example example = new Example(Book.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orLike("bookName","%"+keyword+"%");
        criteria.orLike("author","%"+keyword+"%");
        criteria.orLike("cardId","%"+keyword+"%");
        criteria.orLike("searchId","%"+keyword+"%");
        PageHelper.startPage(currentPage,pageSize);
        List<Book> books = bookDao.selectByExample(example);
        for(Book b : books){
            Result result = restTemplate.getForObject("http://category-service/category/get/" + b.getCid(), Result.class);
            Category category = ConvertJsonToBean.convertMapToBean((Map<String, Object>) result.getData(), Category.class);
            b.setCategory(category);
        }
        pageInfo = new PageInfo<>(books);
        return pageInfo;
    }

    @Override
    public PageInfo<Book> selectAll(Integer currentPage, Integer pageSize) {
        PageInfo<Book> pageInfo = null;
        List<Book> books = null;
        PageHelper.startPage(currentPage,pageSize);
        books = bookDao.selectAll();
        pageInfo = new PageInfo<>(books);
        return pageInfo;
    }

    @Override
    public int save(Book t) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        t.setPublishDate(date);
        t.setDayClickCount(0);
        t.setMonthClickCount(0);
        t.setTotalClickCount(0);
        int row = bookDao.insert(t);
        return row;
    }




    @Override
    public int update(Book t) {
        return bookDao.updateByPrimaryKeySelective(t);
    }

    @Override
    public int delete(Integer id) {
        int row = bookDao.deleteByPrimaryKey(id);
        if(row > 0){
            Map<String,Integer> map = new HashMap<>();
            map.put("bid",id);
            restTemplate.delete("http://user-book-service/userBook/deleteByBid/{bid}",map);
        }
        return row;
    }

    @Override
    public PageInfo<Book> guess(Integer uid) {
        Result userBookResult = restTemplate.getForObject("http://user-book-service/userBook/getByUid/6/1/" + uid, Result.class);
        PageInfo<UserBook> pageInfo = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userBookResult.getData(),PageInfo.class);
        List list = pageInfo.getList();
        List<UserBook> userBooks = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            UserBook userBook = ConvertJsonToBean.convertMapToBean((Map<String, Object>) list.get(0),UserBook.class);
            userBooks.add(userBook);
        }
        Set<Integer> cids = new HashSet<>();
        Set<Integer> bids = new HashSet<>();
        //获取到该用户浏览过何种类型的书并存储到集合中
        for(UserBook userBook : userBooks){
            cids.add(userBook.getBook().getCid());
            bids.add(userBook.getBid());
        }
        List<Book> books = new ArrayList<>();
        for(Integer cid : cids){
            PageHelper.startPage(1,2);
            List<Book> select = bookDao.select(new Book().setCid(cid));
            for(Book b:select){
                if(!bids.contains(b.getId())){
                    books.add(b);
                }
            }
        }
        List<Book> books2 = new ArrayList<>();
        if(books.size() < 5){
            Example example = new Example(Book.class);
            example.setOrderByClause("total_click_count");
            PageHelper.startPage(1,5-books.size());
            books2 = bookDao.selectByExample(example);
        }
        for(Book book : books2){
            books.add(book);
        }
        PageInfo<Book> pageInfo1 = new PageInfo<>(books);
        return pageInfo1;
    }

    @Override
    public PageInfo<Book> categoryBookList(Integer categoryId,Integer pageSize,Integer currentPage) {
        Example example=new Example(Book.class);
        example.createCriteria().andEqualTo("cid",categoryId);
        PageHelper.startPage(currentPage,pageSize);
        List<Book> list=bookDao.selectByExample(example);
        PageInfo<Book> pageInfo=new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<Book> guess() {
        Example example=new Example(Book.class);
        PageHelper.startPage(1,5);
        example.setOrderByClause("total_click_count desc");
        List<Book> list=bookDao.selectByExample(example);
        return new PageInfo(list);
    }

    @Override
    public int deleteByCid(Integer cid) {
        return bookDao.delete(new Book().setCid(cid));
    }
}
