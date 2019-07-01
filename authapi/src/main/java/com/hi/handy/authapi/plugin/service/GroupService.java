package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.dao.HdGroupDao;
import com.hi.handy.authapi.plugin.dao.HdGroupRelationDao;
import com.hi.handy.authapi.plugin.entity.HdGroupEntity;
import com.hi.handy.authapi.plugin.entity.HdGroupRelationEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.AuthModel;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter;
import com.hi.handy.authapi.plugin.parameter.GroupParameter;
import com.hi.handy.authapi.plugin.parameter.Relation;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-06-27 14:53
 * @Description
 */
public class GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    public static GroupService getInstance() {
        return INSTANCE;
    }
    public static final GroupService INSTANCE = new GroupService();


    public AuthModel groupRegister(GroupParameter parameter) {
        //TODO icon上传
        //hdGroup 插入记录
        //openfire group创建
        AuthModel result = new AuthModel();
        LOGGER.info("groupRegister>>>>"+parameter.toString());
        if(groupRegCheck(parameter)){
            LOGGER.info("check pass>>");
            String groupName = null;
            HdGroupEntity hdGroupEntity = new HdGroupEntity();
            String uuid = getUUID();
            boolean isVIPGroup = false;
            if("VIP".equals(parameter.getType())){
                // name的命名: vip-chat-uuid
                isVIPGroup = true;
                groupName =  "vip-chat-"+uuid;
            }else if("HOTEL".equals(parameter.getType())){
                groupName =  "hotel-chat-"+uuid;
            }else{
                throw new BusinessException(ExceptionConst.PARAMETER_ERROR,"group type error");
            }
            hdGroupEntity.setId(uuid);
            hdGroupEntity.setName(groupName);
            hdGroupEntity.setDisplayName(parameter.getDisplayName());
            hdGroupEntity.setType(parameter.getType());
            hdGroupEntity.setWelcomeMessage(parameter.getWelcomeMessage());
            hdGroupEntity.setCreateDate(new Timestamp(System.currentTimeMillis()));
            boolean isGroupExist = HdGroupDao.getInstance().isGroupExist(uuid);
            LOGGER.info("uuid:"+uuid+" --"+isGroupExist);
            if(!isGroupExist){
                LOGGER.info("create hdGroup>>>>");
                boolean isCreateSuccess = HdGroupDao.getInstance().creatGroup(hdGroupEntity);
                if(isCreateSuccess){
                    //在openfire中添加group
                    LOGGER.info("create openfire group>>>>");
                    GroupManager groupManager = GroupManager.getInstance();
                    if(groupManager.isSearchSupported()){
                        Group existGroup = null;
                        try {
                            existGroup = groupManager.getGroup(groupName,true);
                        } catch (GroupNotFoundException e) {
                            e.printStackTrace();
                            LOGGER.warn("not found group:"+groupName+", will create new group");
                        }
                        if(null==existGroup){
                            //创建新的group
                            Group newGroup = null;
                            try {
                                newGroup = groupManager.createGroup(groupName);
                            } catch (GroupAlreadyExistsException e) {
                                e.printStackTrace();
                                LOGGER.error("create new group fail, GroupAlreadyExists group: "+groupName);
                                throw new BusinessException(ExceptionConst.BUSINESS_ERROR,"create new group fail, GroupAlreadyExists group: "+groupName);
                            }
//                            newGroup.setDescription("zoneId:"+parameter.getZoneId()+"'s group");
                            //sharedRoster.showInRoster 允许的值：onlyGroup，nobody，everybody。 onlyGroup和everybody的时候最好设置sharedRoster.displayName
//                            newGroup.getProperties().put("sharedRoster.showInRoster", "everybody");
                            newGroup.getProperties().put("sharedRoster.displayName", groupName);
//                            newGroup.getProperties().put("sharedRoster.groupList", "");
                        }
                    }else{
                        throw new BusinessException(ExceptionConst.BUSINESS_ERROR,"openfire not support group search");
                    }
                    //维护 agent group和 zone/hotel关系
                    LOGGER.info("create hdGroupRelation>>>>");
                    for (Relation relation : parameter.getRelations()) {
                        HdGroupRelationEntity hdGroupRelation = new HdGroupRelationEntity();
                        hdGroupRelation.setId(getUUID());
                        hdGroupRelation.setGroupId(uuid);
                        hdGroupRelation.setType(isVIPGroup?"VIP":"HOTEL");
                        hdGroupRelation.setRelationId(relation.getId());
                        hdGroupRelation.setRelationName(relation.getName());
                        hdGroupRelation.setCreateDate(new Timestamp(System.currentTimeMillis()));
                        HdGroupRelationDao.getInstance().createGroupRelation(hdGroupRelation);
                    }
                    result.setName(groupName);
                    result.setUid(uuid);
                }
            }
        }
        return result;
    }

    Boolean groupRegCheck(GroupParameter parameter) {
        if (StringUtils.isBlank(parameter.getDisplayName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group displayname is needed");
        }
        if (StringUtils.isBlank(parameter.getWelcomeMessage())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "welcome message is needed");
        }
        if (StringUtils.isBlank(parameter.getType())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "group type is needed");
        }
        if("VIP".equals(parameter.getType())){
            if(null==parameter.getRelations()||parameter.getRelations().size()<=0){
                throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "agent group manage zones is needed");
            }
        }else if("HOTEL".equals(parameter.getType())){
            if(null==parameter.getRelations()||parameter.getRelations().size()<=0){
                throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "agent group manage hotel is needed");
            }
        }else{
            throw new BusinessException(ExceptionConst.PARAMETER_ERROR,"group type error");
        }
        return true;
    }

    String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
