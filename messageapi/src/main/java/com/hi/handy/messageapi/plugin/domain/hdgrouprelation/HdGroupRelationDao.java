package com.hi.handy.messageapi.plugin.domain.hdgrouprelation;

import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import com.hi.handy.messageapi.plugin.domain.BaseDao;
import org.jivesoftware.database.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HdGroupRelationDao extends BaseDao {

  private HdGroupRelationDao() {
  }

  public static final HdGroupRelationDao INSTANCE = new HdGroupRelationDao();

  public static HdGroupRelationDao getInstance() {
    return INSTANCE;
  }

  public List<Long> searchZoneIdsByGroupIds(String groupIds) {
    String sql = "SELECT relationId FROM hdGroupRelation WHERE groupId in (" + groupIds + ")";

    List<Long> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getLong(1));
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
