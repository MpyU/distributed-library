package com.library.utils;

import com.google.gson.Gson;
import com.library.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class ConvertJsonToBean<T> {

    public static<T> T convertMapToBean(Map<String,Object> map,Class clazz){
        Gson gson = new Gson();
        String str = gson.toJson(map);
        return (T)gson.fromJson(str, clazz);
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("username","zhangsan");
        map.put("password","123");
        User user = ConvertJsonToBean.convertMapToBean(map, User.class);
        System.out.println(user);
    }
}
