package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.library.pojo.Result;
import com.library.pojo.ResultCode;
import com.library.pojo.User;
import com.library.service.UserService;
import com.library.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private RestTemplate restTemplate;


    @PostMapping("/save")
    public Result<Integer> save(User user){
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
            Map<String,String> map = new HashMap<>();
            map.put("email",user.getEmail());
            Result<Integer> result = restTemplate.getForObject("http://EMAIL-SERVICE/email/send?email={email}",Result.class,map);
            System.out.println(result.getMessage());
            return new Result(ResultCode.SUCCESS,"注册成功！",row);
        }
        return new Result(ResultCode.FAIL,"注册失败！");
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody String userStr){
        System.out.println(userStr);
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        System.out.println(user);
        User u = userService.getByPWD(user);
        System.out.println("------"+u);
        List<Object> list = new ArrayList<>();
        if(u != null){
            String token = jwtUtils.generateToken(u);
            list.add(u.getId());
            list.add(token);
            redisTemplate.opsForValue().set("token",token, 3600,TimeUnit.SECONDS);
            Object token1 = redisTemplate.opsForValue().get("token");
            System.out.println(token1);
            return new Result(ResultCode.SUCCESS,"登录成功！",token);
        }else{
            return new Result(ResultCode.FAIL,"用户名或密码有误！");
        }
    }


    @GetMapping("/get/{id}")
    public Result<User> get(@PathVariable("id")Integer id){
        User u = userService.get(new User(id));
        System.out.println(u);
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
    public Result<Integer> update(User user){
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

}
