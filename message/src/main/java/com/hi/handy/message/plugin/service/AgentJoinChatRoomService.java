package com.hi.handy.message.plugin.service;

import com.hi.handy.message.plugin.domain.hdagentmessagerecord.AgentMessageRecordDao;
import com.hi.handy.message.plugin.domain.hdagentmessagerecord.AgentMessageRecordEntity;
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

public class AgentJoinChatRoomService extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentJoinChatRoomService.class);
    public static final AgentJoinChatRoomService INSTANCE = new AgentJoinChatRoomService();

    private AgentJoinChatRoomService() {
    }

    public static AgentJoinChatRoomService getInstance() {
        return INSTANCE;
    }

    public void setRoomAllMessageIsRead(Packet packet) {
        LOGGER.debug("setRoomAllMessageIsRead");
        Presence presence=(Presence) packet;
        LOGGER.debug("presence:" + presence);
        if (!shouldHandlerPresence(presence)) {
            return;
        }
        try {
            String roomType = getRoomeType(packet.getFrom().getNode());
            LOGGER.debug("setRoomAllMessageIsRead roomType:"+roomType);
            String userName= packet.getTo().getNode();
            LOGGER.debug("setRoomAllMessageIsRead userName:"+userName);
            if(StringUtils.isNoneBlank(roomType) && ("room-vip".equals(roomType)||"room-hotel".equals(roomType)) && userisExist(userName)) {
                String roomName = packet.getFrom().getNode();
                LOGGER.debug("setRoomAllMessageIsRead roomName:"+roomName);
                updateAgentMessageRecord(userName,roomName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateAgentMessageRecord(String userName, String roomName ) {
        LOGGER.debug("updateAgentMessageRecord");
        HdRoomMessageRecordEntity hdRoomMessageRecordEntity = HdRoomMessageRecordDao.getInstance().findByRoomName(roomName);
        if(hdRoomMessageRecordEntity!=null) {
            LOGGER.debug("hdRoomMessageRecordEntity is exist");

            AgentMessageRecordEntity agentMessageRecordEntity = AgentMessageRecordDao.getInstance().findByAgentNameAndRoomName(userName,roomName);
            if(agentMessageRecordEntity!=null){
                LOGGER.debug("agentMessageRecordEntity is exist");
                AgentMessageRecordDao.getInstance().updateByUserNameAndRoomName(agentMessageRecordEntity.getId(),hdRoomMessageRecordEntity.getAmount(),new java.sql.Timestamp(System.currentTimeMillis()));
            }else{
                LOGGER.debug("createAgentMessageRecord");
                createAgentMessageRecord(userName,roomName);
            }
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
        LOGGER.debug("shouldHandlerPresence");
        JID recipient = presence.getTo();
        String username = recipient.getNode();
        LOGGER.debug("shouldHandlerPresence username:"+username);
        if (username == null || !UserManager.getInstance().isRegisteredUser(recipient)) {
            return false;
        }
        LOGGER.debug("shouldHandlerPresence domain:"+recipient.getDomain());
        if (!XMPPServer.getInstance().getServerInfo().getXMPPDomain().equals(recipient.getDomain())) {
            return false;
        }
        return true;
    }

    private void createAgentMessageRecord(String userName, String roomName){
        LOGGER.debug("createAgentMessageRecord");
        LOGGER.debug("createAgentMessageRecord userName:"+userName);
        LOGGER.debug("createAgentMessageRecord roomName:"+roomName);
        AgentMessageRecordDao.getInstance().create(getId(),userName,roomName,0l,new java.sql.Timestamp(System.currentTimeMillis()));
    }
}
