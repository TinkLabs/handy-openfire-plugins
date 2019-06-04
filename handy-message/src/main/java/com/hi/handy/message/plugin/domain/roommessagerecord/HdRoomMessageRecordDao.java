package com.hi.handy.message.plugin.domain.roommessagerecord;

import com.hi.handy.message.plugin.domain.BaseDao;
import com.hi.handy.message.plugin.domain.message.HdMessageDao;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings("Duplicates")
public class HdRoomMessageRecordDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);
  private HdRoomMessageRecordDao() {
  }

  public static final HdRoomMessageRecordDao INSTANCE = new HdRoomMessageRecordDao();

  public static HdRoomMessageRecordDao getInstance() {
    return INSTANCE;
  }

  private static final String CREATE_SQL =
          "INSERT INTO hdRoomMessageRecord (id,hotelId, hotelName, roomNum, messageId, amount, updateDate) VALUES (?,?, ?, ?, ?, ?, ?)";
  private static final String SEARCH_BY_HOTELID_AND_ROOMNUM_SQL =
          "SELECT * FROM hdRoomMessageRecord WHERE hotelId = ? AND roomNum = ?";
  private static final String UPDATE_SQL =
          "UPDATE hdRoomMessageRecord t SET messageId = ?, amount = ?, updateDate = ? WHERE id = ?";

  public void create(HdRoomMessageRecordEntity hdRoomMessageRecordEntity){
    Connection con = null;
    PreparedStatement statement = null;
    try {
      con = DbConnectionManager.getConnection();
      statement = con.prepareStatement(CREATE_SQL);
      statement.setString(1, hdRoomMessageRecordEntity.getId());
      statement.setLong(2, hdRoomMessageRecordEntity.getHotelId());
      statement.setString(3, hdRoomMessageRecordEntity.getHotelName());
      statement.setString(4, hdRoomMessageRecordEntity.getRoomNum());
      statement.setString(5, hdRoomMessageRecordEntity.getMessageId());
      statement.setLong(6, hdRoomMessageRecordEntity.getAmount());
      statement.setString(7, hdRoomMessageRecordEntity.getUpdateDate());
      statement.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("roomMessageRecord create error", ex);
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
      LOGGER.info("roomMessageRecord findByHotelIdAndNum PreparedStatement", pstmt);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = new HdRoomMessageRecordEntity();
        result.setId(rs.getString(1));
        result.setHotelId(rs.getLong(2));
        result.setHotelName(rs.getString(3));
        result.setRoomNum(rs.getString(4));
        result.setMessageId(rs.getString(5));
        result.setAmount(rs.getLong(6));
        result.setUpdateDate(rs.getString(7));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("roomMessageRecord findByHotelIdAndNum error", ex);
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
      LOGGER.info("roomMessageRecord updateByHotelIdAndNum PreparedStatement", pstmt);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("roomMessageRecord updateByHotelIdAndNum error", ex);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }
}
