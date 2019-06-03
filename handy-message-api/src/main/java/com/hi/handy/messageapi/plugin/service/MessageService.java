package com.hi.handy.messageapi.plugin.service;

import com.hi.handy.messageapi.plugin.domain.agentmessagerecord.AgentMessageRecordDao;
import com.hi.handy.messageapi.plugin.domain.agentmessagerecord.AgentMessageRecordEntity;
import com.hi.handy.messageapi.plugin.domain.hduserproperty.HdUserPropertyDao;
import com.hi.handy.messageapi.plugin.domain.hduserproperty.HdUserPropertyEntity;
import com.hi.handy.messageapi.plugin.domain.message.HdMessageDao;
import com.hi.handy.messageapi.plugin.domain.roommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.messageapi.plugin.domain.roommessagerecord.HdRoomMessageRecordEntity;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import com.hi.handy.messageapi.plugin.model.HotelIdAndRoomNum;
import com.hi.handy.messageapi.plugin.model.MessageHistoryModel;
import com.hi.handy.messageapi.plugin.model.MessageModel;
import com.hi.handy.messageapi.plugin.model.MessageStanzaAndCreationDate;
import com.hi.handy.messageapi.plugin.parameter.BaseParameter.UserType;
import com.hi.handy.messageapi.plugin.parameter.MessageParameter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService {
  public static final MessageService INSTANCE = new MessageService();

  private MessageService() {
  }

  public static MessageService getInstance() {
    return INSTANCE;
  }

  public List<MessageModel> chatListByPaging(MessageParameter parameter) {
    checkParameter(parameter);
    String userName = parameter.getUserName();
    HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByName(userName);
    if(hdUserPropertyEntity == null) return null;
    String type = hdUserPropertyEntity.getType();
    if (type.equals(UserType.AGENT.name())) {
      return getAgentMessageList(hdUserPropertyEntity,parameter);
    } else if (type.equals(UserType.AGENT_ADMIN.name())) {
      return getAgentMessageList(hdUserPropertyEntity,parameter);
    } else if (type.equals(UserType.HOTEL.name())) {
      return getGuestMessageList(hdUserPropertyEntity);
    }else{
      return null;
    }
  }

  public MessageHistoryModel history(MessageParameter parameter) {
    checkParameter(parameter);
    String userName = parameter.getUserName();
    HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByName(userName);
    if(hdUserPropertyEntity == null) return null;
    String type = hdUserPropertyEntity.getType();
    if (type.equals(UserType.AGENT.name())) {
      return getAgentMessageHistory(hdUserPropertyEntity);
    }
    return null;
  }

  private List<MessageModel> getAgentMessageList(HdUserPropertyEntity hdUserPropertyEntity,MessageParameter parameter){
    List<MessageModel> messageModels = new ArrayList<MessageModel>();
    List<HotelIdAndRoomNum> hotelIdAndRoomNums = HdUserPropertyDao.getInstance().searchHotelIdAndRoomNumByAgentName(hdUserPropertyEntity.getUserName(),hdUserPropertyEntity.getZoneId());
    for(HotelIdAndRoomNum hotelIdAndRoomNum:hotelIdAndRoomNums ){
       HdRoomMessageRecordEntity hdRoomMessageRecordEntity = queryHdRoomMessageRecord(hotelIdAndRoomNum.getHotelId(),hotelIdAndRoomNum.getRoomNum());
       if(hdRoomMessageRecordEntity!=null){
         AgentMessageRecordEntity agentMessageRecordEntity = queryAgentMessageRecord(hotelIdAndRoomNum.getHotelId(),hotelIdAndRoomNum.getRoomNum());
         String stanza = queryMessageById(hdRoomMessageRecordEntity.getMessageId());
         MessageModel messageModel = new MessageModel();
         messageModel.setHotelName(hdRoomMessageRecordEntity.getHotelName());
         messageModel.setRoomNum(hotelIdAndRoomNum.getRoomNum());
         messageModel.setCount(hdRoomMessageRecordEntity.getAmount() - agentMessageRecordEntity.getReadCount());
         messageModel.setCreateDate(hdRoomMessageRecordEntity.getUpdateDate());
         messageModel.setContent(stanza);
         messageModels.add(messageModel);
       }
    }
    return messageModels.stream().sorted(Comparator.comparing(MessageModel::getCreateDate).reversed()).collect(Collectors.toList());
  }

  private MessageHistoryModel getAgentMessageHistory(HdUserPropertyEntity hdUserPropertyEntity){
    AgentMessageRecordEntity agentMessageRecordEntity = queryAgentMessageRecord(hdUserPropertyEntity.getHotelId(),hdUserPropertyEntity.getRoomNum());
    MessageHistoryModel messageHistoryModel = new MessageHistoryModel();
    messageHistoryModel.setReadCount(agentMessageRecordEntity.getReadCount());
    List<MessageStanzaAndCreationDate> hdMessageEntityList = queryMessageListByHotelIdAndRoomNum(hdUserPropertyEntity.getHotelId(),hdUserPropertyEntity.getRoomNum());
    messageHistoryModel.setMessageStanzaAndCreationDates(hdMessageEntityList);
    return messageHistoryModel;
  }

  private List<MessageModel> getAgentAdminMessageList(HdUserPropertyEntity hdUserPropertyEntity){
    return null;
  }

  private List<MessageModel> getGuestMessageList(HdUserPropertyEntity hdUserPropertyEntity){
    // 查询所有此区域中的agent 对其讲的话
    return null;
  }

  private List<MessageModel> getHotelMessageList(HdUserPropertyEntity hdUserPropertyEntity){
    return null;
  }

  private void checkParameter(MessageParameter parameter) {
    if (StringUtils.isBlank(parameter.getUserName())) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "userName is needed");
    }
    if (parameter.getPageIndex() == null) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "pageIndex is needed");
    }
    if (parameter.getPageIndex() <=0 ) {
      throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "pageIndex is wrong");
    }
    if (parameter.getPageSize() == null) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "pageSize is needed");
    }
    if (parameter.getPageSize() <=0 ) {
      throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "pageSize is wrong");
    }
  }

  private HdRoomMessageRecordEntity queryHdRoomMessageRecord(Long hotelId, String roomNum){
    return HdRoomMessageRecordDao.getInstance().findByHotelIdAndNum(hotelId,roomNum);
  }

  private AgentMessageRecordEntity queryAgentMessageRecord(Long hotelId, String roomNum){
    return AgentMessageRecordDao.getInstance().findByHotelIdAndNum(hotelId,roomNum);
  }

  private String queryMessageById(String id){
    return HdMessageDao.getInstance().findStanzaById(id);
  }

  private List<MessageStanzaAndCreationDate> queryMessageListByHotelIdAndRoomNum(Long hotelId, String roomNum){
    return HdMessageDao.getInstance().findByHotelIdAndNum(hotelId,roomNum);
  }
}
