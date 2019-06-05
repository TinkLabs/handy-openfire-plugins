package com.hi.handy.messageapi.plugin.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.messageapi.plugin.model.MessageHistoryModel;
import com.hi.handy.messageapi.plugin.model.MessageModel;
import com.hi.handy.messageapi.plugin.parameter.BaseParameter;
import com.hi.handy.messageapi.plugin.parameter.MessageParameter;
import com.hi.handy.messageapi.plugin.service.MessageService;
import com.hi.handy.messageapi.plugin.test.BaseTest;
import com.hi.handy.messageapi.plugin.test.util.OutPrintUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MessageServiceTest extends BaseTest {

    @Test
    public void chatListByPaging_Test() throws JsonProcessingException {
        MessageParameter messageParameter = new MessageParameter();
        messageParameter.setUserName("user1");
        messageParameter.setPageIndex(1);
        messageParameter.setPageSize(10);
        List<MessageModel> messageModelList = MessageService.getInstance().chatListByPaging(messageParameter);
        Assert.assertNotNull(messageModelList);
        OutPrintUtils.printlnJson(messageModelList);
    }

    @Test
    public void historyList_Test() throws JsonProcessingException {
        MessageParameter messageParameter = new MessageParameter();
        messageParameter.setUserName("user1");
        messageParameter.setPageIndex(1);
        messageParameter.setPageSize(10);
        MessageHistoryModel messageHistoryModel = MessageService.getInstance().historyList(messageParameter);
        Assert.assertNotNull(messageHistoryModel);
        OutPrintUtils.printlnJson(messageHistoryModel);
    }

    @Test
    public void list_historyList_Test() throws JsonProcessingException {
        MessageParameter messageParameter = new MessageParameter();
        messageParameter.setUserName("user1");
        messageParameter.setPageIndex(1);
        messageParameter.setPageSize(10);
        messageParameter.setMessageType(BaseParameter.MessageType.HISTORY);
        Object results = MessageService.getInstance().list(messageParameter);
        Assert.assertNotNull(results);
        OutPrintUtils.printlnJson(results);
    }

    @Test
    public void list_chatListByPaging_Test() throws JsonProcessingException {
        MessageParameter messageParameter = new MessageParameter();
        messageParameter.setUserName("user1");
        messageParameter.setPageIndex(1);
        messageParameter.setPageSize(10);
        messageParameter.setMessageType(BaseParameter.MessageType.LIST);
        Object results = MessageService.getInstance().list(messageParameter);
        Assert.assertNotNull(results);
        OutPrintUtils.printlnJson(results);
    }
}

