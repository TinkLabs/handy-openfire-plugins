package com.hi.handy.group.plugin.utils;

public class Base64Utils {
    public static String encode(byte[] content){
        return new sun.misc.BASE64Encoder().encode(content);
    }
}
