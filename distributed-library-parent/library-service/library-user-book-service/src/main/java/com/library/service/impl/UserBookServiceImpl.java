package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.UserBookDao;
import com.library.pojo.Book;
import com.library.pojo.Result;
import com.library.pojo.User;
import com.library.pojo.UserBook;
import com.library.service.UserBookService;
import com.library.utils.ConvertJsonToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        Result bookResult = restTemplate.getForObject(BOOK_URL +"get/"+ userBook.getBid(), Result.class);
        Result userResult = restTemplate.getForObject(USER_URL + "get/"+userBook.getUid(), Result.class);
        User user = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), User.class);
        Book book = ConvertJsonToBean.convertMapToBean((Map<String, Object>) bookResult.getData(), Book.class);
        userBook.setUser(user);
        userBook.setBook(book);
        return userBook;
    }

    @Override
    public PageInfo<UserBook> selectAll(Integer currentPage, Integer pageSize) {
        List<UserBook> userBooks = userBookDao.selectAll();

        if(redisTemplate.opsForValue().get("userBookList") != null){
            userBooks = (List<UserBook>) redisTemplate.opsForValue().get("userBookList");
        }else{
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
            redisTemplate.opsForValue().set("userBookList",userBooks);
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
    public PageInfo<UserBook> getByUidOrBid(Integer currentPage, Integer pageSize, UserBook userBook) {
//        PageHelper.startPage(currentPage,pageSize);
//        List<UserBook> userBooks = userBookDao.getByUidOrBid(userBook);
//        for(UserBook ub : userBooks){
//            Book book = bookDao.get(new Book(ub.getBid()));
//            User user = userDao.get(new User(ub.getUid()));
//            ub.setUser(user);
//            ub.setBook(book);
//        }
//        PageInfo<UserBook> pageInfo = new PageInfo<>(userBooks);
        return null;
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
        restTemplate.put(BOOK_URL + "update",null,book);
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

}
