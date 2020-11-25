package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.library.dao.AdminDao;
import com.library.pojo.*;
import com.library.service.AdminService;
import com.library.utils.ConvertJsonToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public  class AdminServiceImpl implements AdminService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminDao adminDao;

    private static String USER_URL = "http://USER-SERVICE/user/";
    private static String CATEGORY_URL = "http://CATEGORY-SERVICE/category/";
    private static String USER_BOOK_URL = "http://USER-BOOK-SERVICE/userBook/";
    private static String NOTICE_URL = "http://MESSAGE-SERVICE/message/";
    private static String BOOK_URL = "http://BOOK-SERVICE/book/";

    @Override
    public User getUserById(Integer id) {
        Result userResult = restTemplate.getForObject(USER_URL + "/get/" + id, Result.class);
        User user = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), User.class);
        return user;
    }
    // 查询还在借的书，看是否已经借够三本
    public Boolean canLendBook(Integer userId) {
        List<UserBook> list = adminDao.hasLendBooks(userId);
        if (list.size() < 3) {
            return true;
        }
        return false;
    }

    public int lendBook(Integer userId,Integer bookId) {

        LocalDateTime lendDate = LocalDateTime.now();
        String returnDate = lendDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        return adminDao.lendBook(userId,bookId,returnDate);

    }


//
//    public int returnBook(Integer userId, Integer bookId) {
//        String returnDate = null;
//        LocalDateTime localDate = LocalDateTime.now();
//        returnDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
//        return adminDao.returnBook(userId, bookId, returnDate);
//    }

    @Override
    public int subBook(int bookId, int num) {
        return adminDao.subBook(bookId,num);
    }

    @Override
    public Category getCategoryById(Integer id) {
        Result userResult = restTemplate.getForObject(CATEGORY_URL + "get/" + id, Result.class);
        Category category = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), Category.class);
        return category;
    }

    @Override
    public UserBook getUserBookById(Integer id) {
        Result userResult = restTemplate.getForObject(USER_BOOK_URL + "/get/" + id, Result.class);
        UserBook userBook = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), UserBook.class);
        User user = getUserById(userBook.getUid());
        Book book = getBookById(userBook.getBid());
        userBook.setUser(user);
        userBook.setBook(book);
        return userBook;
    }

    @Override
    public Notice getNoticeById(Integer id) {
        Result userResult = restTemplate.getForObject(NOTICE_URL + "get/" + id, Result.class);
        Notice notice = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), Notice.class);
        if(notice != null){
            if(notice.getUid() != 0){
                User user = getUserById(notice.getUid());
                notice.setUser(user);
            }
        }
        return notice;
    }

    @Override
    public Book getBookById(Integer id) {
        Result userResult = restTemplate.getForObject(BOOK_URL + "/get/" + id, Result.class);
        Book book = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), Book.class);
        if(book!=null && book.getCid()!=null){
            Category categoryById = getCategoryById(book.getCid());
            book.setCategory(categoryById);
        }

        return book;
    }

    @Override
    public List<User> selectAllUsers() {
//        Result userResult = restTemplate.getForObject(BOOK_URL + "/get/" + id, Result.class);
//        PageInfo pageInfo = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(), PageInfo.class);

        return null;
    }

    @Override
    public List<Category> selectAllCategories() {
        return null;
    }

    @Override
    public List<UserBook> selectAllUserBooks() {
        return null;
    }

    @Override
    public List<Notice> selectAllNotices() {
        return null;
    }

    @Override
    public List<Book> selectAllBooks() {
        return null;
    }

    @Override
    public int deleteUser(Integer id) {
        return 0;
    }

    @Override
    public int deleteCategory(Integer id) {
        return 0;
    }

    @Override
    public int deleteUserBook(Integer id) {
        return 0;
    }

    @Override
    public int deleteNotice(Integer id) {
        return 0;
    }

    @Override
    public int deleteBook(Integer id) {
        return 0;
    }

    @Override
    public int updateUser(User user) {
        return 0;
    }

    @Override
    public int updateCategory(Category category) {
        return 0;
    }

    @Override
    public int updateUserBook(UserBook userBook) {
        return 0;
    }

    @Override
    public int updateNotice(Notice notice) {
        return 0;
    }

    @Override
    public int updateBook(Book book) {
        return 0;
    }

    @Override
    public List<Book> summary(Integer currentPage, Integer pageSize, String date,int status) {
        List<UserBook> list = null;
        PageHelper.startPage(currentPage,pageSize);
        if(status == 0){
            list = adminDao.summaryByDay(date);
        }else if(status == 1){
            list = adminDao.summaryByMonth(date);
        }else{
            list = adminDao.summaryByYear(date);
        }
        List<Book> books = new ArrayList<>();
        for(UserBook ub:list){
            Result bookResult = restTemplate.getForObject(BOOK_URL + "get/" + ub.getBid(), Result.class);
            Result userResult = restTemplate.getForObject(USER_URL + "get/" + ub.getUid(), Result.class);
            Book book = ConvertJsonToBean.convertMapToBean((Map<String, Object>) bookResult.getData(),Book.class);
            User user = ConvertJsonToBean.convertMapToBean((Map<String, Object>) userResult.getData(),User.class);
            ub.setUser(user);
            ub.setBook(book);
            books.add(book);
        }

        return books;
    }


}
