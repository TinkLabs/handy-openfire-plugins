package com.hi.handy.message.plugin.test.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.message.plugin.domain.roommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.message.plugin.domain.roommessagerecord.HdRoomMessageRecordEntity;
import com.hi.handy.message.plugin.test.BaseTest;
import com.hi.handy.message.plugin.test.util.OutPrintUtils;
import org.junit.Test;

import java.util.UUID;

public class HdRoomMessageRecordDaoTest extends BaseTest {

    @Test
    public void create_Test() {
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity = new HdRoomMessageRecordEntity();
        hdRoomMessageRecordEntity.setId(UUID.randomUUID().toString());
        hdRoomMessageRecordEntity.setHotelId(1l);
        hdRoomMessageRecordEntity.setHotelName("酒店1");
        hdRoomMessageRecordEntity.setRoomNum("301");
        hdRoomMessageRecordEntity.setAmount(0l);
        hdRoomMessageRecordEntity.setMessageId("ewr234523eqwerq");
        hdRoomMessageRecordEntity.setUpdateDate(new java.sql.Timestamp(System.currentTimeMillis()));
        HdRoomMessageRecordDao.getInstance().create(hdRoomMessageRecordEntity);
    }

    @Test
    public void findByHotelIdAndNum_Test() throws JsonProcessingException {
        Long hotelId =1l;
        String roomNum="301";
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity=HdRoomMessageRecordDao.getInstance().findByHotelIdAndNum(hotelId,roomNum);
        OutPrintUtils.printlnJson(hdRoomMessageRecordEntity);
    }

    @Test
    public void updateByHotelIdAndNum_Test() throws JsonProcessingException {
        Long hotelId =1l;
        String roomNum="301";
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity=HdRoomMessageRecordDao.getInstance().findByHotelIdAndNum(hotelId,roomNum);
        hdRoomMessageRecordEntity.setAmount(hdRoomMessageRecordEntity.getAmount() + 1);
        hdRoomMessageRecordEntity.setMessageId("43524352");
        hdRoomMessageRecordEntity.setUpdateDate(new java.sql.Timestamp(System.currentTimeMillis()));
        HdRoomMessageRecordDao.getInstance().updateByHotelIdAndNum(hdRoomMessageRecordEntity);
    }
}
