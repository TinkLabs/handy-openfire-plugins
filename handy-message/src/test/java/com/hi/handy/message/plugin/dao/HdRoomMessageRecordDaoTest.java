package com.hi.handy.message.plugin.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.message.plugin.BaseTest;
import com.hi.handy.message.plugin.domain.roommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.message.plugin.domain.roommessagerecord.HdRoomMessageRecordEntity;
import com.hi.handy.message.plugin.util.OutPrintUtils;
import org.junit.Test;

public class HdRoomMessageRecordDaoTest extends BaseTest {

    @Test
    public void create_Test() throws JsonProcessingException {
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity= new HdRoomMessageRecordEntity();
        HdRoomMessageRecordDao.getInstance().create(hdRoomMessageRecordEntity);
    }

    @Test
    public void findByHotelIdAndNum_Test() throws JsonProcessingException {
        Long hotelId =0l;
        String roomNum="";
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity=HdRoomMessageRecordDao.getInstance().findByHotelIdAndNum(hotelId,roomNum);
        OutPrintUtils.printlnJson(hdRoomMessageRecordEntity);
    }

    @Test
    public void updateByHotelIdAndNum_Test() throws JsonProcessingException {
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity= new HdRoomMessageRecordEntity();
        HdRoomMessageRecordDao.getInstance().updateByHotelIdAndNum(hdRoomMessageRecordEntity);
    }
}
