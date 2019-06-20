package com.hi.handy.authapi.plugin.utils;

public class Base64Utils {

    public static String encode(byte[] content){
        return new sun.misc.BASE64Encoder().encode(content);
    }

}
