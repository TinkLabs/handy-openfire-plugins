package com.hi.handy.authapi.plugin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hi.handy.authapi.plugin.dao.HdGroupAgentDao;
import com.hi.handy.authapi.plugin.dao.HdUserPropertyDao;
import com.hi.handy.authapi.plugin.entity.AgentStatus;
import com.hi.handy.authapi.plugin.model.NotifyModel;
import com.hi.handy.authapi.plugin.utils.Base64Utils;
import com.hi.handy.authapi.plugin.utils.MD5Utils;
import org.jivesoftware.openfire.XMPPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;

import java.util.List;

public class BaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);
    protected static final String LINE_THROUGH = "-";
    protected static final String AT_SYMBOL = "@";
    protected static final String VIP_CHAT_ROOM_SUFFIX = "room-vip#";
    protected static final String HOTEL_CHAT_ROOM_SUFFIX = "room-hotel#";
    protected static final String EMAIL_SUFFIX = "@tinklabs.com";
    protected static final String ROOMINFO_SUFFIX = "#";
    protected static final String HOTEL_CHAT_ROOM_GUEST_DEFAULT_DISPLAY_NAME = "guest";
    protected static final String DELETE_CHATROOM_SUFFIX = "_leave";

    protected String md5EncodePassword(String code) {
        return MD5Utils.MD5Encode(code,"utf8");
    }

    protected String encodePassword(String password){
        return Base64Utils.encode(password.getBytes());
    }

    protected String getDomain(){
        return XMPPServer.getInstance().getServerInfo().getXMPPDomain();
    }

    protected String joinListForInSqlString(List<String> ids){
        StringBuffer in_sql=new StringBuffer();
        for (int i = 0; i < ids.size(); i++) {
            in_sql.append("'" + ids.get(i) + "'");
            if (i < ids.size() - 1) {
                in_sql.append(",");
            }
        }
        return in_sql.toString();
    }

    protected Long groupIsOnline(String groupId){
        List<String> userNames = queryGroupAgentByGroupId(groupId);
        if(userNames!=null && userNames.size()>0) {
            Long count = HdUserPropertyDao.getInstance().searchLoginCount(joinListForInSqlString(userNames), AgentStatus.LOGGED);
            if (count != null && count > 0) {
                return count;
            }
        }
        return 0l;
    }

    private List<String> queryGroupAgentByGroupId(String groupId){
        return HdGroupAgentDao.getInstance().searchAgentByGroupId(groupId);
    }

    protected void notify(NotifyModel notifyModel) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String bodyContent = mapper.writeValueAsString(notifyModel);
            String serverName = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
            Message message = new Message();
            message.setBody(bodyContent);
            message.setTo("all@broadcast." + serverName);
            XMPPServer.getInstance().getRoutingTable().broadcastPacket(message, false);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            LOGGER.error("notify error", e);
        }
    }
}
