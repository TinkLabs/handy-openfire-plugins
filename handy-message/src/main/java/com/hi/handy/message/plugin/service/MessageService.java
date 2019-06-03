package com.hi.handy.message.plugin.service;

import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyDao;
import com.hi.handy.message.plugin.domain.hduserproperty.HdUserPropertyEntity;
import com.hi.handy.message.plugin.domain.message.HdMessageDao;
import com.hi.handy.message.plugin.domain.message.HdMessageEntity;
import com.hi.handy.message.plugin.domain.roommessagerecord.HdRoomMessageRecordDao;
import com.hi.handy.message.plugin.domain.roommessagerecord.HdRoomMessageRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import java.util.UUID;

public class MessageService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
  public static final MessageService INSTANCE = new MessageService();

  private MessageService() {
  }

  public static MessageService getInstance() {
    return INSTANCE;
  }

  public void save(Packet packet){
    try {
      Message message = (Message) packet;
      HdMessageEntity hdMessageEntity = new HdMessageEntity();
      String messageId = UUID.randomUUID().toString();
      hdMessageEntity.setId(messageId);
      hdMessageEntity.setFromUser(packet.getFrom().getNode());
      hdMessageEntity.setFromJID(packet.getFrom().toBareJID());
      hdMessageEntity.setToUser(packet.getTo().getNode());
      hdMessageEntity.setToJID(packet.getTo().toBareJID());
      hdMessageEntity.setCreationDate(String.valueOf(System.currentTimeMillis()));
      hdMessageEntity.setStanza(message.getElement().asXML());
      HdUserPropertyEntity hdUserProperty = HdUserPropertyDao.getInstance().searchByName(hdMessageEntity.getFromUser());
      if (hdUserProperty != null) {
        HdMessageDao.getInstance().save(hdMessageEntity);
        HdRoomMessageRecordEntity roomMessageRecord = HdRoomMessageRecordDao.getInstance().findByHotelIdAndNum(hdUserProperty.getHotelId(), hdUserProperty.getRoomNum());
        if (roomMessageRecord == null) {
          HdRoomMessageRecordEntity hdRoomMessageRecordEntity = new HdRoomMessageRecordEntity();
          hdRoomMessageRecordEntity.setId(UUID.randomUUID().toString());
          hdRoomMessageRecordEntity.setHotelId(hdUserProperty.getHotelId());
          hdRoomMessageRecordEntity.setHotelName(hdUserProperty.getHotelName());
          hdRoomMessageRecordEntity.setRoomNum(hdUserProperty.getRoomNum());
          hdRoomMessageRecordEntity.setAmount(0l);
          hdRoomMessageRecordEntity.setMessageId(message.getID());
          hdRoomMessageRecordEntity.setUpdateDate(String.valueOf(System.currentTimeMillis()));
          HdRoomMessageRecordDao.getInstance().create(hdRoomMessageRecordEntity);
        } else {
          HdRoomMessageRecordEntity hdRoomMessageRecordEntity = new HdRoomMessageRecordEntity();
          hdRoomMessageRecordEntity.setAmount(hdRoomMessageRecordEntity.getAmount() + 1);
          hdRoomMessageRecordEntity.setMessageId(messageId);
          hdRoomMessageRecordEntity.setUpdateDate(String.valueOf(System.currentTimeMillis()));
          HdRoomMessageRecordDao.getInstance().updateByHotelIdAndNum(hdRoomMessageRecordEntity);
        }
      } else {
        LOGGER.info(packet.getFrom().getNode() + "is not register");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("message save error", ex);
    }
  }
}
