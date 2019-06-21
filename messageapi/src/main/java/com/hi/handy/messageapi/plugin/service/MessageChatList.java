package com.hi.handy.messageapi.plugin.service;

import com.hi.handy.messageapi.plugin.domain.hdagentmessagerecord.AgentMessageRecordDao;
import com.hi.handy.messageapi.plugin.domain.hdgroupagent.HdGroupAgentDao;
import com.hi.handy.messageapi.plugin.domain.hdgrouprelation.HdGroupRelationDao;
import com.hi.handy.messageapi.plugin.domain.hdmessage.HdMessageDao;
import com.hi.handy.messageapi.plugin.domain.hdmessage.HdMessageEntity;
import com.hi.handy.messageapi.plugin.domain.hdroommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.messageapi.plugin.domain.hduserproperty.HdUserPropertyDao;
import com.hi.handy.messageapi.plugin.domain.hduserproperty.HdUserPropertyEntity;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import com.hi.handy.messageapi.plugin.model.MessageModel;
import com.hi.handy.messageapi.plugin.parameter.MessageParameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageChatList extends BaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageChatList.class);

    public static final MessageChatList INSTANCE = new MessageChatList();

    private MessageChatList() {
    }

    public static MessageChatList getInstance() {
        return INSTANCE;
    }

    public List<MessageModel> queryChatListByPaging(MessageParameter parameter) {
        LOGGER.debug("queryChatListByPaging");
        LOGGER.debug("parameter userName:"+parameter.getUserName());
        String userName = parameter.getUserName();
        HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByName(userName);
        if(hdUserPropertyEntity == null) throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "user is not exit");
        return queryChatListByPaging(hdUserPropertyEntity.getUserName(),parameter.getPageIndex(),parameter.getPageSize());
    }

    private List<MessageModel> queryChatListByPaging(String userName,Integer pageIndex,Integer pageSize){
        LOGGER.debug("queryChatListByPaging");
        List<MessageModel> messageModels = new ArrayList();
        String groupId = HdGroupAgentDao.getInstance().searchByUserName(userName);
        if(StringUtils.isNoneBlank(groupId)) {
            List<Long> zoneAndHotelIds = HdGroupRelationDao.getInstance().searchHotelIdOrZoneIdByGroupId(groupId);
            LOGGER.debug("zoneAndHotelIds"+zoneAndHotelIds);
            if(zoneAndHotelIds!=null&&zoneAndHotelIds.size()>0) {
                String zoneAndHotelIdsString = joinListForInSqlLong(zoneAndHotelIds.stream().distinct().collect(Collectors.toList()));
                LOGGER.debug("zoneAndHotelIdsString:"+zoneAndHotelIdsString);
                List<HdMessageEntity> messageEntityList = queryMessages(zoneAndHotelIdsString, pageIndex, pageSize);
                for (HdMessageEntity hdMessageEntity : messageEntityList) {
                    Long amount = queryHdRoomMessageRecord(hdMessageEntity.getFromJID());
                    Long readCount = queryHdAgentMessageRecord(userName, hdMessageEntity.getToUser());
                    MessageModel messageModel = new MessageModel();
                    messageModel.setHotelName(hdMessageEntity.getHotelName());
                    messageModel.setRoomNum(hdMessageEntity.getRoomNum());
                    messageModel.setUnRead(amount - readCount);
                    messageModel.setCreateDate(hdMessageEntity.getCreationDate());
                    messageModel.setContent(hdMessageEntity.getStanza());
                    messageModels.add(messageModel);
                }
            }
        }
        return messageModels;
    }

    private Long queryHdRoomMessageRecord(String fromJID){
        return HdRoomMessageRecordDao.getInstance().findByRoomName(fromJID);
    }

    private Long queryHdAgentMessageRecord(String userName,String fromJID){
        return AgentMessageRecordDao.getInstance().findByAgentNameAndRoomName(userName,fromJID);
    }

    private List<HdMessageEntity> queryMessages(String zoneIds, Integer pageIndex, Integer pageSize){
        return HdMessageDao.getInstance().findByZoneIdsPaging(zoneIds,pageIndex,pageSize);
    }
}
