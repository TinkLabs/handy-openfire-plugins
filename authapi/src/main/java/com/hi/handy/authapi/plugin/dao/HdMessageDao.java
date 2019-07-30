package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class HdMessageDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);

  private HdMessageDao() {}
  public static final HdMessageDao INSTANCE = new HdMessageDao();
  public static HdMessageDao getInstance() {
    return INSTANCE;
  }

  public Boolean deleteBymessageId(String messageId){
    String sql = "DELETE FROM ofMessageArchive WHERE messageId = '" + messageId + "'";
    Connection con = null;
    PreparedStatement pstmt = null;
    Boolean result = Boolean.FALSE;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(sql);
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
