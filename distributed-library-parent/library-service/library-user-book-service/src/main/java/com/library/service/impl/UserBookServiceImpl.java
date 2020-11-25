package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.UserBookDao;
import com.library.pojo.*;
import com.library.service.UserBookService;
import com.library.utils.ConvertJsonToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserBookServiceImpl implements UserBookService {

    @Autowired
    private UserBookDao userBookDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String USER_URL = "http://USER-SERVICE/user/";
    private static final String BOOK_URL = "http://BOOK-SERVICE/book/";

    @Override
    public UserBook get(Integer id) {
        UserBook userBook = userBookDao.selectByPrimaryKey(id);
        if(userBook != null){
            Result bookResult = restTemplate.getForObject(BOOK_URL +"get/"+ userBook.getBid(), Result.class);
            Result userResult = restTemplate.getForObject(USER_URL + "get/"+userBook.getUid(), Result.class);
            User user = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), User.class);
            Book book = ConvertJsonToBean.convertMapToBean((Map<String, Object>) bookResult.getData(), Book.class);
            userBook.setUser(user);
            userBook.setBook(book);
        }
        return userBook;
    }

    @Override
    public PageInfo<UserBook> selectAll(Integer currentPage, Integer pageSize) {
        List<UserBook> userBooks = userBookDao.selectAll();
        PageHelper.startPage(currentPage,pageSize);
        userBooks = userBookDao.selectAll();
        for(UserBook userBook : userBooks){
            Result userResult = restTemplate.getForObject(USER_URL+"get/"+userBook.getUid(),Result.class);
            Result bookResult = restTemplate.getForObject(BOOK_URL+"get/"+userBook.getBid(),Result.class);
            User user = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), User.class);
            Book book = ConvertJsonToBean.convertMapToBean((Map<String, Object>) bookResult.getData(), Book.class);
            userBook.setUser(user);
            userBook.setBook(book);
        }
        PageInfo<UserBook> pageInfo = new PageInfo<>(userBooks);
        return pageInfo;
    }

    @Override
    public int save(UserBook userBook) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        userBook.setLendDate(date);
        userBook.setStatus(0);
        return userBookDao.insert(userBook);
    }

    @Override
    public int update(UserBook userBook) {
        return userBookDao.updateByPrimaryKeySelective(userBook);
    }

    @Override
    public int delete(Integer id) {
        return userBookDao.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByUid(Integer uid) {
        return userBookDao.delete(new UserBook().setUid(uid));
    }

    @Override
    public int deleteByBid(Integer bid) {
        return userBookDao.delete(new UserBook().setBid(bid));
    }

    @Override
    public PageInfo<UserBook> getByUid(Integer currentPage, Integer pageSize, Integer uid) {
        PageHelper.startPage(currentPage,pageSize);
        List<UserBook> userBooks = userBookDao.select(new UserBook().setUid(uid));
        for(UserBook ub : userBooks){
            Result bookResult = restTemplate.getForObject("http://BOOK-SERVICE/book/get/" + ub.getBid(), Result.class);
            Result userResult = restTemplate.getForObject("http://USER-SERVICE/user/get/" + ub.getUid(), Result.class);
            Book book = ConvertJsonToBean.convertMapToBean((Map<String, Object>) bookResult.getData(),Book.class);
            User user = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(),User.class);
            Result categoryResult = restTemplate.getForObject("http://category-service/category/get/" + book.getCid(), Result.class);
            if(categoryResult!=null &&categoryResult.getData()!=null){
                Category category = ConvertJsonToBean.convertMapToBean((Map<String, Object>) categoryResult.getData(), Category.class);
                book.setCategory(category);

            }
            ub.setUser(user);
            ub.setBook(book);

        }
        PageInfo<UserBook> pageInfo = new PageInfo<>(userBooks);
        return pageInfo;
    }

    @Override
    public int lendBook(Integer userId,Integer bookId) {
        Result bookResult = restTemplate.getForObject(BOOK_URL + "get/" + bookId, Result.class);
        Book book = ConvertJsonToBean.convertMapToBean((Map<String, Object>) bookResult.getData(), Book.class);
        if(book.getCount() == 0){
            return -1;
        }
        List<UserBook> userBookList = userBookDao.select(new UserBook().setUid(userId));
        if(userBookList.size() >= 3){
            return Integer.MAX_VALUE;
        }
        book.setCount(book.getCount()-1);
//        restTemplate.postForObject(BOOK_URL + "update", Result.class, book);
        restTemplate.put(BOOK_URL + "update",book);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String date = simpleDateFormat.format(System.currentTimeMillis());
        UserBook userBook = new UserBook(userId,bookId,0,date);
        return userBookDao.insert(userBook);
    }

    @Override
    public int returnBook(Integer id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        return userBookDao.updateByPrimaryKeySelective(new UserBook().setId(id).setStatus(1).setReturnDate(date));
    }

    @Override
    public UserBook selectOneByUidAndBookId(Integer uid, Integer bid) {
        Example example=new Example(UserBook.class);
        PageHelper.startPage(1,1);
        example.createCriteria().andEqualTo("uid",uid).andEqualTo("bid",bid);

        return  userBookDao.selectOneByExample(example);
    }


}
