package com.hi.handy.message.plugin.test.dao;

import com.hi.handy.message.plugin.domain.message.HdMessageDao;
import com.hi.handy.message.plugin.domain.message.HdMessageEntity;
import com.hi.handy.message.plugin.test.BaseTest;
import org.junit.Test;

public class HdMessageDaoTest extends BaseTest {

    @Test
    public void create_Test() {
        HdMessageEntity hdMessageEntity = new HdMessageEntity();
        hdMessageEntity.setId("ewr234523eqwerq");
        hdMessageEntity.setMessageId("43324");
        hdMessageEntity.setHotelId(2L);
        hdMessageEntity.setHotelName("酒店2");
        hdMessageEntity.setRoomNum("301");
        hdMessageEntity.setFromUser("user1");
        hdMessageEntity.setFromJID("dfas");
        hdMessageEntity.setToUser("user2");
        hdMessageEntity.setToJID("fadsfas");
        hdMessageEntity.setCreationDate(new java.sql.Timestamp(System.currentTimeMillis()));
        hdMessageEntity.setStanza("dsfasdfas");
        HdMessageDao.getInstance().create(hdMessageEntity);
    }
}
