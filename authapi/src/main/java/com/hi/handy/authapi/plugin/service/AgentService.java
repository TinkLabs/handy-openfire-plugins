package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.dao.HdGroupAgentDao;
import com.hi.handy.authapi.plugin.dao.HdGroupDao;
import com.hi.handy.authapi.plugin.dao.HdUserPropertyDao;
import com.hi.handy.authapi.plugin.entity.AgentStatus;
import com.hi.handy.authapi.plugin.entity.HdGroupAgentEntity;
import com.hi.handy.authapi.plugin.entity.HdGroupEntity;
import com.hi.handy.authapi.plugin.entity.HdUserPropertyEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.AgentInfoModel;
import com.hi.handy.authapi.plugin.model.AuthModel;
import com.hi.handy.authapi.plugin.model.GroupStatusNotifyModel;
import com.hi.handy.authapi.plugin.model.NotifyType;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter;
import com.hi.handy.authapi.plugin.utils.Base64Utils;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

import java.sql.Timestamp;
import java.util.UUID;

public class AgentService extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentService.class);

    private AgentService() {
    }

    public static AgentService getInstance() {
        return INSTANCE;
    }

    public static final AgentService INSTANCE = new AgentService();

    public AgentInfoModel agentLogin(AuthParameter parameter) throws UserAlreadyExistsException, UserNotFoundException {
        LOGGER.debug("agentLogin");
        LOGGER.debug("parameter",parameter);
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
        String displayName = hdUserPropertyEntity.getDisplayName();
        if(StringUtils.isBlank(displayName)){
            String[] agentUserNameArray = agentUserName.split(LINE_THROUGH);
            displayName = agentUserNameArray[0];
        }
        // find openFire user by email,if user is not exist then create
        UserManager userManager = UserManager.getInstance();
        User user;
        if (!userManager.isRegisteredUser(agentUserName)) {
            user = userManager.createUser(agentUserName, password, displayName,parameter.getEmail());
        } else {
            user = userManager.getUser(agentUserName);
        }

        String groupId = HdGroupAgentDao.getInstance().searchByUserName(agentUserName);
        HdGroupEntity hdGroupEntity = HdGroupDao.getInstance().searchById(groupId);
        AgentInfoModel result = new AgentInfoModel();
        result.setGroupIcon(hdGroupEntity.getIcon());
        result.setGroupName(hdGroupEntity.getDisplayName());
        result.setWelcomeMessage(hdGroupEntity.getWelcomeMessage());
        result.setUid(user.getUsername());
        result.setName(displayName);
        result.setToken(encodePassword(password));
        result.setDomain(getDomain());
        return result;
    }

    public AuthModel agentLogout(AuthParameter parameter){
        LOGGER.debug("agentLogout");
        LOGGER.debug("parameter",parameter);
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
            HdUserPropertyDao.getInstance().updateStatus(agentUserName, AgentStatus.NOTLOGGED.name());
            // notify group status
            notifyGroupStatus(agentUserName);
        }
        return null;
    }

    public void notifyGroupStatus(String userName){
        String groupId  = HdGroupAgentDao.getInstance().searchByUserName(userName);
        Long onlineAgent = groupIsOnline(groupId);
        if(onlineAgent==0){
            notify(new GroupStatusNotifyModel(NotifyType.GROUPSTATUS,groupId,false));
        }
        if (onlineAgent==1) {
            notify(new GroupStatusNotifyModel(NotifyType.GROUPSTATUS,groupId,true));
        }
    }

    /**
     * agent 注册逻辑：判断是hotel 还是 vip agent，hotel agent，
     * vip agent zoneid 不能为空，hotel agent hotelid不能为空
     *
     * @param parameter
     * @return
     */
    public AgentInfoModel agentRegister(AuthParameter parameter) throws UserAlreadyExistsException {
        if(agentRegCheck(parameter)){


            UserManager userManager = UserManager.getInstance();
            User user;
            String agentUserName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
            if (userManager.isRegisteredUser(agentUserName)) {
                throw new BusinessException(ExceptionConst.DATA_REPEATED, "account is already exist ");
            }

            // 先添加或者更新agent的相关属性，确保成功后再创建agent用户
            Long count = HdUserPropertyDao.getInstance().countByUserName(agentUserName);
            parameter.setUserType(BaseParameter.UserType.AGENT);
            String password =  md5EncodePassword(parameter.getPassword().trim());
            parameter.setPassword(password);
            if (count == null || count < 1L) {
                HdUserPropertyDao.getInstance().createUserProperty(agentUserName, parameter);
            }
            //openfire 创建用户
            user = userManager.createUser(agentUserName, password, parameter.getDisplayName(),parameter.getEmail());;

            // 加入group：
            //TODO openfire group和 hdGroupAgent


            // 返回账号信息
            AgentInfoModel result = new AgentInfoModel();
            result.setUid(user.getUsername());
            result.setDomain(getDomain());
            result.setToken(Base64Utils.encode(parameter.getPassword().getBytes()));

            GroupManager groupManager = GroupManager.getInstance();
            if(groupManager.isSearchSupported()){
                String groupName = parameter.getGroupName();
                Group existGroup = null;
                HdGroupEntity hdGroupEntity = HdGroupDao.getInstance().searchByGroupName(groupName);
                try {
                    existGroup = groupManager.getGroup(groupName,true);
                } catch (GroupNotFoundException e) {
                    LOGGER.error("agent:"+agentUserName+" cannot join agent group cause not exist group:"+groupName);
                }
                if(null!=existGroup&&null!=hdGroupEntity){
                    LOGGER.info("agent join existGroup:" + existGroup.getName());
                    JID jid = new JID(agentUserName);
                    //group 能多次加入？
                    existGroup.getMembers().add(jid);
                }else {
                    throw new BusinessException(ExceptionConst.BUSINESS_ERROR,"agent join agent group fail cause not exist group:"+groupName);
                }
                //hdGroupAgent
                HdGroupAgentEntity hdGroupAgent = new HdGroupAgentEntity();
                hdGroupAgent.setId(getUUID());
                hdGroupAgent.setUserName(agentUserName);
                hdGroupAgent.setGroupId(hdGroupEntity.getId());
                hdGroupAgent.setCreateDate(new Timestamp(System.currentTimeMillis()));
                HdGroupAgentDao.getInstance().createGroupAgent(hdGroupAgent);

            }else{
                LOGGER.error("agent:"+agentUserName+" cannot join agent group cause Openfire not support group search");
            }
            return result;
        }
        return null;
    }

    Boolean agentRegCheck(AuthParameter parameter){
        if (parameter.getAuthType() != BaseParameter.AuthType.AGENT_REGISTER) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is needed");
        }
//        if (parameter.getUserType() != BaseParameter.UserType.AGENT) {
//            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "usertype is needed");
//        }
        if (StringUtils.isBlank(parameter.getEmail())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "email is needed");
        }
        if (StringUtils.isBlank(parameter.getPassword())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "password is needed");
        }
        if (parameter.getPassword().trim().length() < 8) {
            throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "password length no less than 8");
        }
        if (null==parameter.getGroupName()){
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "should join agent group");
        }
        return true;
    }
    String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
