package com.hi.handy.messageapi.plugin.domain.roommessagerecord;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.domain.message.HdMessageDao;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HdRoomMessageRecordDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);
  private HdRoomMessageRecordDao() {
  }

  public static final HdRoomMessageRecordDao INSTANCE = new HdRoomMessageRecordDao();

  public static HdRoomMessageRecordDao getInstance() {
    return INSTANCE;
  }

  private static final String CREATE_SQL =
          "INSERT INTO hdRoomMessageRecord (hotelId, hotelName, roomNum, messageId, amount, updateDate) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String SEARCH_BY_HOTELID_AND_ROOMNUM_SQL =
          "SELECT * FROM hdRoomMessageRecord WHERE hotelId = ? AND roomNum = ?";
  private static final String UPDATE_SQL =
          "UPDATE hdRoomMessageRecord t SET messageId = ?, amount = ?, updateDate = ? "
                  + "WHERE id = ?";

  public void create(HdRoomMessageRecordEntity hdRoomMessageRecordEntity){
    Connection con = null;
    PreparedStatement statement = null;
    try {
      con = DbConnectionManager.getConnection();
      statement = con.prepareStatement(CREATE_SQL);
      statement.setLong(1, hdRoomMessageRecordEntity.getHotelId());
      statement.setString(2, hdRoomMessageRecordEntity.getHotelName());
      statement.setString(3, hdRoomMessageRecordEntity.getRoomNum());
      statement.setString(4, hdRoomMessageRecordEntity.getMessageId());
      statement.setLong(5, hdRoomMessageRecordEntity.getAmount());
      statement.setString(6, hdRoomMessageRecordEntity.getUpdateDate());
      statement.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("message save error", ex);
    } finally {
      DbConnectionManager.closeConnection(statement, con);
    }
  }

  public HdRoomMessageRecordEntity findByHotelIdAndNum(Long hotelId, String roomNum){
    HdRoomMessageRecordEntity result = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_HOTELID_AND_ROOMNUM_SQL);
      pstmt.setLong(1, hotelId);
      pstmt.setString(2, roomNum);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = new HdRoomMessageRecordEntity();
        result.setId(rs.getString(1));
        result.setHotelId(rs.getInt(2));
        result.setHotelName(rs.getString(3));
        result.setRoomNum(rs.getString(4));
        result.setAmount(rs.getLong(5));
        result.setMessageId(rs.getString(6));
        result.setUpdateDate(rs.getString(7));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("message save error", ex);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

  public void updateByHotelIdAndNum(HdRoomMessageRecordEntity hdRoomMessageRecordEntity){
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_SQL);
      pstmt.setString(i++, hdRoomMessageRecordEntity.getMessageId());
      pstmt.setLong(i++, hdRoomMessageRecordEntity.getAmount());
      pstmt.setString(i++, hdRoomMessageRecordEntity.getUpdateDate());
      pstmt.setString(i++, hdRoomMessageRecordEntity.getId());
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("message save error", ex);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }
}
