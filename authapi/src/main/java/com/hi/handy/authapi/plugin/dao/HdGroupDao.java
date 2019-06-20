package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.entity.HdGroupEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HdGroupDao extends BaseDao {

  private HdGroupDao() {
  }

  public static final HdGroupDao INSTANCE = new HdGroupDao();

  public static HdGroupDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_ID_SQL =  "SELECT * FROM hdGroup WHERE id = ?";

  public HdGroupEntity searchById(String id) {
    HdGroupEntity hdGroupEntity = new HdGroupEntity();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ID_SQL);
      pstmt.setString(1, id);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        hdGroupEntity.setId(rs.getString(1));
        hdGroupEntity.setName(rs.getString(2));
        hdGroupEntity.setIcon(rs.getString(3));
        hdGroupEntity.setType(rs.getString(4));
        hdGroupEntity.setWelcomeMessage(rs.getString(5));
        hdGroupEntity.setCreateDate(rs.getTimestamp(6));
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return hdGroupEntity;
  }
}
