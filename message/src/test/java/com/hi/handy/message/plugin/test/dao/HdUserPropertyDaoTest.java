package com.hi.handy.message.plugin.test.dao;

import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyDao;
import com.hi.handy.message.plugin.test.BaseTest;
import org.junit.Test;

public class HdUserPropertyDaoTest extends BaseTest {

    @Test
    public void Test() {
        HdUserPropertyDao.getInstance().countAgentByUserName("guest-tinklabs.com");
    }
}
