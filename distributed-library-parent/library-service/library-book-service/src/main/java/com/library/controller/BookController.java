package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Book;
import com.library.pojo.Result;
import com.library.pojo.ResultCode;
import com.library.pojo.UserBook;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/book")
@CrossOrigin
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/get/{id}")
    public Result<Book> get(@PathVariable("id")Integer id){
        Book book = bookService.get(id);
        Book b = new Book();
        if(book != null && book.getDayClickCount() == null){
            book.setDayClickCount(0);

        }
        if(book != null && book.getMonthClickCount() == null){
            book.setMonthClickCount(0);

        }
        if(book != null && book.getTotalClickCount() == null){
            book.setTotalClickCount(0);

        }
        if(book != null){
            b.setDayClickCount(book.getDayClickCount()+1);
            b.setMonthClickCount(book.getMonthClickCount()+1);
            b.setTotalClickCount(book.getTotalClickCount()+1);
            b.setId(book.getId());
            bookService.update(b);
        }
        if(book != null){
            return new Result(ResultCode.SUCCESS,"查询图书信息成功！",book);
        }
        return new Result(ResultCode.FAIL,"查询图书信息失败！");
    }

    /**
     * 查询所有图书信息
     * @param pageSize
     * @param currentPage
     * @return
     */
    @GetMapping("/select/{pageSize}/{currentPage}")
    public Result<PageInfo<Book>> select(@PathVariable("pageSize")Integer pageSize,
                                         @PathVariable("currentPage")Integer currentPage){
        if(pageSize==null){
            pageSize=5;
        }
        if(currentPage==null){
            currentPage=1;
        }
        PageInfo<Book> pageInfo = bookService.selectAll(currentPage, pageSize);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"查询所有图书信息成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"查询所有图书信息失败！");
    }

//    @GetMapping("/selectAllByCondition/{pageSize}/{currentPage}")
//    public Result<PageInfo<Book>> selectAllByCondition(@PathVariable("pageSize")Integer pageSize,
//                                                       @PathVariable("pageSize")Integer currentPage,
//                                                       Book book){
//        if(pageSize == null){
//            pageSize = 5;
//        }
//        if(currentPage == null){
//            currentPage = 1;
//        }
//        System.out.println(book+"-------------");
//        PageInfo<Book> pageInfo = bookService.selectAllByCondition(currentPage, pageSize, book);
//        if(pageInfo.getList().size() > 0){
//            return new Result(ResultCode.SUCCESS,"查询所有图书信息成功！",pageInfo);
//        }
//        return new Result(ResultCode.FAIL,"查询所有图书信息失败！");
//    }

    @PostMapping("/save")
    public Result<Integer> save(@RequestBody Book book){
        int row = bookService.save(book);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"添加图书信息成功！",row);
        }
        return new Result(ResultCode.FAIL,"添加图书信息失败！");
    }

    @PutMapping("/update")
    public Result<Integer> update(@RequestBody Book book){
        int row = bookService.update(book);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"修改图书信息成功！",row);
        }
        return new Result(ResultCode.FAIL,"修改图书信息失败！");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Integer> delete(@PathVariable("id")Integer id){
        int row = bookService.delete(id);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"删除图书成功！",row);
        }
        return new Result(ResultCode.FAIL,"删除图书失败！");
    }

        @GetMapping("/selectByCondition/{pageSize}/{currentPage}")
    public Result<PageInfo> selectByCondition(@PathVariable("pageSize")Integer pageSize,
                                              @PathVariable("currentPage")Integer currentPage,String keyword){

        PageInfo<Book> pageInfo = bookService.selectAllByCondition(currentPage, pageSize, keyword);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"模糊查询图书成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"模糊图书查询失败！");
    }

    @GetMapping("/categoryBookList/{categryId}/{pageSize}/{currentPage}")
    public Result<PageInfo<Book>> categoryBookList(@PathVariable("categryId")Integer categoryId,@PathVariable("pageSize") Integer pageSize,@PathVariable("currentPage")Integer currentPage){
        PageInfo<Book> pageInfo=bookService.categoryBookList(categoryId,pageSize,currentPage);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"分类查询图书成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"分类查询图书失败！");
    }
//    @PutMapping("/update/count/{id}")
//    public Result<Integer> updateCount(@PathVariable("id")Integer id){
//        Book book = bookService.get(id);
//        Book b = new Book();
//        b.setDayClickCount(book.getDayClickCount()+1);
//        b.setMonthClickCount(book.getMonthClickCount()+1);
//        b.setTotalClickCount(book.getTotalClickCount()+1);
//        b.setId(book.getId());
//        int row = bookService.update(b);
//        if(row > 0){
//            return  new Result(ResultCode.SUCCESS,"更新成功！",row);
//        }
//        return  new Result(ResultCode.SUCCESS,"更新失败！");
//    }

    @GetMapping("/guess/{uid}")
    public Result<PageInfo<UserBook>> guess(@PathVariable("uid")Integer uid){

        PageInfo<Book> pageInfo = bookService.guess(uid);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"推荐书籍成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"推荐书籍失败！");
    }
    @GetMapping("/guess/")
    public Result<PageInfo<UserBook>> guess(){

        PageInfo<Book> pageInfo = bookService.guess();
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"推荐书籍成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"推荐书籍失败！");
    }


}
