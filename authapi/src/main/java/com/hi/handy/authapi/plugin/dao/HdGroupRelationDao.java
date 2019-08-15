package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HdGroupRelationDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdGroupRelationDao.class);

  private HdGroupRelationDao() {}
  private static final HdGroupRelationDao INSTANCE = new HdGroupRelationDao();
  public static HdGroupRelationDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_RELACTIONID_AND_TYPE_SQL = "SELECT groupId FROM hdGroupRelation WHERE relationId = ? AND type = ?";

  public String searchByRelationId(Long relationId,String type) {
    String groupId = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_RELACTIONID_AND_TYPE_SQL);
      pstmt.setLong(1, relationId);
      pstmt.setString(2, type);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        groupId = rs.getString(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchByRelationId error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return groupId;
  }
}
