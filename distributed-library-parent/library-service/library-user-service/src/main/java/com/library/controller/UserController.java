package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.library.pojo.*;
import com.library.service.UserService;
import com.library.utils.JwtUtils;
import com.library.utils.Md5Utils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/save")
    public Result<Integer> save(@RequestBody User user){
        int row = userService.save(user);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"新增用户成功！",row);
        }
        return new Result(ResultCode.FAIL,"新增用户失败！");
    }

    @PostMapping("/rsave")
    public Result<Integer> rsave(User user){
        int row = userService.save(user);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"新增用户成功！",row);
        }
        return new Result(ResultCode.FAIL,"新增用户失败！");
    }

    @PostMapping("/register")
    public Result<Integer> register(User user){
        int row = userService.save(user);
        if(row > 0){
            UserEmail userEmail = new UserEmail();
            userEmail.setFrom("mpy_library@163.com").setTo(user.getEmail())
                    .setText("恭喜您成功注册PinyLib图书馆，点击链接可直接跳转首页: http://10.10.102.166:8080/")
                    .setSubject("PinyLib图书馆注册信息");
            rabbitTemplate.convertAndSend("","email_queue",userEmail);
            return new Result(ResultCode.SUCCESS,"注册成功！",row);
        }
        return new Result(ResultCode.FAIL,"注册失败！");
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody String userStr){
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        User u = userService.getByPWD(user);
        List<Object> list = new ArrayList<>();
        if(u != null){
            String token = jwtUtils.generateToken(u);
            list.add(u.getId());
            list.add(token);
            list.add(u.getStatus());
            redisTemplate.opsForValue().set("token",token, 3600,TimeUnit.SECONDS);
            Object token1 = redisTemplate.opsForValue().get("token");
            return new Result(ResultCode.SUCCESS,"登录成功！",list);
        }else{
            return new Result(ResultCode.FAIL,"用户名或密码有误！");
        }
    }



    @GetMapping("/get/{id}")
    public Result<User> get(@PathVariable("id")Integer id){

        User u = userService.get(new User(id));
        if(u != null){
            return new Result(ResultCode.SUCCESS,"查询用户成功！",u);
        }
        return new Result(ResultCode.FAIL,"无此用户！");
    }

    @GetMapping("/getUser")
    public Result<User> getUser(String email){
        User u = userService.getUser(new User().setEmail(email));
        if(u != null){
            return new Result(ResultCode.SUCCESS,"查询用户成功！",u);
        }
        return new Result(ResultCode.FAIL,"无此用户！");
    }

    @GetMapping("/getUserByUsername")
    public Result<User> getUserByUsername(String username){
        User u = userService.getUserByUsername(new User().setUsername(username));
        if(u != null){
            return new Result(ResultCode.SUCCESS,"查询用户成功！",u);
        }
        return new Result(ResultCode.FAIL,"无此用户！");
    }

    @GetMapping("/select/{pageSize}/{currentCount}")
    public Result<PageInfo<User>> select(@PathVariable("pageSize")Integer pageSize
        ,@PathVariable("currentCount")Integer currentPage){
        if(pageSize == null){
            pageSize = 5;
        }
        if(currentPage == null){
            currentPage = 1;
        }
        PageInfo<User> pageInfo = userService.selectAll(pageSize,currentPage);
        if(pageInfo.getList().size() > 0){
            return new Result(ResultCode.SUCCESS,"查询所有用户成功！",pageInfo);
        }
        return new Result(ResultCode.FAIL,"查询所有用户失败！");
    }


    @PutMapping("/update")
    public Result<Integer> update(@RequestBody User user){
        int row = userService.update(user);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"用户信息修改成功！",row);
        }else{
            return new Result(ResultCode.FAIL,"用户信息修改失败！");
        }
    }

    @PutMapping("/rupdate")
    public Result<Integer> rupdate(User user){
        System.out.println("rupdate....."+user);
        int row = userService.update(user);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"用户信息修改成功！",row);
        }else{
            return new Result(ResultCode.FAIL,"用户信息修改失败！");
        }
    }




    @DeleteMapping("/delete/{id}")
    public Result<Integer> delete(@PathVariable("id") Integer id){
        int row = userService.delete(id);
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"用户删除成功！",row);
        }else{
            return new Result(ResultCode.FAIL,"用户删除失败！");
        }
    }

    @PutMapping("/changePwd")
    public Result<Integer> changePwd(ChangePwdForm form){
        User user = userService.get(new User().setEmail(form.getEmail()));
        int row = 0;
        String oldCode = (String) redisTemplate.opsForValue().get("code:"+form.getEmail());
        if(form.getCode().equals(oldCode)){
            String encode = Md5Utils.encode(form.getPassword());
            row = userService.update(user.setPassword(encode));
        }else{
            return new Result(ResultCode.FAIL,"验证码不正确或已失效，请重新获取");
        }
        if(row > 0){
            return new Result(ResultCode.SUCCESS,"修改密码成功！",row);
        }
        return new Result(ResultCode.FAIL,"修改密码失败！",row);
    }

}
