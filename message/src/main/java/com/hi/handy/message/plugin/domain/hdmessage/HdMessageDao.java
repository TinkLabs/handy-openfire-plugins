package com.hi.handy.message.plugin.domain.hdmessage;

import com.hi.handy.message.plugin.domain.BaseDao;
import com.hi.handy.message.plugin.domain.hdroommessagerecord.HdRoomMessageRecordEntity;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

@SuppressWarnings("Duplicates")
public class HdMessageDao extends BaseDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);

  private HdMessageDao() {
  }

  public static final HdMessageDao INSTANCE = new HdMessageDao();

  public static HdMessageDao getInstance() {
    return INSTANCE;
  }

  private static final String CREATE_HISTORY_SQL = "INSERT INTO hdMessage (id, messageId, zoneId, hotelId, hotelName, roomNum, deviceUserId, fromUser, fromJID, toUser, toJID, creationDate, stanza) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
  private static final String UPDATE_SQL = "UPDATE hdMessage t SET creationDate = ? , stanza = ? WHERE id = ?";
  private static final String SEARCH_BY_TOJID_SQL = "SELECT id from hdMessage t WHERE t.toJID = ?";

  public void create(HdMessageEntity hdMessageEntity) {
    Connection con = null;
    PreparedStatement statement = null;
    try {
      con = DbConnectionManager.getConnection();
      statement = con.prepareStatement(CREATE_HISTORY_SQL);
      statement.setString(1, hdMessageEntity.getId());
      statement.setString(2,hdMessageEntity.getMessageId());
      statement.setLong(3, hdMessageEntity.getZoneId());
      statement.setLong(4, hdMessageEntity.getHotelId());
      statement.setString(5, hdMessageEntity.getHotelName());
      statement.setString(6, hdMessageEntity.getRoomNum());
      statement.setString(7, hdMessageEntity.getDeviceUserId());
      statement.setString(8, hdMessageEntity.getFromUser());
      statement.setString(9, hdMessageEntity.getFromJID());
      statement.setString(10, hdMessageEntity.getToUser());
      statement.setString(11, hdMessageEntity.getToJID());
      statement.setTimestamp(12, hdMessageEntity.getCreationDate());
      statement.setString(13, hdMessageEntity.getStanza());
      statement.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("create error", ex);
    } finally {
      DbConnectionManager.closeConnection(statement, con);
    }
  }

  public void updateStanzaById(String id, String stanza, Timestamp creationDate){
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_SQL);
      pstmt.setTimestamp(i++, creationDate);
      pstmt.setString(i++, stanza);
      pstmt.setString(i++, id);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("updateStanzaById error", ex);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public String findBytoJID(String toJID){
    String messageId=null;
    HdRoomMessageRecordEntity result = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_TOJID_SQL);
      pstmt.setString(1, toJID);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        messageId = rs.getString(1);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findBytoJID error", ex);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return messageId;
  }
}
