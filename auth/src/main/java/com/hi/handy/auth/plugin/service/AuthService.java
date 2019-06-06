package com.hi.handy.auth.plugin.service;

import com.hi.handy.auth.plugin.dao.HdUserPropertyDao;
import com.hi.handy.auth.plugin.dao.HdUserRoomDao;
import com.hi.handy.auth.plugin.entity.UserRoomEntity;
import com.hi.handy.auth.plugin.exception.BusinessException;
import com.hi.handy.auth.plugin.exception.ExceptionConst;
import com.hi.handy.auth.plugin.model.AuthModel;
import com.hi.handy.auth.plugin.model.ChatRoomModel;
import com.hi.handy.auth.plugin.model.ChatRoomModel.RoomType;
import com.hi.handy.auth.plugin.parameter.AuthParameter;
import com.hi.handy.auth.plugin.parameter.AuthParameter.AuthType;
import com.hi.handy.auth.plugin.parameter.AuthParameter.UserType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.directwebremoting.annotations.Auth;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.vcard.VCardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

public class AuthService {

  public static final Logger log = LoggerFactory.getLogger(AuthService.class);
  private static final String DEFAULT_SERVICE_NAME = "conference";
  private static final String GUEST_PASSWORD_SUFFIX = "_openfire";
  private static final String GUEST_ROOM_SUFFIX = "-room-guest";
  private static final String HOTEL_ROOM_SUFFIX = "-room-hotel";
  private static final String LINE_THROUGH = "-";
  private static final String AT_SYMBOL = "@";
  private static final String DOT_SYMBOL = ".";
  public static final AuthService INSTANCE = new AuthService();

  private AuthService() {
  }

  public static AuthService getInstance() {
    return INSTANCE;
  }

  public AuthModel auth(AuthParameter parameter)
      throws UserAlreadyExistsException, UserNotFoundException {
    checkParameter(parameter);

    AuthModel result = null;
    AuthType type = parameter.getAuthType();
    if (type == AuthType.GUEST) {
      result = guestAuth(parameter);
    } else if (type == AuthType.AGENT_REGISTER) {
      result = agentRegister(parameter);
    } else if (type == AuthType.AGENT_MODIFY) {
      result = agentModify(parameter);
    } else if (type == AuthType.AGENT_CHAT_ROOM) {
      result = agentChatRoom(parameter);
    }
    return result;
  }

  private AuthModel agentChatRoom(AuthParameter parameter) {
    AuthModel result;
    String userName = parameter.getUserName();
    if (StringUtils.isBlank(userName) && StringUtils.isNoneBlank(parameter.getEmail())) {
      userName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
    }
    List<UserRoomEntity> userRooms = HdUserRoomDao.getInstance()
        .searchByZoneAndRoomName(parameter.getZoneId(), userName);
    if (userRooms == null || userRooms.isEmpty()) {
      return null;
    }
    result = new AuthModel();
    List<ChatRoomModel> chatRooms = new ArrayList<ChatRoomModel>(userRooms.size());
    for (UserRoomEntity userRoom : userRooms) {
      ChatRoomModel chatRoomModel = new ChatRoomModel();
      chatRoomModel.setRoomType(RoomType.AGENT);
      chatRoomModel.setRoomName(userRoom.getRoomName());
      chatRoomModel.setRoomJID(userRoom.getRoomName() + AT_SYMBOL + getServiceName()
          + DOT_SYMBOL + XMPPServer.getInstance().getServerInfo().getXMPPDomain());
      chatRooms.add(chatRoomModel);
    }
    result.setChatRooms(chatRooms);
    return result;
  }

  private AuthModel agentModify(AuthParameter parameter) {

    // ticket 校验 TODO
    UserManager userManager = UserManager.getInstance();
    String agentUserName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
    if (!userManager.isRegisteredUser(agentUserName)) {
      throw new BusinessException(ExceptionConst.DATA_LOSE, "account is not exist");
    }
    // 更新zoneId,zoneName TODO 调整userRoom表
    HdUserPropertyDao.getInstance().updateUserProperty(agentUserName, parameter);
    return null;
  }

  private AuthModel agentRegister(AuthParameter parameter) throws UserAlreadyExistsException {
    // 使用email查找是否有这个用户，如果没有，则进行注册
    UserManager userManager = UserManager.getInstance();
    User user;
    String agentUserName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
    if (userManager.isRegisteredUser(agentUserName)) {
      throw new BusinessException(ExceptionConst.DATA_REPEATED, "account is already exist ");
    }

    // 先添加或者更新agent的相关属性，确保成功后再创建agent用户
    Long count = HdUserPropertyDao.getInstance().countByUserName(agentUserName);
    if (count == null || count < 1L) {
      HdUserPropertyDao.getInstance().createUserProperty(agentUserName, parameter);
    } else {
      HdUserPropertyDao.getInstance().updateUserProperty(agentUserName, parameter);
    }
    // 创建用户
    String password = parameter.getPassword().trim();
    user = userManager.createUser(agentUserName, password, parameter.getUserName(),
        parameter.getEmail());

    // 返回账号信息
    AuthModel result = new AuthModel();
    result.setUserName(user.getUsername());
    result.setEmail(user.getEmail());
    //新注册的agent需要加入到zone下面的group
    GroupManager groupManager = GroupManager.getInstance();
    if(groupManager.isSearchSupported()){
      Collection<Group> groups = groupManager.search("zone"+parameter.getZoneId()+"-");
      log.info("zone:"+parameter.getZoneId()+" groupSize:"+groups.size()+"");
      for (Group group : groups) {
        JID jid = new JID(agentUserName);
        group.getMembers().add(jid);
      }
    }

    return result;
  }

  private AuthModel guestAuth(AuthParameter parameter)
      throws UserAlreadyExistsException, UserNotFoundException {
    String guestUserName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
    // 先确保新增或者更新成功user相关的zoneId,hotelId,roomNum
    parameter.setUserType(UserType.GUEST);
    Long count = HdUserPropertyDao.getInstance().countByUserName(guestUserName);
    if (count == null || count < 1L) {
      HdUserPropertyDao.getInstance().createUserProperty(guestUserName, parameter);
    } else {
      HdUserPropertyDao.getInstance().updateAllUserProperty(guestUserName, parameter);
    }

    // 使用email查找是否有这个用户，如果没有，则进行注册
    UserManager userManager = UserManager.getInstance();
    User user;

    if (!userManager.isRegisteredUser(guestUserName)) {
      String password = generatePassword(guestUserName);
      user = userManager
          .createUser(guestUserName, password, parameter.getUserName(),
              parameter.getEmail());
    } else {
      user = userManager.getUser(guestUserName);
    }

    // 通过email查找有没有chat room,如果没有则创建
    List<ChatRoomModel> chatRooms = getOrCreateChatRoom(guestUserName, parameter);
    ChatRoomModel chatRoomModel = findAgentRoom(chatRooms);
    // 分配agent
    String agent = distributeAgent(parameter.getZoneId(), guestUserName, chatRoomModel);
    //创建组（group）,小组的成员是某个房间的guest和所属zone下面的agent,group的name是zoneid+hotelid+roomid+deviceuserid？
    String  groupName = "zone"+parameter.getZoneId()+"-hotel"+parameter.getHotelId()+"-room"+parameter.getRoomNum();

    GroupManager groupManager = GroupManager.getInstance();
    boolean isGroupExist = false;
    Group group = null;
    try {
      group = groupManager.getGroup(groupName,true);
    } catch (GroupNotFoundException e) {
      e.printStackTrace();
    }
    if(group!=null){
      isGroupExist = true;
    }
    if(!isGroupExist){
      try {
        group = groupManager.createGroup(groupName);
      } catch (GroupAlreadyExistsException e) {
        e.printStackTrace();
        throw new BusinessException(ExceptionConst.BUSINESS_ERROR,
                "create group fail groupAlreadyExists! groupname:"+groupName);
      }
    }
    //将当前的的guest 加入到group
    JID userJid = new JID(user.getUsername());
    group.getMembers().add(userJid);
    //将该zone下的agent加入到group中
    List<String> agentUserNames = HdUserPropertyDao.getInstance().searchByZone(parameter.getZoneId());
    for (String agentUserName : agentUserNames) {
      JID jid = new JID(agentUserName);
      group.getMembers().add(jid);
    }


    // 返回账号信息 和 chat room
    AuthModel result = new AuthModel();
    result.setUserName(user.getUsername());
    result.setPassword(generatePassword(user.getUsername()));
    result.setEmail(user.getEmail());
    result.setChatRooms(chatRooms);
    result.setAgent(agent);
    result.setAgentJID(agent + AT_SYMBOL + getServiceName()
        + DOT_SYMBOL + XMPPServer.getInstance().getServerInfo().getXMPPDomain());
    return result;
  }

  private String distributeAgent(Long zoneId, String guestUserName, ChatRoomModel chatRoomModel) {
    if (zoneId == null || StringUtils.isBlank(guestUserName) || chatRoomModel == null) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE,
          "distribute agent parameter can not be null");
    }
    // 查询已经分配了的agent
    List<UserRoomEntity> userRooms = HdUserRoomDao.getInstance()
        .searchByRoomName(chatRoomModel.getRoomName());

    // 如果有符合条件的agent则返回
    if (!userRooms.isEmpty() && userRooms.size() == 1
        && userRooms.get(0).getZoneId().equals(zoneId)) {
      return userRooms.get(0).getUserName();
    }

    // 如果已经有agent分配但是zone不一样，或者有多条冗余的数据，则删除记录
    if (!userRooms.isEmpty()) {
      if (userRooms.size() > 1 || !zoneId.equals(userRooms.get(0).getZoneId())) {
        HdUserRoomDao.getInstance().deleteByRoomName(chatRoomModel.getRoomName());
        // 更新hdUserProperty表中的roomAmount字段，数值减1 TODO
      }
    }

    // 根据zone查找所有的agent
    List<String> agents = HdUserPropertyDao.getInstance().searchByZone(zoneId);
    if (agents == null || agents.isEmpty()) {
      throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "no agent in current zone");
    }
    // 查找一个合适的agent,并持久化到HdUserRoom中
    String agent = findOptimalAgent(agents);
    HdUserRoomDao.getInstance().create(zoneId, agent, chatRoomModel.getRoomName());
    // 更新hdUserProperty表中的roomAmount字段，数值加1
    HdUserPropertyDao.getInstance().roomAmountPlusOne(agent);

    return agent;
  }

  private String findOptimalAgent(List<String> agents) {
    String agent = null;
    if (agents.size() == 1) {
      return agents.get(0);
    }
    // 查找目前已有chat room数量最小的agent
    List<String> onlineAgents = findOnlineUsers(agents);
    if (onlineAgents != null && !onlineAgents.isEmpty()) {
      // 优先从在线的agent中查找
      if (onlineAgents.size() == 1) {
        return onlineAgents.get(0);
      } else {
        agent = HdUserPropertyDao.getInstance().searchMinRoomAmountUserName(onlineAgents);
      }
    } else {
      agent = HdUserPropertyDao.getInstance().searchMinRoomAmountUserName(agents);
    }
    // 确保有一个agent
    if (agent == null) {
      agent = agents.get(0);
    }
    return agent;
  }

  private List<String> findOnlineUsers(List<String> users) {
    List<String> onlineUsers = new ArrayList<String>(users.size());
    for (String user : users) {
      Collection<ClientSession> clientSessions = SessionManager.getInstance().getSessions(user);
      if (!clientSessions.isEmpty()) {
        onlineUsers.add(user);
      }
    }
    return onlineUsers;
  }

  private ChatRoomModel findAgentRoom(List<ChatRoomModel> chatRooms) {
    for (ChatRoomModel r : chatRooms) {
      if (r.getRoomType() == RoomType.AGENT) {
        return r;
      }
    }
    return null;
  }

  private void checkParameter(AuthParameter parameter) {
    if (parameter.getAuthType() == null) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is needed");
    }
    if (StringUtils.isBlank(parameter.getEmail())) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "email is needed");
    }
    if (parameter.getAuthType() == AuthType.GUEST) {
      if (parameter.getHotelId() == null) {
        throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "hotelId is needed");
      }
      if (StringUtils.isBlank(parameter.getHotelName())) {
        throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "hotelName is needed");
      }
      if (StringUtils.isBlank(parameter.getRoomNum())) {
        throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "roomNum is needed");
      }
    } else if (parameter.getAuthType() == AuthType.AGENT_REGISTER) {
      if (StringUtils.isBlank(parameter.getPassword())) {
        throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "password is needed");
      }
      if (parameter.getUserType() == null) {
        throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "userType is needed");
      }
      if (parameter.getPassword().trim().length() < 8) {
        throw new BusinessException(ExceptionConst.PARAMETER_ERROR,
            "password length no less than 8");
      }
      if (parameter.getZoneId() == null) {
        throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "zoneId is needed");
      }
    } else if (parameter.getAuthType() == AuthType.AGENT_MODIFY) {
      if (parameter.getZoneId() == null) {
        throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "zoneId is needed");
      }
    } else if (parameter.getAuthType() == AuthType.AGENT_CHAT_ROOM) {
      if (parameter.getZoneId() == null) {
        throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "zoneId is needed");
      }
    }
  }

  private List<ChatRoomModel> getOrCreateChatRoom(String guestUserName,
      AuthParameter parameter) {
    List<ChatRoomModel> rooms = new ArrayList<ChatRoomModel>(3);
    String roomName;
    // 创建或获取和agent相关的chat room
    roomName = getRoomNameByGuestName(RoomType.AGENT, guestUserName);
    MUCRoom agentChatRoom = XMPPServer.getInstance().getMultiUserChatManager()
        .getMultiUserChatService(getServiceName())
        .getChatRoom(roomName);
    if (agentChatRoom == null) {
      agentChatRoom = createRoom(RoomType.AGENT, guestUserName, parameter);
    } else {
      updateHotelInfo(RoomType.AGENT, agentChatRoom, parameter);
    }
    rooms.add(chatRoomConvert(RoomType.AGENT, agentChatRoom));

    // 创建或获取和hotel相关的chat room TODO
    return rooms;
  }

  private void updateHotelInfo(RoomType roomType, MUCRoom agentChatRoom, AuthParameter parameter) {
    String naturalLanguageName = parameter.getHotelId() + LINE_THROUGH + parameter.getRoomNum();
    if (naturalLanguageName.equals(agentChatRoom.getNaturalLanguageName())) {
      // no need update
      return;
    }
    try {
      agentChatRoom.setNaturalLanguageName(naturalLanguageName);
      agentChatRoom
          .setDescription(parameter.getHotelName() + LINE_THROUGH + parameter.getRoomNum());
      agentChatRoom.setModificationDate(new Date());
      agentChatRoom.unlock(agentChatRoom.getRole());
      agentChatRoom.saveToDB();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private ChatRoomModel chatRoomConvert(RoomType roomType, MUCRoom agentChatRoom) {
    if (agentChatRoom == null) {
      return null;
    }
    ChatRoomModel chatRoomModel = new ChatRoomModel();
    chatRoomModel.setRoomName(agentChatRoom.getName());
    chatRoomModel.setRoomType(roomType);

    return chatRoomModel;
  }

  private MUCRoom createRoom(RoomType roomType, String guestUserName, AuthParameter parameter) {
    JID owner = new JID(
        guestUserName + AT_SYMBOL + XMPPServer.getInstance().getServerInfo().getXMPPDomain());
    String roomName = getRoomNameByGuestName(roomType, guestUserName);

    MUCRoom room = null;
    try {
      room = XMPPServer.getInstance().getMultiUserChatManager()
          .getMultiUserChatService(getServiceName())
          .getChatRoom(roomName, owner);
      // hotelId_romNum
      room.setNaturalLanguageName(parameter.getHotelId() + LINE_THROUGH + parameter.getRoomNum());
      // hotelName_romNum
      room.setDescription(parameter.getHotelName() + LINE_THROUGH + parameter.getRoomNum());
      room.setPersistent(true);
      room.setPublicRoom(true);
      room.setMaxUsers(10);
      room.setRolesToBroadcastPresence(new ArrayList());
//      setRoles(room, mucRoomEntity);
//      room.addNone(owner, room.getRole());
      room.setCreationDate(new Date());
      room.setModificationDate(new Date());
      room.unlock(room.getRole());
      room.saveToDB();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return room;
  }


  private String getRoomNameByGuestName(RoomType roomType, String guestUserName) {
    if (roomType == RoomType.AGENT) {
      return guestUserName.toLowerCase() + GUEST_ROOM_SUFFIX;
    } else if (roomType == RoomType.HOTEL) {
      return guestUserName.toLowerCase() + HOTEL_ROOM_SUFFIX;
    }
    return guestUserName.toLowerCase();
  }

  private String getServiceName() {
    // TODO
    return DEFAULT_SERVICE_NAME;
  }

  private String generatePassword(String code) {
    // TODO
    return code + GUEST_PASSWORD_SUFFIX;
  }
}
