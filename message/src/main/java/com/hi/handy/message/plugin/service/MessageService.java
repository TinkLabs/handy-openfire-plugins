package com.hi.handy.message.plugin.service;

import com.hi.handy.message.plugin.domain.hdmessage.HdMessageDao;
import com.hi.handy.message.plugin.domain.hdmessage.HdMessageEntity;
import com.hi.handy.message.plugin.domain.hdroommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.message.plugin.domain.hdroommessagerecord.HdRoomMessageRecordEntity;
import org.apache.commons.lang3.StringUtils;
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
        LOGGER.info("save");
        LOGGER.info("packet",packet);
        try {
            Message message = (Message) packet;
            String roomType = getRoomeType(packet.getFrom().getNode());
            if(StringUtils.isNoneBlank(roomType) && ("room-vip".equals(roomType)||"room-hotel".equals(roomType))) {
                String[] roomInfoArray = packet.getFrom().getNode().split("#");
                String[] getRoomInfoDetailArray = roomInfoArray[1].split("-");
                Long zoneId = Long.valueOf(getRoomInfoDetailArray[0]);
                Long hotelId = Long.valueOf(getRoomInfoDetailArray[1]);
                String hotelName = getRoomInfoDetailArray[2];
                String roomNum = getRoomInfoDetailArray[3];

                String deviceUserId = roomInfoArray[2];
                String roomName = packet.getFrom().getNode();

                createOrUpdateMessage(packet, message.getID(), message.getElement().asXML(), zoneId, hotelId, hotelName, roomNum, deviceUserId);
                HdRoomMessageRecordEntity roomMessageRecord = HdRoomMessageRecordDao.getInstance().findByRoomName(roomName);
                if (roomMessageRecord == null) {
                    createRoomMessageRecord(roomName);
                } else {
                    updateRoomMessageRecord(roomMessageRecord);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createOrUpdateMessage(Packet packet,String messageId,String stanza,Long zoneId,Long hotelId,String hotelName,String roomNum, String deviceUserId) {
        LOGGER.info("createOrUpdateMessage");
        String systemId = HdMessageDao.getInstance().findBytoJID(packet.getFrom().toBareJID());
        if(StringUtils.isNoneBlank(systemId)){
            HdMessageDao.getInstance().updateStanzaById(systemId,stanza,new java.sql.Timestamp(System.currentTimeMillis()));
        }else{
            HdMessageEntity hdMessageEntity = new HdMessageEntity();
            hdMessageEntity.setId(UUID.randomUUID().toString());
            hdMessageEntity.setMessageId(messageId);
            hdMessageEntity.setZoneId(zoneId);
            hdMessageEntity.setHotelId(hotelId);
            hdMessageEntity.setHotelName(hotelName);
            hdMessageEntity.setRoomNum(roomNum);
            hdMessageEntity.setDeviceUserId(deviceUserId);
            hdMessageEntity.setFromUser(packet.getTo().getNode());
            hdMessageEntity.setFromJID(packet.getTo().toBareJID());
            hdMessageEntity.setToUser(packet.getFrom().getNode());
            hdMessageEntity.setToJID(packet.getFrom().toBareJID());
            hdMessageEntity.setCreationDate(new java.sql.Timestamp(System.currentTimeMillis()));
            hdMessageEntity.setStanza(stanza);
            HdMessageDao.getInstance().create(hdMessageEntity);
        }

    }

    private void updateRoomMessageRecord(HdRoomMessageRecordEntity hdRoomMessageRecordEntity) {
        LOGGER.info("updateRoomMessageRecord");
        HdRoomMessageRecordDao.getInstance().updateAmountAndUpdateDateById(hdRoomMessageRecordEntity.getId(),hdRoomMessageRecordEntity.getAmount()+1,new java.sql.Timestamp(System.currentTimeMillis()));
    }

    private void createRoomMessageRecord(String roomName) {
        LOGGER.info("createRoomMessageRecord");
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity = new HdRoomMessageRecordEntity();
        hdRoomMessageRecordEntity.setId(UUID.randomUUID().toString());
        hdRoomMessageRecordEntity.setRoomName(roomName);
        hdRoomMessageRecordEntity.setAmount(1l);
        hdRoomMessageRecordEntity.setUpdateDate(new java.sql.Timestamp(System.currentTimeMillis()));
        HdRoomMessageRecordDao.getInstance().create(hdRoomMessageRecordEntity);
    }

    private String getRoomeType(String fromUser){
        try {
            String[] splitkByOne = fromUser.split("#");
            return splitkByOne[0];
        }catch (Exception ex){
            return null;
        }
    }
}
