package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.dao.HdGroupAgentDao;
import com.hi.handy.authapi.plugin.dao.HdUserPropertyDao;
import com.hi.handy.authapi.plugin.entity.AgentStatus;
import com.hi.handy.authapi.plugin.utils.Base64Utils;
import com.hi.handy.authapi.plugin.utils.MD5Utils;
import org.jivesoftware.openfire.XMPPServer;

import java.util.List;

public class BaseService {

    protected static final String LINE_THROUGH = "-";
    protected static final String AT_SYMBOL = "@";
    protected static final String DEFAULT_SERVICE_NAME = "conference";
    protected static final String VIP_CHAT_ROOM_SUFFIX = "room-vip#";
    protected static final String HOTEL_CHAT_ROOM_SUFFIX = "room-hotel#";
    protected static final String EMAIL_SUFFIX = "@tinklabs.com";
    protected static final String ROOMINFO_SUFFIX = "#";
    protected static final String HOTEL_CHAT_ROOM_GUEST_DEFAULT_DISPLAY_NAME = "guest";

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
}
