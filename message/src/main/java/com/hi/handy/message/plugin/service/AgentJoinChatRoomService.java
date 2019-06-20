package com.hi.handy.message.plugin.service;

import com.hi.handy.message.plugin.domain.hdagentmessagerecord.AgentMessageRecordDao;
import com.hi.handy.message.plugin.domain.hdroommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.message.plugin.domain.hdroommessagerecord.HdRoomMessageRecordEntity;
import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyDao;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

public class AgentJoinChatRoomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentJoinChatRoomService.class);
    public static final AgentJoinChatRoomService INSTANCE = new AgentJoinChatRoomService();

    private AgentJoinChatRoomService() {
    }

    public static AgentJoinChatRoomService getInstance() {
        return INSTANCE;
    }

    public void setRoomAllMessageIsRead(Packet packet) {
        LOGGER.info("setRoomAllMessageIsRead");
        Presence presence=(Presence) packet;
        LOGGER.info("presence:" + presence);
        if (!shouldHandlerPresence(presence)) {
            return;
        }
        try {
            String roomType = getRoomeType(packet.getFrom().getNode());
            LOGGER.info("setRoomAllMessageIsRead roomType:"+roomType);
            String userName= packet.getTo().getNode();
            LOGGER.info("setRoomAllMessageIsRead userName:"+userName);
            if(StringUtils.isNoneBlank(roomType) && ("room-vip".equals(roomType)||"room-hotel".equals(roomType)) && userisExist(userName)) {
                String roomName = packet.getFrom().getNode();
                updateAgentMessageRecord(userName,roomName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateAgentMessageRecord(String userName, String roomName ) {
        LOGGER.info("updateAgentMessageRecord");
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity = HdRoomMessageRecordDao.getInstance().findByRoomName(roomName);
        if(hdRoomMessageRecordEntity!=null) {
            AgentMessageRecordDao.getInstance().updateByUserNameAndRoomName(userName, roomName,hdRoomMessageRecordEntity.getAmount());
        }
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

    private boolean shouldHandlerPresence(Presence presence) {
        JID recipient = presence.getTo();
        String username = recipient.getNode();

        if (username == null || !UserManager.getInstance().isRegisteredUser(recipient)) {
            return false;
        }

        if (!XMPPServer.getInstance().getServerInfo().getXMPPDomain().equals(recipient.getDomain())) {
            return false;
        }
        return true;
    }
}
