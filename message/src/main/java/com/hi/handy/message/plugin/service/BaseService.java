package com.hi.handy.message.plugin.service;

import java.util.UUID;

public class BaseService {
    protected String getId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
