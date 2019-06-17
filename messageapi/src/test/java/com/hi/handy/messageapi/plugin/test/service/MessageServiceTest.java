package com.hi.handy.messageapi.plugin.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.messageapi.plugin.parameter.MessageParameter;
import com.hi.handy.messageapi.plugin.service.MessageService;
import com.hi.handy.messageapi.plugin.test.BaseTest;
import com.hi.handy.messageapi.plugin.test.util.OutPrintUtils;
import org.junit.Test;

public class MessageServiceTest extends BaseTest {

    @Test
    public void Test() throws JsonProcessingException {
        MessageParameter messageParameter=new MessageParameter();
        messageParameter.setUserName("agent-tinklabs.com");
        messageParameter.setPageIndex(0);
        messageParameter.setPageSize(10);
        Object object = MessageService.getInstance().list(messageParameter);
        OutPrintUtils.printlnJson(object);
    }
}

