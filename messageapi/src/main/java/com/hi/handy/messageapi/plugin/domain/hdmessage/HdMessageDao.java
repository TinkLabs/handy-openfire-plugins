package com.hi.handy.messageapi.plugin.domain.hdmessage;

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

@SuppressWarnings("Duplicates")
public class HdMessageDao extends BaseDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);

  private HdMessageDao() {
  }

  public static final HdMessageDao INSTANCE = new HdMessageDao();

  public static HdMessageDao getInstance() {
    return INSTANCE;
  }


  public List<HdMessageEntity> findByZoneIdsPaging(String zoneOrHotelId,Integer pageIndex,Integer pageSize){
    String SEARCH_BY_ZONEIDS_PAGING_SQL = "SELECT * FROM hdMessage WHERE zoneId in ("+zoneOrHotelId+") OR hotelId in ("+zoneOrHotelId+") ORDER BY creationDate DESC LIMIT "+pageIndex+","+pageSize;
    LOGGER.debug("SEARCH_BY_ZONEIDS_PAGING_SQL:"+SEARCH_BY_ZONEIDS_PAGING_SQL);
    List<HdMessageEntity> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ZONEIDS_PAGING_SQL);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        HdMessageEntity hdMessageEntity = new HdMessageEntity();
        hdMessageEntity.setId(rs.getString(1));
        hdMessageEntity.setMessageId(rs.getString(2));
        hdMessageEntity.setZoneId(rs.getLong(3));
        hdMessageEntity.setZoneName(rs.getString(4));
        hdMessageEntity.setHotelId(rs.getLong(5));
        hdMessageEntity.setHotelName(rs.getString(6));
        hdMessageEntity.setRoomNum(rs.getString(7));
        hdMessageEntity.setFromUser(rs.getString(8));
        hdMessageEntity.setFromJID(rs.getString(9));
        hdMessageEntity.setToUser(rs.getString(10));
        hdMessageEntity.setToJID(rs.getString(11));
        hdMessageEntity.setCreationDate(rs.getTimestamp(12));
        hdMessageEntity.setStanza(rs.getString(13));
        hdMessageEntity.setDeviceUserId(rs.getString(14));
        result.add(hdMessageEntity);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findByZoneIdsPaging error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
