package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.dao.HdGroupDao;
import com.hi.handy.authapi.plugin.dao.HdGroupRelationDao;
import com.hi.handy.authapi.plugin.entity.HdGroupEntity;
import com.hi.handy.authapi.plugin.entity.HdGroupRelationEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.GroupInfoModel;
import com.hi.handy.authapi.plugin.model.RelationModel;
import com.hi.handy.authapi.plugin.parameter.GroupParameter;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GroupService extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    public static GroupService getInstance() {
        return INSTANCE;
    }
    public static final GroupService INSTANCE = new GroupService();

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
                if (null == group) {
                    group = groupManager.createGroup(groupName);
                    group.getProperties().put("sharedRoster.displayName", groupName);
                }
            }catch (Exception ex){
                throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "create group in openfire failed");
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

}
