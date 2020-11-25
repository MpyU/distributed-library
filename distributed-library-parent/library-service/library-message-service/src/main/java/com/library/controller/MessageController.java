package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.library.dao.NoticeUserDao;
import com.library.pojo.Notice;
import com.library.pojo.NoticeUser;
import com.library.pojo.Result;
import com.library.pojo.ResultCode;
import com.library.service.MessageService;
import com.library.service.NoticeUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private NoticeUserService noticeUserService;



    @Autowired
    private RabbitTemplate rabbitTemplate;

    //http://10.10.102.162/admin/message/selectByMessage/{pageSize}/{currentPage}/{message}
//根据消息内容模糊查询
    @GetMapping("/selectByMessage/{pageSize}/{currentPage}/{message}")
    public Result<PageInfo<Notice>> selectByMessage(@PathVariable("pageSize") Integer pageSize, @PathVariable("currentPage") Integer currentPage,@PathVariable("message") String message) {
        System.out.println("pageSize:"+pageSize+" currentPage:"+currentPage+" message:"+message);
        List<Notice> list = messageService.selectByMessage(pageSize, currentPage, message);
        PageInfo pageInfo=new PageInfo(list);
        if(pageInfo.getList().size()>0){
            return new Result(ResultCode.SUCCESS,"查询成功",pageInfo);
        }
        return new Result(ResultCode.FAIL,"查询失败,无数据");
    }

    @PostMapping("/sendMsg")
    public Result<Notice> sendMessage(Integer uid,String message){
        int row = messageService.save(new Notice().setUid(uid).setMessage(message));
        String msg = "";
        if(uid == 0){
            rabbitTemplate.convertAndSend("","all_user_queue",message);
            msg = "消息已发送至所有用户！";
        }else{
            rabbitTemplate.convertAndSend("","user_queue",message);
            msg = "消息已发送至该用户！";
        }
        if(row > 0){
            return new Result(ResultCode.SUCCESS,msg,row);
        }
        return new Result(ResultCode.FAIL,"消息未发送成功！");
    }
   //传入用户id和公告id，插入一条已读记录
    @PostMapping("/setRead/{userId}/{noticeId}")
    public Result<Integer> setReadByUserId(@PathVariable("userId")Integer userId,@PathVariable("noticeId")Integer noticeId){
//        int result=noticeUserService.setReadByUserId(userId,noticeId);
        NoticeUser noticeUser=new NoticeUser(userId,noticeId,1);
        int result=noticeUserService.insert(noticeUser);
        return new Result(ResultCode.SUCCESS,"更新成功",result);

    }
    //查出所有未读消息，用户id
    @GetMapping("getUnReadMsgByUserId/{userId}")
    public Result<List<Notice>> unReadMsg(@PathVariable("userId")Integer userId){
        List<Notice> list=noticeUserService.selectunReadMsgByUserId(userId);
        System.out.println("list:"+list);
        return new Result(ResultCode.SUCCESS,"查询未读消息成功",list);
    }

    @PostMapping("/save")
    public Result<Integer> save(@RequestBody Notice notice){
        int row = messageService.save(notice);

        if(row > 0){
            return new Result(ResultCode.SUCCESS,"发布消息成功！",row);
        }
        return new Result(ResultCode.FAIL,"发布消息失败！",row);
    }

    @GetMapping("/get/{id}")
    public Result<Notice> get(@PathVariable("id")Integer id){
        Notice notice = messageService.get(id);
        if(notice != null){
            return new Result(ResultCode.SUCCESS,"查询消息成功！",notice);
        }
        return new Result(ResultCode.FAIL,"查询消息失败！");
    }

    @GetMapping("/getByUid/{uid}/{pageSize}/{currentPage}")
    public Result<Notice> getByUid(@PathVariable("uid")Integer uid,@PathVariable("pageSize")Integer pageSize,
                                   @PathVariable("currentPage")Integer currentPage){
        if(pageSize == null){
            pageSize = 5;
        }
        if(currentPage == null){
            currentPage = 1;
        }
        PageInfo<Notice> pageInfo = messageService.getNoticeByUid(uid,currentPage,pageSize);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"查询指定用户消息成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"查询消息失败！");
    }

    @GetMapping("/readMsg/{id}/{uid}")
    public Result<Integer> readMsg(@PathVariable("id")Integer id,@PathVariable("uid")Integer uid){
        int row = messageService.update(new Notice().setId(id).setUid(uid));
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"消息已阅读",row);
        }
        return new Result(ResultCode.FAIL,"消息阅读状态更新失败！");
    }

    @GetMapping("/select/{pageSize}/{currentPage}")
    public Result<PageInfo<Notice>> select(@PathVariable("pageSize")Integer pageSize,
                                           @PathVariable("currentPage")Integer currentPage){
        PageInfo<Notice> pageInfo = messageService.selectAll(currentPage, pageSize);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"查询所有消息成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"查询所有消息失败！");
    }

    @DeleteMapping("delete/{id}")
    public Result<Integer> delete(@PathVariable("id")Integer id){
        int row = messageService.delete(id);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"删除消息成功！",row);
        }
        return new Result(ResultCode.FAIL,"删除消息失败！");

    }

    @PutMapping("/update")
    public Result<Integer> update(@RequestBody Notice notice){
        int row = messageService.update(notice);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"修改消息成功！",row);
        }
        return new Result(ResultCode.FAIL,"修改消息失败！");
    }

}
