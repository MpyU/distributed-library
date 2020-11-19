package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Book;
import com.library.pojo.Result;
import com.library.pojo.ResultCode;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/get/{id}")
    public Result<Book> get(@PathVariable("id")Integer id){
        Book book = bookService.get(id);
        System.out.println(book);
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
    /**
     * 按条件查询所有图书信息
     * @param pageSize
     * @param currentPage
     * @param book
     * @return
     */
    @GetMapping("/selectAllByCondition/{pageSize}/{currentPage}")
    public Result<PageInfo<Book>> selectAllByCondition(@PathVariable("pageSize")Integer pageSize,
                                                       @PathVariable("pageSize")Integer currentPage,
                                                       Book book){
        if(pageSize == null){
            pageSize = 5;
        }
        if(currentPage == null){
            currentPage = 1;
        }
        PageInfo<Book> pageInfo = bookService.selectAllByCondition(currentPage, pageSize, book);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"查询所有图书信息成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"查询所有图书信息失败！");
    }

    @PostMapping("/save")
    public Result<Integer> save(Book book){
        int row = bookService.save(book);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"添加图书信息成功！",row);
        }
        return new Result(ResultCode.FAIL,"添加图书信息失败！");
    }

    @PutMapping("/update")
    public Result<Integer> update(Book book){
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
}
