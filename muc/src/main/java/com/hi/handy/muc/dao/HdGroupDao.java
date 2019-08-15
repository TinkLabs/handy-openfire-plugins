package com.hi.handy.muc.dao;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HdGroupDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdGroupDao.class);

  private HdGroupDao() {}
  private static final HdGroupDao INSTANCE = new HdGroupDao();
  public static HdGroupDao getInstance() {
    return INSTANCE;
  }

  public List<String> searchNamesByIds(String ids) {
    String SEARCH_GROUP_NAME_BY_IDS_SQL = "SELECT DISTINCT name FROM hdGroup WHERE id in ("+ids+")";
    List<String> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_GROUP_NAME_BY_IDS_SQL);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getString(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("searchNamesByIds error ",e);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

}
