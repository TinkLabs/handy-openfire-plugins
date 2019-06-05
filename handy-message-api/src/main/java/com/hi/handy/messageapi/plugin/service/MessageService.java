package com.hi.handy.messageapi.plugin.service;

import com.hi.handy.messageapi.plugin.domain.agentmessagerecord.AgentMessageRecordDao;
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
import com.hi.handy.messageapi.plugin.parameter.BaseParameter;
import com.hi.handy.messageapi.plugin.parameter.BaseParameter.UserType;
import com.hi.handy.messageapi.plugin.parameter.MessageParameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
  public static final MessageService INSTANCE = new MessageService();

  private MessageService() {
  }

  public static MessageService getInstance() {
    return INSTANCE;
  }

  public Object list(MessageParameter parameter){
    LOGGER.info("list"+parameter);
    checkParameter(parameter);
    if (parameter.getMessageType().equals(BaseParameter.MessageType.LIST)) {
      return chatListByPaging(parameter);
    } else if (parameter.getMessageType().equals(BaseParameter.MessageType.HISTORY)) {
      return historyList(parameter);
    }
    return null;
  }

  public List<MessageModel> chatListByPaging(MessageParameter parameter) {
    String userName = parameter.getUserName();
    HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByName(userName);
    if(hdUserPropertyEntity == null) return null;
    String type = hdUserPropertyEntity.getType();
    if (type.equals(UserType.AGENT.name())) {
      return getAgentChatListByPaging(hdUserPropertyEntity.getUserName(),hdUserPropertyEntity.getZoneId(),parameter.getPageIndex(),parameter.getPageSize());
    }
    return null;
  }

  public MessageHistoryModel historyList(MessageParameter parameter) {
    LOGGER.info("historyList"+parameter.toString());
    String userName = parameter.getUserName();
    HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByName(userName);
    if(hdUserPropertyEntity == null) return null;
    String type = hdUserPropertyEntity.getType();
    if (type.equals(UserType.AGENT.name())) {
      return getAgentHistoryList(userName,hdUserPropertyEntity.getHotelId(),hdUserPropertyEntity.getRoomNum(),parameter.getPageIndex(),parameter.getPageSize());
    }else if (type.equals(UserType.GUEST.name())) {
      return getGuestHistoryList(hdUserPropertyEntity.getHotelId(),hdUserPropertyEntity.getRoomNum(),parameter.getPageIndex(),parameter.getPageSize());
    }
    return null;
  }

  private List<MessageModel> getAgentChatListByPaging(String userName,Long zoneId,Integer pageIndex,Integer pageSize){
    List<MessageModel> messageModels = new ArrayList();
    List<HotelIdAndRoomNum> hotelIdAndRoomNums = HdUserPropertyDao.getInstance().searchHotelIdAndRoomNumByAgentName(userName,zoneId,pageIndex,pageSize);
    for(HotelIdAndRoomNum hotelIdAndRoomNum:hotelIdAndRoomNums ){
       HdRoomMessageRecordEntity hdRoomMessageRecordEntity = queryHdRoomMessageRecord(hotelIdAndRoomNum.getHotelId(),hotelIdAndRoomNum.getRoomNum());
       if(hdRoomMessageRecordEntity!=null){
         Long readCount = queryAgentMessageRecord(userName,hotelIdAndRoomNum.getHotelId(),hotelIdAndRoomNum.getRoomNum());
         String stanza = queryMessageById(hdRoomMessageRecordEntity.getMessageId());
         MessageModel messageModel = new MessageModel();
         messageModel.setHotelName(hdRoomMessageRecordEntity.getHotelName());
         messageModel.setRoomNum(hotelIdAndRoomNum.getRoomNum());
         messageModel.setCount(hdRoomMessageRecordEntity.getAmount() - readCount);
         messageModel.setCreateDate(hdRoomMessageRecordEntity.getUpdateDate());
         messageModel.setContent(stanza);
         messageModels.add(messageModel);
       }
    }
    return messageModels.stream().sorted(Comparator.comparing(MessageModel::getCreateDate).reversed()).collect(Collectors.toList());
  }

  private MessageHistoryModel getAgentHistoryList(String userName,Long hotelId,String roomNum,Integer pageIndex,Integer pageSize){
    Long readCount = queryAgentMessageRecord(userName,hotelId,roomNum);
    MessageHistoryModel messageHistoryModel = new MessageHistoryModel();
    messageHistoryModel.setReadCount(readCount);
    List<MessageStanzaAndCreationDate> hdMessageEntityList = queryMessageListByHotelIdAndRoomNum(hotelId,roomNum,pageIndex,pageSize);
    messageHistoryModel.setMessageStanzaAndCreationDates(hdMessageEntityList);
    return messageHistoryModel;
  }

  private MessageHistoryModel getGuestHistoryList(Long hotelId,String roomNum,Integer pageIndex,Integer pageSize){
    List<MessageStanzaAndCreationDate> hdMessageEntityList = queryMessageListByHotelIdAndRoomNum(hotelId,roomNum,pageIndex,pageSize);
    MessageHistoryModel messageHistoryModel = new MessageHistoryModel();
    messageHistoryModel.setMessageStanzaAndCreationDates(hdMessageEntityList);
    return messageHistoryModel;
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

  private Long queryAgentMessageRecord(String userName,Long hotelId, String roomNum){
    return AgentMessageRecordDao.getInstance().findByHotelIdAndNum(userName,hotelId,roomNum);
  }

  private String queryMessageById(String id){
    return HdMessageDao.getInstance().findStanzaById(id);
  }

  private List<MessageStanzaAndCreationDate> queryMessageListByHotelIdAndRoomNum(Long hotelId, String roomNum,Integer pageIndex,Integer pageSize){
    return HdMessageDao.getInstance().findByHotelIdAndNum(hotelId,roomNum,pageIndex,pageSize);
  }
}
