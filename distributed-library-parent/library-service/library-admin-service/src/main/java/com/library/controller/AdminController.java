package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.library.pojo.*;
import com.library.service.AdminService;
import com.library.utils.ConvertJsonToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RestTemplate restTemplate;

    private static String USER_URL = "http://USER-SERVICE/user/";
    private static String CATEGORY_URL = "http://CATEGORY-SERVICE/category/";
    private static String USER_BOOK_URL = "http://USER-BOOK-SERVICE/userBook/";
    private static String NOTICE_URL = "http://MESSAGE-SERVICE/message/";
    private static String BOOK_URL = "http://BOOK-SERVICE/book/";

//
////    借书，还书：
////    借书：
    @PostMapping("/lend/{userId}/{bookId}")
public Result lendBook(@PathVariable("userId")Integer userId,@PathVariable("bookId") Integer bookId) {
//		Map<String, Object> user = mapparseHeaderToUser(httpServletRequest);

    // 用户借书数量是否达到三本
    boolean canLean = adminService.canLendBook(userId);
    // 书的余量
    Book book= adminService.getBookById(bookId);
    int bookRemain=0;
    if(book!=null &&book.getCount()!=null){
        bookRemain=book.getCount();
    }
    if (canLean && bookRemain > 0) {
        int num = adminService.lendBook(userId,bookId);
        adminService.subBook(bookId,1);
        if (num > 0) {
            return new Result<>(ResultCode.SUCCESS, "借书成功");
        } else {
            return new Result<>(ResultCode.FAIL, "借书失败");
        }
    } else {
        if (bookRemain <= 0) {
            return new Result<>(ResultCode.FAIL, "该书已经借完");
        }
        return new Result<>(ResultCode.FAIL, "借书数量已经达到三本");
    }

}

    // 还书
    // http://10.10.102.163:8001/user/returnBook/bookId
    @PostMapping("returnBook/{userBookID}")
    public Result returnBook(@PathVariable("userBookID") Integer userBookID) {
           Result result=restTemplate.getForObject(USER_BOOK_URL+"/return/"+userBookID,Result.class);
           result=restTemplate.getForObject(USER_BOOK_URL+"/get/"+userBookID,Result.class);
           UserBook userBook=ConvertJsonToBean.convertMapToBean((Map<String, Object>) result.getData(),UserBook.class);
           result=restTemplate.getForObject(BOOK_URL+"get/"+userBook.getBid(),Result.class);
           Book book=ConvertJsonToBean.convertMapToBean((Map<String, Object>) result.getData(),Book.class);
           book.setCount(book.getCount()+1);
           HttpHeaders header = new HttpHeaders();
           header.setContentType(MediaType.APPLICATION_JSON_UTF8);
           HttpEntity<String> entity = new HttpEntity<String>(new Gson().toJson(book),header);
           restTemplate.put(BOOK_URL+"update",entity);
        return result;
    }

//    public <T> T sendRequest(Class clasz, String url, HttpMethod httpMethod){
//        Map<String,String> paramters = new HashMap<>();
////        paramters.put("id",id);
////        paramters.put("access_token",accessToken);
//        // 请求头
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        // 发送请求
//        HttpEntity entity = new HttpEntity(null, headers);
//        ResponseEntity<T> responseEntity= restTemplate.exchange(url, httpMethod,entity,clasz);
//
//        return responseEntity.getBody();
//    }

    //类别id,书籍推荐：总点击量最多的5本
    @GetMapping("/guess")
    public Result<PageInfo<Book>> guess(){

        return null;
    }

    //列出某一个分类的所有图书
    @GetMapping("/categoryBookList/{categryId}/{pageSzie}/{currentPage}")
    public Result<PageInfo<Book>> categoryBookList(@PathVariable("categryId")Integer categoryId,@PathVariable("pageSzie") Integer pageSize,@PathVariable("currentPage")Integer currentPage){
        Result result=restTemplate.getForObject(BOOK_URL+"categoryBookList/"+categoryId+"/"+pageSize+"/"+currentPage,Result.class);
        Object pageInfo=result.getData();
//        if(pageInfo!=null ){
//            PageInfo<Book> bookPageInfo=ConvertJsonToBean.convertMapToBean((Map<String, Object>) pageInfo,PageInfo.class);
//            return new Result(ResultCode.SUCCESS,"查询分类成功",bookPageInfo);
//        }
//        return new Result(ResultCode.FAIL,"查询分类失败");
        return result;
    }
    @GetMapping("/user/get/{id}")
    public Result<User> getUserById(@PathVariable("id")Integer id){

        User user = adminService.getUserById(id);
        if(user != null){
            return new Result(ResultCode.SUCCESS,"查询用户成功！",user);
        }
        return new Result(ResultCode.FAIL,"查询用户失败！");
    }

    @GetMapping("/category/get/{id}")
    public Result<Category> getCategoryById(@PathVariable("id")Integer id){

        Category category = adminService.getCategoryById(id);
        if(category != null){
            return new Result(ResultCode.SUCCESS,"查询分类成功！",category);
        }
        return new Result(ResultCode.FAIL,"查询分类失败！");
    }

    @GetMapping("/notice/get/{id}")
    public Result<User> getNoticeById(@PathVariable("id")Integer id){
        Notice notice = adminService.getNoticeById(id);
        if(notice != null){
            return new Result(ResultCode.SUCCESS,"查询消息成功！",notice);
        }
        return new Result(ResultCode.FAIL,"查询消息失败！");
    }

    @GetMapping("/userBook/get/{id}")
    public Result<UserBook> getUserBookById(@PathVariable("id")Integer id){
        return restTemplate.getForObject(USER_BOOK_URL + "get/" + id, Result.class);
    }

    @GetMapping("/book/get/{id}")
    public Result<Book> getBookById(@PathVariable("id")Integer id){

        Book book = adminService.getBookById(id);
        if(book != null){
            return new Result(ResultCode.SUCCESS,"查询借阅历史成功！",book);
        }
        return new Result(ResultCode.FAIL,"查询借阅历史失败！");
    }

    @GetMapping("/user/select/{pageSize}/{currentPage}")
    public Result<PageInfo<User>> selectAllUsers(@PathVariable("pageSize")Integer pageSize,
                                                 @PathVariable("currentPage")Integer currentPage){

        Result result = restTemplate.getForObject(USER_URL + "select/" + pageSize + "/" + currentPage, Result.class);
        return result;
    }

    @GetMapping("/book/select/{pageSize}/{currentPage}")
    public Result<PageInfo<User>> selectAllBooks(@PathVariable("pageSize")Integer pageSize,
                                                 @PathVariable("currentPage")Integer currentPage, HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Result result = restTemplate.getForObject(BOOK_URL + "select/" + pageSize + "/" + currentPage, Result.class);
        return result;
    }

    @GetMapping("/category/select/{pageSize}/{currentPage}")
    public Result<PageInfo<User>> selectAllCategories(@PathVariable("pageSize")Integer pageSize,
                                                 @PathVariable("currentPage")Integer currentPage){

        Result result = restTemplate.getForObject(CATEGORY_URL + "select/" + pageSize + "/" + currentPage, Result.class);
        return result;
    }

    @GetMapping("/userBook/select/{pageSize}/{currentPage}")
    public Result<PageInfo<User>> selectAllUserBooks(@PathVariable("pageSize")Integer pageSize,
                                                 @PathVariable("currentPage")Integer currentPage){

        Result result = restTemplate.getForObject(USER_BOOK_URL + "select/" + pageSize + "/" + currentPage, Result.class);
        return result;
    }

    @GetMapping("/userBook/getByUid/{pageSize}/{currentPage}/{uid}")
    public Result<PageInfo<User>> getByUid(@PathVariable("pageSize")Integer pageSize,
                                                     @PathVariable("currentPage")Integer currentPage,
                                           @PathVariable("uid")Integer uid){
        Result result = restTemplate.getForObject(USER_BOOK_URL + "getByUid/" + pageSize + "/" + currentPage+"/"+uid, Result.class);
        return result;
    }

    @GetMapping("/notice/select/{pageSize}/{currentPage}")
    public Result<PageInfo<User>> selectAllNotices(@PathVariable("pageSize")Integer pageSize,
                                                 @PathVariable("currentPage")Integer currentPage){

        Result result = restTemplate.getForObject(NOTICE_URL + "select/" + pageSize + "/" + currentPage, Result.class);
        return result;
    }

    @GetMapping("/notice/getByUid/{pageSize}/{currentPage}/{uid}")
    public Result<PageInfo<User>> getNoticesByUid(@PathVariable("pageSize")Integer pageSize,
                                                   @PathVariable("currentPage")Integer currentPage,
                                           @PathVariable("uid")Integer uid){

        Result result = restTemplate.getForObject(NOTICE_URL + "getByUid/"+uid+"/"+ pageSize + "/" + currentPage, Result.class);
        return result;
    }

    @PutMapping("/book/update")
    public Result<Integer> updateBook(Book book){
        restTemplate.put(BOOK_URL+"update",book);
        return new Result(ResultCode.SUCCESS,"修改图书信息成功！",1);
    }

    @PutMapping("/category/update")
    public Result<Integer> updateCategory(Category category){
        restTemplate.put(CATEGORY_URL+"update",category);
        return new Result(ResultCode.SUCCESS,"修改分类信息成功！",1);
    }

    @PutMapping("/userBook/update")
    public Result<Integer> updateUserBook(UserBook userBook){
        restTemplate.put(USER_BOOK_URL+"/update",userBook);
        return new Result(ResultCode.SUCCESS,"修改借阅历史信息成功！",1);
    }

    @PutMapping("/notice/update")
    public Result<Integer> updateNotice(Notice notice){
        restTemplate.put(NOTICE_URL+"update",notice);
        return new Result(ResultCode.SUCCESS,"修改消息信息成功！",1);
    }

    @PutMapping("/user/update")
    public Result<Integer> updateUser(User user){
        restTemplate.put(USER_URL+"update",user);
        return new Result(ResultCode.SUCCESS,"修改用户信息成功！",1);
    }

    @DeleteMapping("/user/delete/{id}")
    public Result<Integer> deleteUser(@PathVariable("id")Integer id){
        restTemplate.delete(USER_URL+"delete/"+id);
        return new Result(ResultCode.SUCCESS,"删除用户信息成功！",1);
    }

    @DeleteMapping("/book/delete/{id}")
    public Result<Integer> deleteBook(@PathVariable("id")Integer id){
        restTemplate.delete(BOOK_URL+"delete/"+id);
        return new Result(ResultCode.SUCCESS,"删除书籍信息成功！",1);
    }

    @DeleteMapping("/notice/delete/{id}")
    public Result<Integer> deleteNotice(@PathVariable("id")Integer id){
        restTemplate.delete(NOTICE_URL+"delete/"+id);
        return new Result(ResultCode.SUCCESS,"删除消息信息成功！",1);
    }

    @DeleteMapping("/userBook/delete/{id}")
    public Result<Integer> deleteUserBook(@PathVariable("id")Integer id){
        restTemplate.delete(USER_BOOK_URL+"delete/"+id);
        return new Result(ResultCode.SUCCESS,"删除借阅历史信息成功！",1);
    }

    @DeleteMapping("/category/delete/{id}")
    public Result<Integer> deleteCategory(@PathVariable("id")Integer id){
        restTemplate.delete(CATEGORY_URL+"delete/"+id);
        return new Result(ResultCode.SUCCESS,"删除分类信息成功！",1);
    }

    @PostMapping("/book/save")
    public Result<Integer> saveBook(Book book){
        return restTemplate.postForObject(BOOK_URL+"save",book,Result.class);
    }

    @PostMapping("/user/save")
    public Result<Integer> saveUser(User user){
        return restTemplate.postForObject(USER_URL+"save",user,Result.class);
    }

    @PostMapping("/category/save")
    public Result<Integer> saveCategory(Category category){
        return restTemplate.postForObject(CATEGORY_URL+"save",category,Result.class);
    }

    @PostMapping("/notice/save")
    public Result<Integer> saveNotice(Notice notice){
        return restTemplate.postForObject(NOTICE_URL+"save",notice,Result.class);
    }

    @GetMapping("/selectByClickCount")
    public Result<PageInfo<Book>> selectByClickCount(Integer status,String pdate){
        String day = "";
        String month = "";
        String year = "";
        List<Book> books = null;
        if(status == 0){
            day = pdate.substring(pdate.lastIndexOf("-")+1);
            books = adminService.summary(1, 10, day, status);
        }else if(status == 1){
            String subString = pdate.substring(pdate.indexOf("-")+1);
            month = subString.substring(0,subString.indexOf("-"));
            books = adminService.summary(1, 10,month,status);
        }else {
            year = pdate.substring(0,pdate.indexOf("-"));
            books = adminService.summary(1,10,year,status);
        }
        System.out.println("books----"+books);
        if(books != null && books.size() > 0){
            return new Result(ResultCode.SUCCESS,"统计查询成功！",books);
        }
        return new Result(ResultCode.FAIL,"统计查询失败！");
    }


}
