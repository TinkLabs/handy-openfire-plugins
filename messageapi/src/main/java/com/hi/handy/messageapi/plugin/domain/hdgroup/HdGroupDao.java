package com.hi.handy.messageapi.plugin.domain.hdgroup;


import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
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
    HdGroupEntity userRoom = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ID_SQL);
      pstmt.setString(1, id);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        userRoom = new HdGroupEntity();
        userRoom.setId(rs.getString(1));
        userRoom.setName(rs.getString(2));
        userRoom.setIcon(rs.getString(3));
        userRoom.setType(rs.getString(4));
        userRoom.setCreateDate(rs.getTimestamp(5));
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return userRoom;
  }
}
