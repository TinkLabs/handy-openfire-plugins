package com.hi.handy.messageapi.plugin.domain.hdgrouprelation;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HdGroupRelationDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdGroupRelationDao.class);
  private HdGroupRelationDao() {
  }

  public static final HdGroupRelationDao INSTANCE = new HdGroupRelationDao();

  public static HdGroupRelationDao getInstance() {
    return INSTANCE;
  }

  String SEARCH_SQL = "SELECT relationId FROM hdGroupRelation WHERE groupId = ?";

  public List<Long> searchHotelIdOrZoneIdByGroupId(String groupId) {


    List<Long> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_SQL);
      pstmt.setString(1, groupId);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getLong(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchHotelIdOrZoneIdByGroupId error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
