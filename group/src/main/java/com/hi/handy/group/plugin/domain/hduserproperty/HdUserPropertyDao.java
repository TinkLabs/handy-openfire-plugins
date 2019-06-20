package com.hi.handy.group.plugin.domain.hduserproperty;

import com.hi.handy.group.plugin.domain.BaseDao;
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

  private static final String COUNT_SQL                   = "SELECT count(1) FROM hdUserProperty WHERE userName = ?";

  public Long countByUserName(String userName) {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Long count = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(COUNT_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        count = rs.getLong(1);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("countByUserName error", ex);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return count;
  }
}
