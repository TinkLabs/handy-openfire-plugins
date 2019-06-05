package com.hi.handy.message.plugin.service;

import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyDao;
import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyEntity;
import com.hi.handy.message.plugin.domain.message.HdMessageDao;
import com.hi.handy.message.plugin.domain.message.HdMessageEntity;
import com.hi.handy.message.plugin.domain.roommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.message.plugin.domain.roommessagerecord.HdRoomMessageRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import java.util.UUID;

public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    public static final MessageService INSTANCE = new MessageService();

    private MessageService() {
    }

    public static MessageService getInstance() {
        return INSTANCE;
    }

    public void save(Packet packet) {
        LOGGER.info("MessagePlugin MessageService save start");
        try {
            Message message = (Message) packet;
            HdUserPropertyEntity hdUserProperty = HdUserPropertyDao.getInstance().searchByName(packet.getTo().getNode());
            LOGGER.info("MessagePlugin MessageService save hdUserProperty:"+hdUserProperty);
            if (hdUserProperty != null) {
                String id = UUID.randomUUID().toString();
                createMessage(packet, id,message.getID(), message.getElement().asXML(), hdUserProperty.getHotelId(), hdUserProperty.getHotelName(), hdUserProperty.getRoomNum());
                HdRoomMessageRecordEntity roomMessageRecord = HdRoomMessageRecordDao.getInstance().findByHotelIdAndNum(hdUserProperty.getHotelId(), hdUserProperty.getRoomNum());
                if (roomMessageRecord == null) {
                    createRoomMessageRecord(id, hdUserProperty.getHotelId(), hdUserProperty.getHotelName(), hdUserProperty.getRoomNum());
                } else {
                    roomMessageRecord.setMessageId(id);
                    updateRoomMessageRecord(roomMessageRecord);
                }
                LOGGER.info("MessagePlugin MessageService save success");
            } else {
                LOGGER.info(packet.getFrom().getNode() + "is not register");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("message save error", ex);
            LOGGER.debug("MessagePlugin MessageService save failed");
        }
    }

    private void createMessage(Packet packet,String id, String messageId, String stanza, Long hotelId, String hotelName, String roomNum) {
        LOGGER.info("MessagePlugin MessageService createMessage:");
        HdMessageEntity hdMessageEntity = new HdMessageEntity();
        hdMessageEntity.setId(id);
        hdMessageEntity.setMessageId(messageId);
        hdMessageEntity.setHotelId(hotelId);
        hdMessageEntity.setHotelName(hotelName);
        hdMessageEntity.setRoomNum(roomNum);
        hdMessageEntity.setFromUser(packet.getTo().getNode());
        hdMessageEntity.setFromJID(packet.getTo().toBareJID());
        hdMessageEntity.setToUser(packet.getFrom().getNode());
        hdMessageEntity.setToJID(packet.getFrom().toBareJID());
        hdMessageEntity.setCreationDate(new java.sql.Timestamp(System.currentTimeMillis()));
        hdMessageEntity.setStanza(stanza);
        HdMessageDao.getInstance().create(hdMessageEntity);
    }

    private void updateRoomMessageRecord(HdRoomMessageRecordEntity hdRoomMessageRecordEntity) {
        LOGGER.info("MessagePlugin MessageService updateRoomMessageRecord");
        hdRoomMessageRecordEntity.setAmount(hdRoomMessageRecordEntity.getAmount() + 1);
        hdRoomMessageRecordEntity.setUpdateDate(new java.sql.Timestamp(System.currentTimeMillis()));
        HdRoomMessageRecordDao.getInstance().updateByHotelIdAndNum(hdRoomMessageRecordEntity);
    }

    private void createRoomMessageRecord(String messageId, Long hotelId, String hotelName, String roomNum) {
        LOGGER.info("MessagePlugin MessageService createRoomMessageRecord");
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity = new HdRoomMessageRecordEntity();
        hdRoomMessageRecordEntity.setId(UUID.randomUUID().toString());
        hdRoomMessageRecordEntity.setHotelId(hotelId);
        hdRoomMessageRecordEntity.setHotelName(hotelName);
        hdRoomMessageRecordEntity.setRoomNum(roomNum);
        hdRoomMessageRecordEntity.setAmount(0l);
        hdRoomMessageRecordEntity.setMessageId(messageId);
        hdRoomMessageRecordEntity.setUpdateDate(new java.sql.Timestamp(System.currentTimeMillis()));
        HdRoomMessageRecordDao.getInstance().create(hdRoomMessageRecordEntity);
    }
}
