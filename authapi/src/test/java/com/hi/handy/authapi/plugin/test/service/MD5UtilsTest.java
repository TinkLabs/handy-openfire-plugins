package com.hi.handy.authapi.plugin.test.service;

import com.hi.handy.authapi.plugin.utils.MD5Utils;
import org.junit.Test;

public class MD5UtilsTest {
    @Test
    public void guestLogin_Test() {
        System.out.println(MD5Utils.MD5Encode("12345678","utf8"));
        System.out.println(MD5Utils.MD5Encode("4352346234523","utf8"));
    }
}
