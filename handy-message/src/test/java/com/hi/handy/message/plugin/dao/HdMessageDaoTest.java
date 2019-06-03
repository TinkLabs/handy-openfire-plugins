package com.hi.handy.message.plugin.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.message.plugin.BaseTest;
import com.hi.handy.message.plugin.domain.message.HdMessageDao;
import com.hi.handy.message.plugin.domain.message.HdMessageEntity;
import org.junit.Test;

public class HdMessageDaoTest extends BaseTest {

    @Test
    public void save_Test() throws JsonProcessingException {
        HdMessageEntity hdMessageEntity= new HdMessageEntity();
        HdMessageDao.getInstance().save(hdMessageEntity);
    }
}
