package com.hi.handy.message.plugin.domain.message;

import com.hi.handy.message.plugin.domain.BaseDao;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
          "INSERT INTO hdMessage (id,hotelId,hotelName,roomNum,messageId,fromUser, toUser, fromJID, toJID, creationDate, stanza) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  public void create(HdMessageEntity hdMessageEntity) {
    Connection con = null;
    PreparedStatement statement = null;
    try {
      con = DbConnectionManager.getConnection();
      statement = con.prepareStatement(CREATE_HISTORY_SQL);
      statement.setString(1, hdMessageEntity.getId());
      statement.setLong(2, hdMessageEntity.getHotelId());
      statement.setString(3, hdMessageEntity.getHotelName());
      statement.setString(4, hdMessageEntity.getRoomNum());
      statement.setString(5, hdMessageEntity.getMessageId());
      statement.setString(6, hdMessageEntity.getFromUser());
      statement.setString(7, hdMessageEntity.getToUser());
      statement.setString(8, hdMessageEntity.getFromJID());
      statement.setString(9, hdMessageEntity.getToJID());
      statement.setString(10, hdMessageEntity.getCreationDate());
      statement.setString(11, hdMessageEntity.getStanza());
      statement.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("message create error", ex);
    } finally {
      DbConnectionManager.closeConnection(statement, con);
    }
  }
}
