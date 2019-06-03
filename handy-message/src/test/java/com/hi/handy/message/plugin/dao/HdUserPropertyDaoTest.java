package com.hi.handy.message.plugin.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.message.plugin.BaseTest;
import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyDao;
import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyEntity;
import com.hi.handy.message.plugin.util.OutPrintUtils;
import org.junit.Assert;
import org.junit.Test;

public class HdUserPropertyDaoTest extends BaseTest {

    @Test
    public void save_Test() throws JsonProcessingException {

        HdUserPropertyDao.getInstance().searchByName("");
        HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByName("");
        Assert.assertNotNull(hdUserPropertyEntity);
        OutPrintUtils.printlnJson(hdUserPropertyEntity);
    }
}
