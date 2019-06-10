package com.hi.handy.message.plugin.domain.hduserproperty;

import com.hi.handy.message.plugin.domain.BaseDao;
import com.hi.handy.message.plugin.domain.message.HdMessageDao;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings("Duplicates")
public class HdUserPropertyDao extends BaseDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);

  private HdUserPropertyDao() {
  }

  public static final HdUserPropertyDao INSTANCE = new HdUserPropertyDao();

  public static HdUserPropertyDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_NAME_SQL =
          "SELECT * FROM hdUserProperty WHERE userName = ?";

  public HdUserPropertyEntity searchByName(String userName) {
    HdUserPropertyEntity result = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_NAME_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = new HdUserPropertyEntity();
        result.setUserName(rs.getString(1));
        result.setZoneId(rs.getLong(2));
        result.setZoneName(rs.getString(3));
        result.setHotelId(rs.getLong(4));
        result.setHotelName(rs.getString(5));
        result.setRoomNum(rs.getString(6));
        result.setType(rs.getString(7));
        result.setRoomAmount(rs.getLong(8));
        result.setCreationDate(rs.getTimestamp(9));
        result.setModificationDamodificationDate(rs.getTimestamp(10));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("userProperty searchByName error", ex);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return result;
  }
}
