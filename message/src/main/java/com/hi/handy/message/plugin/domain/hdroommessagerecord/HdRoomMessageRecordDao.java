package com.hi.handy.message.plugin.domain.hdroommessagerecord;

import com.hi.handy.message.plugin.domain.BaseDao;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

@SuppressWarnings("Duplicates")
public class HdRoomMessageRecordDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdRoomMessageRecordDao.class);
  private HdRoomMessageRecordDao() {
  }

  public static final HdRoomMessageRecordDao INSTANCE = new HdRoomMessageRecordDao();

  public static HdRoomMessageRecordDao getInstance() {
    return INSTANCE;
  }

  private static final String CREATE_SQL = "INSERT INTO hdRoomMessageRecord (id, roomName, amount, updateDate) VALUES (?, ?, ?, ?)";
  private static final String SEARCH_BY_ROOMNAME_SQL = "SELECT * FROM hdRoomMessageRecord WHERE roomName = ?";
  private static final String UPDATE_SQL = "UPDATE hdRoomMessageRecord t SET amount = ?, updateDate = ? WHERE id = ?";

  public void create(HdRoomMessageRecordEntity hdRoomMessageRecordEntity){
    Connection con = null;
    PreparedStatement statement = null;
    try {
      con = DbConnectionManager.getConnection();
      statement = con.prepareStatement(CREATE_SQL);
      statement.setString(1, hdRoomMessageRecordEntity.getId());
      statement.setString(2, hdRoomMessageRecordEntity.getRoomName());
      statement.setLong(3, hdRoomMessageRecordEntity.getAmount());
      statement.setTimestamp(4, hdRoomMessageRecordEntity.getUpdateDate());
      statement.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("create error", ex);
    } finally {
      DbConnectionManager.closeConnection(statement, con);
    }
  }

  public HdRoomMessageRecordEntity findByRoomName(String roomName){
    HdRoomMessageRecordEntity result = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ROOMNAME_SQL);
      pstmt.setString(1, roomName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = new HdRoomMessageRecordEntity();
        result.setId(rs.getString(1));
        result.setRoomName(rs.getString(2));
        result.setAmount(rs.getLong(3));
        result.setUpdateDate(rs.getTimestamp(4));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findByRoomName error", ex);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

  public void updateAmountAndUpdateDateById(String id, Long amount, Timestamp updateDate){
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_SQL);
      pstmt.setLong(i++, amount);
      pstmt.setTimestamp(i++, updateDate);
      pstmt.setString(i++, id);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("updateById error", ex);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }
}
