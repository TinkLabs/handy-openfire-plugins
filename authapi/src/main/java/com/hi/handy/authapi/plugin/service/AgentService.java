package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.dao.HdGroupAgentDao;
import com.hi.handy.authapi.plugin.dao.HdGroupDao;
import com.hi.handy.authapi.plugin.dao.HdGroupRelationDao;
import com.hi.handy.authapi.plugin.dao.HdUserPropertyDao;
import com.hi.handy.authapi.plugin.entity.AgentStatus;
import com.hi.handy.authapi.plugin.entity.GroupType;
import com.hi.handy.authapi.plugin.entity.HdGroupEntity;
import com.hi.handy.authapi.plugin.entity.HdUserPropertyEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.AgentInfoModel;
import com.hi.handy.authapi.plugin.model.AuthModel;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;

import java.util.List;

public class AgentService extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentService.class);

    private AgentService() {
    }

    public static AgentService getInstance() {
        return INSTANCE;
    }

    public static final AgentService INSTANCE = new AgentService();

    public AgentInfoModel agentLogin(AuthParameter parameter) throws UserAlreadyExistsException, UserNotFoundException {
        LOGGER.info("agentLogin");
        LOGGER.info("parameter",parameter);
        if (parameter.getAuthType() != BaseParameter.AuthType.AGENT_LOGIN) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is wrong");
        }
        if (StringUtils.isBlank(parameter.getEmail())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "email is needed");
        }
        if (StringUtils.isBlank(parameter.getPassword())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "password is needed");
        }
        if (parameter.getPassword().trim().length() < 8) {
            throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "password length no less than 8");
        }

        String agentUserName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
        HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByUserName(agentUserName);
        if(hdUserPropertyEntity == null) throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "email or password is wrong");
        String password = md5EncodePassword(parameter.getPassword());
        if(!password.equals(hdUserPropertyEntity.getPassword())) throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "password is wrong");
        if(!(StringUtils.isNoneBlank(hdUserPropertyEntity.getStatus())&&hdUserPropertyEntity.getStatus().equals(AgentStatus.LOGGED.name()))){
            // update status
            HdUserPropertyDao.getInstance().updateStatus(agentUserName, AgentStatus.LOGGED.name());
            // notify group status
            notifyGroupStatus(agentUserName);
        }

        // find openFire user by email,if user is not exist then create
        UserManager userManager = UserManager.getInstance();
        User user;
        if (!userManager.isRegisteredUser(agentUserName)) {
            user = userManager.createUser(agentUserName, password, parameter.getDisplayName(),parameter.getEmail());
        } else {
            user = userManager.getUser(agentUserName);
        }

        String groupId = HdGroupRelationDao.getInstance().searchByRelationId(hdUserPropertyEntity.getZoneId(), GroupType.VIP.name());
        HdGroupEntity hdGroupEntity = HdGroupDao.getInstance().searchById(groupId);
        AgentInfoModel result = new AgentInfoModel();
        result.setGroupIcon(hdGroupEntity.getIcon());
        result.setGroupName(hdGroupEntity.getName());
        result.setWelcomeMessage(hdGroupEntity.getWelcomeMessage());
        result.setUid(user.getUsername());
        result.setName(user.getName());
        result.setToken(encodePassword(password));
        result.setDomain(getDomain());
        return result;
    }

    public AuthModel agentLogout(AuthParameter parameter){
        LOGGER.info("agentLogout");
        LOGGER.info("parameter",parameter);
        if (parameter.getAuthType() != BaseParameter.AuthType.AGENT_LOGOUT) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is wrong");
        }
        if (StringUtils.isBlank(parameter.getEmail())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "email is needed");
        }

        String agentUserName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
        HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByUserName(agentUserName);
        if(hdUserPropertyEntity == null) throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "user is not exit");
        if(StringUtils.isNoneBlank(hdUserPropertyEntity.getStatus())&&hdUserPropertyEntity.getStatus().equals(AgentStatus.LOGGED.name())){
            // update status
            HdUserPropertyDao.getInstance().updateStatus(agentUserName, AgentStatus.LOGGED.name());
            // notify group status
            notifyGroupStatus(agentUserName);
        }
        return null;
    }

    public void notifyGroupStatus(String userName){
        List<String> groupIds  = HdGroupAgentDao.getInstance().searchByUserName(userName);
        if(groupIds!=null&&groupIds.size()>0) {
            groupIds.forEach(groupId -> {
                Long onlineAgent = groupIsOnline(groupId);
                if(onlineAgent==0){
                    sendMessage(groupId, false);
                }
                if (onlineAgent==1) {
                    sendMessage(groupId, true);
                }
            });
        }
    }

    private void sendMessage(String groupId,boolean groupStatus){
        String serverName = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
        LOGGER.info("serverName:"+serverName);
        String body="{type:groupType,id:"+groupId+",isonline:"+groupStatus+"}";
        Message message = new Message();
        message.setBody(body);
        message.setTo("all@broadcast." + serverName);
        XMPPServer.getInstance().getRoutingTable().broadcastPacket(message, false);
    }
}
