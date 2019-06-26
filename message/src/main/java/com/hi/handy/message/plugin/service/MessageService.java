package com.hi.handy.message.plugin.service;

import com.hi.handy.message.plugin.domain.hdmessage.HdMessageDao;
import com.hi.handy.message.plugin.domain.hdmessage.HdMessageEntity;
import com.hi.handy.message.plugin.domain.hdroommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.message.plugin.domain.hdroommessagerecord.HdRoomMessageRecordEntity;
import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyDao;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

public class MessageService extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    public static final MessageService INSTANCE = new MessageService();

    private MessageService() {
    }

    public static MessageService getInstance() {
        return INSTANCE;
    }

    public void recordMessage(Packet packet) {
        Message message = (Message) packet;
        if (!shouldStoreMessage(message,packet)) {
            return;
        }
        LOGGER.debug("recordMessage:" + message);
        save(packet);
    }

    private void save(Packet packet) {
        LOGGER.debug("save");
        LOGGER.debug("packet",packet);
        try {
            Message message = (Message) packet;
            String roomType = getRoomeType(packet.getTo().getNode());

            if(StringUtils.isNoneBlank(roomType) && ("room-vip".equals(roomType)||"room-hotel".equals(roomType)) && userisExist(packet.getFrom().getNode())) {
                String[] roomInfoArray = packet.getTo().getNode().split("#");
                String[] getRoomInfoDetailArray = roomInfoArray[1].split("-");
                Long zoneId = Long.valueOf(getRoomInfoDetailArray[0]);
                Long hotelId = Long.valueOf(getRoomInfoDetailArray[1]);
                String deviceUserId = getRoomInfoDetailArray[2];
                String roomNum = getRoomInfoDetailArray[3];
                String roomName = packet.getTo().getNode();

                createOrUpdateMessage(packet, message.getID(), message.getElement().asXML(), zoneId, hotelId, roomNum, deviceUserId);
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

    private void createOrUpdateMessage(Packet packet,String messageId,String stanza,Long zoneId,Long hotelId,String roomNum, String deviceUserId) {
        LOGGER.debug("createOrUpdateMessage");
        String systemId = HdMessageDao.getInstance().findByToJID(packet.getTo().toBareJID());
        if(StringUtils.isNoneBlank(systemId)){
            HdMessageDao.getInstance().updateStanzaById(systemId,stanza,new java.sql.Timestamp(System.currentTimeMillis()));
        }else{
            HdMessageEntity hdMessageEntity = new HdMessageEntity();
            hdMessageEntity.setId(getId());
            hdMessageEntity.setMessageId(messageId);
            hdMessageEntity.setZoneId(zoneId);
            hdMessageEntity.setHotelId(hotelId);
            hdMessageEntity.setRoomNum(roomNum);
            hdMessageEntity.setDeviceUserId(deviceUserId);
            hdMessageEntity.setFromUser(packet.getFrom().getNode());
            hdMessageEntity.setFromJID(packet.getFrom().toBareJID());
            hdMessageEntity.setToUser(packet.getTo().getNode());
            hdMessageEntity.setToJID(packet.getTo().toBareJID());
            hdMessageEntity.setCreationDate(new java.sql.Timestamp(System.currentTimeMillis()));
            hdMessageEntity.setStanza(stanza);
            HdMessageDao.getInstance().create(hdMessageEntity);
        }

    }

    private void updateRoomMessageRecord(HdRoomMessageRecordEntity hdRoomMessageRecordEntity) {
        LOGGER.debug("updateRoomMessageRecord");
        HdRoomMessageRecordDao.getInstance().updateAmountAndUpdateDateById(hdRoomMessageRecordEntity.getId(),hdRoomMessageRecordEntity.getAmount()+1,new java.sql.Timestamp(System.currentTimeMillis()));
    }

    private void createRoomMessageRecord(String roomName) {
        LOGGER.debug("createRoomMessageRecord");
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity = new HdRoomMessageRecordEntity();
        hdRoomMessageRecordEntity.setId(getId());
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

    private Boolean userisExist(String userName){
        Long count = HdUserPropertyDao.getInstance().countByUserName(userName);
        if(count!=null&&(count.longValue()>0)){
            return true;
        }
        return false;
    }

    private boolean shouldStoreMessage(Message message,Packet packet) {
        if (message.getType() != Message.Type.groupchat) {
            return false;
        }
        if (StringUtils.isBlank(message.getBody())) {
            return false;
        }
        JID recipient = message.getFrom();
        String username = recipient.getNode();

        if (username == null || !UserManager.getInstance().isRegisteredUser(recipient)) {
            return false;
        }

        PacketExtension packetExtension = packet.getExtension("stanza-id","urn:xmpp:sid:0");
        if(packetExtension!=null){
            return false;
        }

        if (!XMPPServer.getInstance().getServerInfo().getXMPPDomain().equals(recipient.getDomain())) {
            return false;
        }
        return true;
    }
}
