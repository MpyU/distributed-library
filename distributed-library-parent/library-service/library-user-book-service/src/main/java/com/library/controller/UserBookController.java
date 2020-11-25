package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Result;
import com.library.pojo.ResultCode;
import com.library.pojo.UserBook;
import com.library.service.UserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/userBook")
public class UserBookController {
    
    @Autowired
    private UserBookService userBookService;

    @GetMapping("/get/{id}")
    public Result<UserBook> get(@PathVariable("id")Integer id){

        UserBook userBook = userBookService.get(id);
        if(userBook != null){
            return new Result(ResultCode.SUCCESS,"查询借阅历史信息成功！",userBook);
        }
        return new Result(ResultCode.FAIL,"查询借阅历史信息失败！");
    }

    @GetMapping("/getByUid/{pageSize}/{currentPage}/{uid}")
    public Result<UserBook> getByUid(@PathVariable("pageSize")Integer pageSize,
                                     @PathVariable("currentPage")Integer currentPage,
                                     @PathVariable("uid")Integer uid){

        PageInfo<UserBook> userBooks = userBookService.getByUid(currentPage, pageSize, uid);
        if(userBooks.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"查询用户借阅历史信息成功！",userBooks);
        }
        return new Result(ResultCode.FAIL,"查询用户借阅历史信息失败！");
    }

    @GetMapping("/select/{pageSize}/{currentPage}")
    public Result<PageInfo<UserBook>> select(@PathVariable(value="pageSize")Integer pageSize,
                                         @PathVariable("currentPage")Integer currentPage){
        if(pageSize == null){
            pageSize = 5;
        }
        if(currentPage == null){
            currentPage = 1;
        }
        PageInfo<UserBook> pageInfo = userBookService.selectAll(currentPage, pageSize);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"查询所有节约历史信息成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"查询所有节约历史信息失败！");
    }

    @PostMapping("/save")
    public Result<Integer> save(@RequestBody UserBook userBook){
        int row = userBookService.save(userBook);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"添加节约历史成功！",row);
        }
        return new Result(ResultCode.SUCCESS,"添加节约历史失败！");
    }

    @PutMapping("/update")
    public Result<Integer> update(@RequestBody UserBook userBook){
        int row = userBookService.update(userBook);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"修改节约历史信息成功！",row);
        }
        return new Result(ResultCode.SUCCESS,"修改节约历史信息失败！");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Integer> delete(@PathVariable("id")Integer id){
        int row = userBookService.delete(id);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"删除节约历史成功！",row);
        }
        return new Result(ResultCode.SUCCESS,"删除节约历史失败！");
    }

    @GetMapping("/lend/{userId}/{bookId}")
    public Result<Integer> lend(@PathVariable("userId")Integer userId,@PathVariable("bookId")Integer bookId){
        int row = userBookService.lendBook(userId,bookId);
        if(row == -1){
            return new Result(ResultCode.FAIL,"此书可借数量为0");
        }else if(row == Integer.MAX_VALUE){
            return new Result(ResultCode.FAIL,"该用户已达借书上限",row);
        }
        return  new Result(ResultCode.SUCCESS,"借书成功！",row);
    }

    @GetMapping("/return/{id}")
    public Result<Integer> returnBook(@PathVariable("id")Integer id){

        int row = userBookService.returnBook(id);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"还书成功！",row);
        }
        return new Result(ResultCode.SUCCESS,"还书失败！");
    }
    @GetMapping("/selectOneByUidAndBookId/{uid}/{bid}")
    //查询出该用户借的这本书的一条记录，从而得到这条记录的id，然后根据id还书
    public Result<UserBook> selectOneByUidAndBookId(@PathVariable("uid")Integer uid,@PathVariable("bid")Integer bid){
        UserBook userBook=userBookService.selectOneByUidAndBookId(uid,bid);
        if(userBook!=null){
            return new Result<>(ResultCode.SUCCESS,"查询成功",userBook);
        }
        return new Result<>(ResultCode.FAIL,"查询失败");

    }


}
