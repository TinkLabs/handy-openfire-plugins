package com.hi.handy.messageapi.plugin.domain.roommessagerecord;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HdRoomMessageRecordDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdRoomMessageRecordDao.class);
  private HdRoomMessageRecordDao() {
  }

  public static final HdRoomMessageRecordDao INSTANCE = new HdRoomMessageRecordDao();

  public static HdRoomMessageRecordDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_HOTELID_AND_ROOMNUM_SQL =
          "SELECT * FROM hdRoomMessageRecord WHERE hotelId = ? AND roomNum = ?";

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
        result.setMessageId(rs.getString(5));
        result.setAmount(rs.getLong(6));
        result.setUpdateDate(rs.getTimestamp(7));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findByHotelIdAndNum error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
