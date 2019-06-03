package com.hi.handy.messageapi.plugin.domain.agentmessagerecord;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.domain.message.HdMessageDao;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AgentMessageRecordDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);
  private AgentMessageRecordDao() {
  }

  public static final AgentMessageRecordDao INSTANCE = new AgentMessageRecordDao();

  public static AgentMessageRecordDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_HOTELID_AND_ROOMNUM_SQL =
          "SELECT * FROM hdRoomMessageRecord WHERE hotelId = ? AND roomNum = ?";

  public AgentMessageRecordEntity findByHotelIdAndNum(Long hotelId, String roomNum){
    AgentMessageRecordEntity result = null;
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
        result = new AgentMessageRecordEntity();
        result.setHotelId(rs.getLong(1));
        result.setHotelName(rs.getString(2));
        result.setRoomNum(rs.getString(3));
        result.setReadCount(rs.getLong(4));
        result.setUpdateDate(rs.getString(5));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findByHotelIdAndNum error", ex);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
