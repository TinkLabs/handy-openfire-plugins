package com.hi.handy.messageapi.plugin.domain.hduserproperty;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HdUserPropertyDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdUserPropertyDao.class);
  private HdUserPropertyDao() {
  }

  public static final HdUserPropertyDao INSTANCE = new HdUserPropertyDao();

  public static HdUserPropertyDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_NAME_SQL = "SELECT * FROM hdUserProperty WHERE type = 'AGENT' AND userName = ? ";

  @SuppressWarnings("Duplicates")
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
        result.setDisplayName(rs.getString(2));
        result.setPassword(rs.getString(3));
        result.setZoneId(rs.getLong(4));
        result.setZoneName(rs.getString(5));
        result.setHotelId(rs.getLong(6));
        result.setHotelName(rs.getString(7));
        result.setRoomNum(rs.getString(8));
        result.setType(rs.getString(9));
        result.setCreationDate(rs.getTimestamp(10));
        result.setModificationDamodificationDate(rs.getTimestamp(11));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("searchByName error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

}
