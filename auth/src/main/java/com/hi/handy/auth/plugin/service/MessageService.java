package com.hi.handy.auth.plugin.service;

import com.hi.handy.auth.plugin.dao.HdUserPropertyDao;
import com.hi.handy.auth.plugin.dao.OfOfflineDao;
import com.hi.handy.auth.plugin.entity.HdUserPropertyEntity;
import com.hi.handy.auth.plugin.exception.BusinessException;
import com.hi.handy.auth.plugin.exception.ExceptionConst;
import com.hi.handy.auth.plugin.model.HotelIdAndRoomNum;
import com.hi.handy.auth.plugin.model.MessageModel;
import com.hi.handy.auth.plugin.model.OfOfflineMessageModel;
import com.hi.handy.auth.plugin.parameter.BaseParameter.UserType;
import com.hi.handy.auth.plugin.parameter.MessageParameter;
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

  public List<MessageModel> list(MessageParameter parameter) {
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

  private List<MessageModel> getAgentMessageList(HdUserPropertyEntity hdUserPropertyEntity,MessageParameter parameter){
    // 当前 agent 及 子级的酒店和房间号
    List<MessageModel> messageModels = new ArrayList<MessageModel>();
    List<HotelIdAndRoomNum> hotelIdAndRoomNums = HdUserPropertyDao.getInstance().searchHotelIdAndRoomNumByAgentName(hdUserPropertyEntity.getUserName(),hdUserPropertyEntity.getZoneId());
    for(HotelIdAndRoomNum hotelIdAndRoomNum:hotelIdAndRoomNums ){
       List<OfOfflineMessageModel> ofOfflineMessageModels = queryOfOffine(hotelIdAndRoomNum.getHotelId(),hotelIdAndRoomNum.getRoomNum());
       if(ofOfflineMessageModels!=null && ofOfflineMessageModels.size()>0){
         // 最新的一条数据
         OfOfflineMessageModel ofOfflineMessageModelLast = ofOfflineMessageModels.get(0);
         MessageModel messageModel = new MessageModel();
         messageModel.setHotelName(ofOfflineMessageModelLast.getHotelName());
         messageModel.setRoomNum(hotelIdAndRoomNum.getRoomNum());
         messageModel.setCount(ofOfflineMessageModels.size());
         messageModel.setCreateDate(ofOfflineMessageModelLast.getCreationDate());
         messageModel.setContent(ofOfflineMessageModelLast.getStanza());
         messageModels.add(messageModel);
       }
    }
    return messageModels.stream().sorted(Comparator.comparing(MessageModel::getCreateDate).reversed()).collect(Collectors.toList());
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

  private List<OfOfflineMessageModel> queryOfOffine(Integer hotelName, String roomNum){
    return OfOfflineDao.getInstance().searchByUserName(hotelName,roomNum);
  }
}
