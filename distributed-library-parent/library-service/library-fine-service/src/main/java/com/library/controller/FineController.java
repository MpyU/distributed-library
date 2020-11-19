package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Fine;
import com.library.pojo.Result;
import com.library.pojo.ResultCode;
import com.library.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/fine")
public class FineController {
    
    @Autowired
    private FineService fineService;

    @GetMapping("/get/{id}")
    public Result<Fine> get(@PathVariable("id")Integer id){

        Fine fine = fineService.get(id);
        if(fine != null){
            return new Result(ResultCode.SUCCESS,"查询罚款信息成功！",fine);
        }
        return new Result(ResultCode.FAIL,"查询罚款信息失败！");
    }

    @GetMapping("/select/{pageSize}/{currentPage}")
    public Result<PageInfo<Fine>> select(@PathVariable(value="pageSize")Integer pageSize,
                                   @PathVariable("currentPage")Integer currentPage){
        if(pageSize == null){
            pageSize = 5;
        }
        if(currentPage == null){
            currentPage = 1;
        }
        PageInfo<Fine> pageInfo = fineService.selectAll(currentPage, pageSize);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"查询所有罚款信息成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"查询所有罚款信息失败！");
    }

    @PostMapping("/save")
    public Result<Integer> save(Fine fine){
        int row = fineService.save(fine);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"添加罚款成功！",row);
        }
        return new Result(ResultCode.SUCCESS,"添加罚款失败！");
    }

    @PutMapping("/update")
    public Result<Integer> update(Fine fine){
        int row = fineService.update(fine);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"修改罚款信息成功！",row);
        }
        return new Result(ResultCode.SUCCESS,"修改罚款信息失败！");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Integer> delete(@PathVariable("id")Integer id){
        int row = fineService.delete(id);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"删除罚款成功！",row);
        }
        return new Result(ResultCode.SUCCESS,"删除罚款失败！");
    }
    
}
