package com.hi.handy.group.plugin.service;

import com.hi.handy.group.plugin.domain.hdgroup.HdGroupDao;
import com.hi.handy.group.plugin.domain.hdgroup.HdGroupEntity;
import com.hi.handy.group.plugin.domain.hdgroupagent.HdGroupAgentDao;
import com.hi.handy.group.plugin.domain.hdgroupagent.HdGroupAgentEntity;
import com.hi.handy.group.plugin.domain.hdgrouprelaction.HdGroupRelationDao;
import com.hi.handy.group.plugin.domain.hdgrouprelaction.HdGroupRelationEntity;
import com.hi.handy.group.plugin.domain.hduserproperty.HdUserPropertyDao;
import com.hi.handy.group.plugin.domain.hduserproperty.HdUserPropertyEntity;
import com.hi.handy.group.plugin.exception.BusinessException;
import com.hi.handy.group.plugin.exception.ExceptionConst;
import com.hi.handy.group.plugin.model.GroupInfoModel;
import com.hi.handy.group.plugin.model.GroupModel;
import com.hi.handy.group.plugin.model.RelationModel;
import com.hi.handy.group.plugin.parameter.*;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupService extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    private static final GroupService INSTANCE = new GroupService();
    private GroupService() {}
    public static GroupService getInstance() {
        return INSTANCE;
    }

    public List<GroupModel> getGroupList() {
        LOGGER.info("getGroupList");
        List<GroupModel> groupModelList = new ArrayList();
        List<HdGroupEntity> groupEntityList = HdGroupDao.getInstance().searchAll();
        List<HdGroupAgentEntity> groupAgentEntityList = HdGroupAgentDao.getInstance().searchAll();
        List<HdGroupRelationEntity> groupRelationEntityList = HdGroupRelationDao.getInstance().searchAll();

        for(HdGroupEntity hdGroupEntity:groupEntityList){
            GroupModel groupModel = new GroupModel();
            groupModel.setGroup(hdGroupEntity);
            groupModel.setAgents(groupAgentEntityList.stream().filter(n->n.getGroupId().equals(hdGroupEntity.getId())).collect(Collectors.toList()));
            groupModel.setRelations(groupRelationEntityList.stream().filter(n->n.getGroupId().equals(hdGroupEntity.getId())).collect(Collectors.toList()));
            groupModelList.add(groupModel);
        }
        return groupModelList;
    }

    public GroupInfoModel groupRegister(GroupParameter parameter) throws GroupNotFoundException, GroupAlreadyExistsException {
        LOGGER.info("groupRegister:" + parameter);
        groupRegCheck(parameter);
        String uuid = getUUID();
        String groupName = getGroupName(parameter.getType(), uuid);
        LOGGER.info("groupName:" + groupName);
        LOGGER.info("create new hdGroup in db");
        boolean result = createGroup(parameter, uuid, groupName);
        if(!result){
            throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "create group failed");
        }
        LOGGER.info("create new hdGroup in openfire");
        GroupManager groupManager = GroupManager.getInstance();
        if (groupManager.isSearchSupported()) {
            Group group = null;
            try {
                group = groupManager.getGroup(groupName, true);
            }catch (GroupNotFoundException ex){

            }catch (Exception ex){
                throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "create group in openfire failed");
            }
            if (null == group) {
                group = groupManager.createGroup(groupName);
                group.getProperties().put("sharedRoster.displayName", groupName);
            }

        } else {
            throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "openfire not support group search");
        }
        LOGGER.info("create hdGroupRelation ");
        createGroupRelaction(parameter, uuid);
        return new GroupInfoModel(uuid,groupName);
    }

    private void createGroupRelaction(GroupParameter parameter, String uuid) {
        List<RelationModel> relationModelList = getRelationArray(parameter.getRelations());
        for (RelationModel relation : relationModelList) {
            HdGroupRelationEntity hdGroupRelation = new HdGroupRelationEntity();
            hdGroupRelation.setId(getUUID());
            hdGroupRelation.setGroupId(uuid);
            hdGroupRelation.setType(parameter.getType());
            hdGroupRelation.setRelationId(relation.getId());
            hdGroupRelation.setRelationName(relation.getName());
            hdGroupRelation.setCreateDate(new Timestamp(System.currentTimeMillis()));
            HdGroupRelationDao.getInstance().createGroupRelation(hdGroupRelation);
        }
    }

    private boolean createGroup(GroupParameter parameter, String uuid, String groupName) {
        HdGroupEntity hdGroupEntity = new HdGroupEntity();
        hdGroupEntity.setId(uuid);
        hdGroupEntity.setIcon(parameter.getIcon());
        hdGroupEntity.setName(groupName);
        hdGroupEntity.setDisplayName(parameter.getName());
        hdGroupEntity.setType(parameter.getType());
        hdGroupEntity.setWelcomeMessage(parameter.getWelcomeMessage());
        hdGroupEntity.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return HdGroupDao.getInstance().createGroup(hdGroupEntity);
    }

    private String getGroupName(String type, String uuid) {
        if("VIP".equals(type)){
            return "vip-chat-"+uuid;
        }
        if("HOTEL".equals(type)) {
            return "hotel-chat-"+uuid;
        }
        return null;
    }

    private void groupRegCheck(GroupParameter parameter) {
        if (StringUtils.isBlank(parameter.getIcon())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group name is needed");
        }
        if (StringUtils.isBlank(parameter.getWelcomeMessage())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "welcome message is needed");
        }
        if (StringUtils.isBlank(parameter.getType())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group type is needed");
        }
        if (StringUtils.isBlank(parameter.getRelations())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE,"relations parameter is needed!");
        }
        List<RelationModel>  relationModelList = getRelationArray(parameter.getRelations());
        if(!(null!=relationModelList&&relationModelList.size()>0)){
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE,"relations parameter is error");
        }
        if(StringUtils.isBlank(parameter.getType())){
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE,"group type is needed");
        }
        if(!("VIP".equals(parameter.getType()) || "HOTEL".equals(parameter.getType()))){
            throw new BusinessException(ExceptionConst.PARAMETER_ERROR,"group type error");
        }
    }

    private List<RelationModel> getRelationArray(String relations) {
        List<RelationModel> relationsList = new ArrayList<>();
        String[] relationArray = relations.split(";");
        if (!(null!=relationArray&&relationArray.length > 0)) {
            throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "relations parameter is error!");
        }

        for (String relationKeyValueString : relationArray) {
            String[] relationKeyValue = relationKeyValueString.split(":");
            if(!(null!=relationKeyValue&&relationKeyValue.length==2)){
                throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "relations parameter is error!");
            }
            RelationModel relationModel = new RelationModel();
            relationModel.setId(relationKeyValue[0]);
            relationModel.setName(relationKeyValue[1]);
            relationsList.add(relationModel);
        }
        return relationsList;
    }

    public boolean deleteGroup(GroupDeleteParameter parameter) {
        LOGGER.debug("deleteGroup");
        LOGGER.debug("parameter",parameter);
        BaseParameter.ApiType type = parameter.getApiType();
        if (type == BaseParameter.ApiType.RELATION_DELETE) {
            return deleteGroupRelation(parameter);
        } else if (type == BaseParameter.ApiType.AGENT_DELETE) {
            return deleteGroupAgent(parameter);
        } else{
            throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "unknow authType");
        }
    }

    public boolean deleteGroupRelation(GroupDeleteParameter parameter) {
        if (StringUtils.isBlank(parameter.getId())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "id is needed");
        }
        return HdGroupRelationDao.getInstance().delete(parameter.getId());
    }

    public boolean deleteGroupAgent(GroupDeleteParameter parameter) {
        if (StringUtils.isBlank(parameter.getId())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "id is needed");
        }
        HdGroupAgentEntity hdGroupAgentEntity = HdGroupAgentDao.getInstance().searchGroupIdById(parameter.getId());
        if(null == hdGroupAgentEntity){
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group is not exist");
        }
        HdGroupEntity groupEntity = HdGroupDao.getInstance().searchById(hdGroupAgentEntity.getGroupId());

        if (null == groupEntity) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group is not exist");
        }

        GroupManager groupManager = GroupManager.getInstance();
        if(groupManager.isSearchSupported()){
            String groupName = groupEntity.getName();
            Group existGroup = null;
            try {
                existGroup = groupManager.getGroup(groupName,true);
            } catch (GroupNotFoundException e) {
                throw new BusinessException(ExceptionConst.BUSINESS_ERROR,"group "+groupName+" is not in openfire");
            }
            if(null!=existGroup){
                JID jid = new JID(hdGroupAgentEntity.getUserName());
                existGroup.getMembers().remove(jid);
            }else {
                throw new BusinessException(ExceptionConst.BUSINESS_ERROR,"group "+groupName+" is not in openfire");
            }
        }else{
            LOGGER.error("openfire not support group search");
        }
        return HdGroupAgentDao.getInstance().delete(parameter.getId());
    }

    public boolean addGroupRelation(GroupRelationParameter parameter){
        LOGGER.info("addGroupRelation");
        if (StringUtils.isBlank(parameter.getGroupId())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group id is needed");
        }
        if (StringUtils.isBlank(parameter.getId())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "id is needed");
        }
        if (StringUtils.isBlank(parameter.getName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "name is needed");
        }
        HdGroupEntity groupEntity = HdGroupDao.getInstance().searchById(parameter.getGroupId());
        if (null == groupEntity) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group is not exist");
        }

        HdGroupRelationEntity hdGroupRelation = new HdGroupRelationEntity();
        hdGroupRelation.setId(getUUID());
        hdGroupRelation.setGroupId(parameter.getGroupId());
        hdGroupRelation.setType(groupEntity.getType());
        hdGroupRelation.setRelationId(parameter.getId());
        hdGroupRelation.setRelationName(parameter.getName());
        hdGroupRelation.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return HdGroupRelationDao.getInstance().createGroupRelation(hdGroupRelation);
    }

    public void addGroupAgent(GroupAgentParameter parameter){
        LOGGER.info("addGroupAgent");
        if (StringUtils.isBlank(parameter.getGroupId())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group id is needed");
        }
        if (StringUtils.isBlank(parameter.getName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "name is needed");
        }
        if (StringUtils.isBlank(parameter.getPassword())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "password is needed");
        }

        HdGroupEntity groupEntity = HdGroupDao.getInstance().searchById(parameter.getGroupId());
        if (null == groupEntity) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group is not exist");
        }

        if(StringUtils.isBlank(parameter.getDisplayName())){
            String[] agentUserNameArray = parameter.getName().split(AT_SYMBOL);
            parameter.setDisplayName(agentUserNameArray[0]);
        }

        String agentUserName = parameter.getName().replace(AT_SYMBOL, LINE_THROUGH);
        int count = HdUserPropertyDao.getInstance().countByUserName(agentUserName);
        if(count<1) {
            HdUserPropertyEntity hdUserPropertyEntity = new HdUserPropertyEntity();
            hdUserPropertyEntity.setUserName(agentUserName);
            hdUserPropertyEntity.setDisplayName(parameter.getDisplayName());
            hdUserPropertyEntity.setPassword(md5EncodePassword(parameter.getPassword().trim()));
            hdUserPropertyEntity.setType(parameter.getType());
            HdUserPropertyDao.getInstance().createUserProperty(hdUserPropertyEntity);
        }

        GroupManager groupManager = GroupManager.getInstance();
        if(groupManager.isSearchSupported()){
            String groupName = groupEntity.getName();
            Group existGroup = null;
            try {
                existGroup = groupManager.getGroup(groupName,true);
            } catch (GroupNotFoundException e) {
                throw new BusinessException(ExceptionConst.BUSINESS_ERROR,"group "+groupName+" is not in openfire");
            }
            if(null!=existGroup){
                JID jid = new JID(agentUserName);
                existGroup.getMembers().add(jid);
            }else {
                throw new BusinessException(ExceptionConst.BUSINESS_ERROR,"group "+groupName+" is not in openfire");
            }
        }else{
            LOGGER.error("openfire not support group search");
        }
        int groupAgentCount = HdGroupAgentDao.getInstance().countByUserName(agentUserName);
        if(groupAgentCount<1) {
            HdGroupAgentEntity hdGroupAgent = new HdGroupAgentEntity();
            hdGroupAgent.setId(getUUID());
            hdGroupAgent.setUserName(agentUserName);
            hdGroupAgent.setGroupId(groupEntity.getId());
            hdGroupAgent.setCreateDate(new Timestamp(System.currentTimeMillis()));

            boolean result = HdGroupAgentDao.getInstance().createGroupAgent(hdGroupAgent);
            if (!result) {
                throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "create failed! try again");
            }
        }
    }
}
