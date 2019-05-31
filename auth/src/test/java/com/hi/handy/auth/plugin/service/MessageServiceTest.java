package com.hi.handy.auth.plugin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.auth.plugin.BaseTest;
import com.hi.handy.auth.plugin.util.OutPrintUtils;
import com.hi.handy.auth.plugin.model.MessageModel;
import com.hi.handy.auth.plugin.parameter.MessageParameter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MessageServiceTest extends BaseTest {

    @Test
    public void list_Test() throws JsonProcessingException {
        MessageParameter messageParameter = new MessageParameter();
        messageParameter.setUserName("user3");
        messageParameter.setPageIndex(1);
        messageParameter.setPageSize(10);
        List<MessageModel> messageModels = MessageService.getInstance().list(messageParameter);
        Assert.assertNotNull(messageModels);
        OutPrintUtils.printlnJson(messageModels);
    }

}
