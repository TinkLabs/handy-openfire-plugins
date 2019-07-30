package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class OfMucRoomDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(OfMucRoomDao.class);

  private OfMucRoomDao() {}
  public static final OfMucRoomDao INSTANCE = new OfMucRoomDao();
  public static OfMucRoomDao getInstance() {
    return INSTANCE;
  }

  private static final String UPDATE_ROOM_NAME_SQL           = "UPDATE ofMucRoom t SET name = ?,modification = ? AND WHERE name = ? AND ";

  public Boolean updateRoomNameByRoomName(String newRoomName,String oldRoomName){
    Connection con = null;
    PreparedStatement pstmt = null;
    Boolean result = Boolean.FALSE;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_ROOM_NAME_SQL);
      pstmt.setString(1, newRoomName);
      pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      pstmt.setString(3, oldRoomName);
      if( pstmt.executeUpdate()>0 ){
        result = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchLoginCount error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return result;
  }
}
