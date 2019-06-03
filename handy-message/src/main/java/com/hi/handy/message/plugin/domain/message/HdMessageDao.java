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
          "INSERT INTO hdHistory (fromUser, toUser, fromJID, toJID, creationDate, stanza) " +
                  "VALUES (?, ?, ?, ?, ?, ?)";

  public void save(HdMessageEntity hdMessageEntity) {
    Connection con = null;
    PreparedStatement statement = null;
    try {
      con = DbConnectionManager.getConnection();
      statement = con.prepareStatement(CREATE_HISTORY_SQL);
      statement.setString(1, hdMessageEntity.getFromUser());
      statement.setString(2, hdMessageEntity.getToUser());
      statement.setString(3, hdMessageEntity.getFromJID());
      statement.setString(4, hdMessageEntity.getToJID());
      statement.setString(5, hdMessageEntity.getCreationDate());
      statement.setString(6, hdMessageEntity.getStanza());
      statement.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("message save error", ex);
    } finally {
      DbConnectionManager.closeConnection(statement, con);
    }
  }
}
