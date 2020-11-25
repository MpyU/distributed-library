package com.library.service;

import com.library.pojo.*;

import java.util.List;

public interface AdminService {
    //查询单个
//    Object get(String path,Integer id);
    User getUserById(Integer id);
    Category getCategoryById(Integer id);
    UserBook getUserBookById(Integer id);
    Notice getNoticeById(Integer id);
    Book getBookById(Integer id);

    //查询所有
//    List<Object> selectAll(String path,Integer currentPage,Integer pageSize);
    List<User> selectAllUsers();
    List<Category> selectAllCategories();
    List<UserBook> selectAllUserBooks();
    List<Notice> selectAllNotices();
    List<Book> selectAllBooks();

    //删除
    int deleteUser(Integer id);
    int deleteCategory(Integer id);
    int deleteUserBook(Integer id);
    int deleteNotice(Integer id);
    int deleteBook(Integer id);

    //修改
    int updateUser(User user);
    int updateCategory(Category category);
    int updateUserBook(UserBook userBook);
    int updateNotice(Notice notice);
    int updateBook(Book book);

    List<Book> summary(Integer currentPage, Integer pageSize, String date, int status);

    //添加
//    int saveBook(Book book);
//    int saveCategory(Category category);
// 查询还在借的书，看是否已经借够三本
    public Boolean canLendBook(Integer userId);
    public int lendBook( Integer userId,Integer bookId);

//    public int  returnBook(Integer userId, Integer bookId);

    int subBook(int bookId, int num);



}
