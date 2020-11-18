package com.library.utils;

import org.springframework.util.DigestUtils;

public class Md5Utils {

    public static String encode(String key){
        String encode = DigestUtils.md5DigestAsHex((key + "mpy").getBytes());
        return encode;
    }
}
