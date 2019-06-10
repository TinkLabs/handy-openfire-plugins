package com.hi.handy.message.plugin.test.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyDao;
import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyEntity;
import com.hi.handy.message.plugin.test.BaseTest;
import com.hi.handy.message.plugin.test.util.OutPrintUtils;
import org.junit.Assert;
import org.junit.Test;

public class HdUserPropertyDaoTest extends BaseTest {

    @Test
    public void searchByName_Test() throws JsonProcessingException {
        HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByName("user1");
        Assert.assertNotNull(hdUserPropertyEntity);
        OutPrintUtils.printlnJson(hdUserPropertyEntity);
    }
}
