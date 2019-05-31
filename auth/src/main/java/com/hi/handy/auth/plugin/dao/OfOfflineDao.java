package com.hi.handy.auth.plugin.dao;

import com.hi.handy.auth.plugin.exception.BusinessException;
import com.hi.handy.auth.plugin.exception.ExceptionConst;
import com.hi.handy.auth.plugin.model.OfOfflineMessageModel;
import org.jivesoftware.database.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OfOfflineDao extends BaseDao {

  private OfOfflineDao() {
  }

  public static final OfOfflineDao INSTANCE = new OfOfflineDao();

  public static OfOfflineDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_USERNAME =
      "SELECT h.hotelName,o.* FROM ofOffline o,hdUserProperty h WHERE o.username = h.userName AND h.hotelId = ? AND h.roomNum = ? ORDER BY o.creationDate DESC";

  public List<OfOfflineMessageModel> searchByUserName(Integer hotelId, String roomNum) {
    List<OfOfflineMessageModel> result = new ArrayList<OfOfflineMessageModel>();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_USERNAME);
      pstmt.setInt(1, hotelId);
      pstmt.setString(2, roomNum);
      System.out.println(pstmt);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        OfOfflineMessageModel ofOfflineMessageModel = new OfOfflineMessageModel();
        ofOfflineMessageModel.setHotelName(rs.getString(1));
        ofOfflineMessageModel.setUserName(rs.getString(2));
        ofOfflineMessageModel.setMessageID(rs.getLong(3));
        ofOfflineMessageModel.setCreationDate(rs.getString(4));
        ofOfflineMessageModel.setMessageSize(rs.getInt(5));
        ofOfflineMessageModel.setStanza(rs.getString(6));
        result.add(ofOfflineMessageModel);
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
