package com.hi.handy.group.plugin.service;

import com.hi.handy.group.plugin.utils.Base64Utils;
import com.hi.handy.group.plugin.utils.MD5Utils;
import org.jivesoftware.openfire.XMPPServer;

import java.util.UUID;

public class BaseService {
    protected static final String AT_SYMBOL = "@";
    protected static final String LINE_THROUGH = "-";

    protected String md5EncodePassword(String code) {
        return MD5Utils.MD5Encode(code,"utf8");
    }

    protected String encodePassword(String password){
        return Base64Utils.encode(password.getBytes());
    }

    protected String getDomain(){
        return XMPPServer.getInstance().getServerInfo().getXMPPDomain();
    }

    protected String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
