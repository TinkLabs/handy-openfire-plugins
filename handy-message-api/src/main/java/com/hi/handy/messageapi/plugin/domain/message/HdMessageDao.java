package com.hi.handy.messageapi.plugin.domain.message;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import com.hi.handy.messageapi.plugin.model.MessageStanzaAndCreationDate;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class HdMessageDao extends BaseDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);

  private HdMessageDao() {
  }

  public static final HdMessageDao INSTANCE = new HdMessageDao();

  public static HdMessageDao getInstance() {
    return INSTANCE;
  }

  private static final String CREATE_HISTORY_SQL =
          "INSERT INTO hdMessage (fromUser, toUser, fromJID, toJID, creationDate, stanza) " +
                  "VALUES (?, ?, ?, ?, ?, ?)";

  private static final String SEARCH_BY_ID_SQL =
          "SELECT stanza FROM hdMessage WHERE id = ?";

  private static final String SEARCH_BY_HOTELID_AND_ROOMNUM_SQL =
          "SELECT stanza,creationDate FROM hdMessage WHERE hotelId = ? AND roomNum = ? ORDER BY creationDate ASC LIMIT ?,?";

  public String findStanzaById(String id){
    String stanza = "";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ID_SQL);
      pstmt.setString(1, id);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        stanza = rs.getString(1);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findStanzaById error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return stanza;
  }

  public List<MessageStanzaAndCreationDate> findByHotelIdAndNum(Long hotelId, String roomNum,Integer pageIndex,Integer pageSize){
    List<MessageStanzaAndCreationDate> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_HOTELID_AND_ROOMNUM_SQL);
      pstmt.setLong(1, hotelId);
      pstmt.setString(2, roomNum);
      pstmt.setInt(3, pageIndex-1);
      pstmt.setInt(4, pageSize);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        MessageStanzaAndCreationDate messageStanzaAndCreationDate = new MessageStanzaAndCreationDate();
        messageStanzaAndCreationDate.setStanza(rs.getString(1));
        messageStanzaAndCreationDate.setCreationDate(rs.getString(2));
        result.add(messageStanzaAndCreationDate);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findByHotelIdAndNum error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
