package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OfMessageArchiveDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(OfMessageArchiveDao.class);

  private OfMessageArchiveDao() {}
  private static final OfMessageArchiveDao INSTANCE = new OfMessageArchiveDao();
  public static OfMessageArchiveDao getInstance() {
    return INSTANCE;
  }

  public Boolean deleteBymessageId(String messageId){
    String sql = "DELETE FROM ofMessageArchive WHERE stanza like '%" + messageId + "%'";
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
      LOGGER.error("deleteBymessageId error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return result;
  }
}
