package com.hi.handy.auth.plugin.service;

import com.hi.handy.auth.plugin.exception.BusinessException;
import com.hi.handy.auth.plugin.exception.ExceptionConst;
import com.hi.handy.auth.plugin.model.AuthModel;
import com.hi.handy.auth.plugin.model.ChatRoomModel;
import com.hi.handy.auth.plugin.model.ChatRoomModel.RoomType;
import com.hi.handy.auth.plugin.parameter.AuthParameter;
import com.hi.handy.auth.plugin.parameter.BaseParameter.AuthType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.xmpp.packet.JID;

public class AuthService {

  private AuthService() {

  }
  private static final String AGENT_ROOM_SUFFIX = "-room-agent";
  private static final String HOTEL_ROOM_SUFFIX = "-room-hotel";
  private static final String LINE_THROUGH = "-";

  public static final AuthService INSTANCE = new AuthService();

  public static AuthService getInstance() {
    return INSTANCE;
  }

  public AuthModel auth(AuthParameter parameter)
      throws UserAlreadyExistsException, UserNotFoundException {
    checkParameter(parameter);

    AuthModel result = null;

    if (parameter.getAuthType() == AuthType.GUEST) {
      result = guestAuth(parameter);
    } else if (parameter.getAuthType() == AuthType.AGENT){
      result = agentAuth(parameter);
    }
    return result;
  }

  private AuthModel agentAuth(AuthParameter parameter) {
    return null;
  }

  private AuthModel guestAuth(AuthParameter parameter)
      throws UserAlreadyExistsException, UserNotFoundException {
    // 使用email查找是否有这个用户，如果没有，则进行注册
    UserManager userManager = UserManager.getInstance();
    User user;
    String guestUserName = parameter.getEmail().replace("@", "-");
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
    // 返回账号信息 和 chat room
    AuthModel result = new AuthModel();
    result.setUserName(user.getUsername());
    result.setPassword(generatePassword(user.getUsername()));
    result.setEmail(user.getEmail());
    result.setChatRooms(chatRooms);
    return result;
  }

  private void checkParameter(AuthParameter parameter) {
    if(parameter.getAuthType() == null) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is needed");
    }
    if(StringUtils.isBlank(parameter.getEmail())) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "email is needed");
    }
    if(StringUtils.isBlank(parameter.getHotelId())) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "hotelId is needed");
    }
    if(StringUtils.isBlank(parameter.getHotelName())) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "hotelName is needed");
    }
    if(StringUtils.isBlank(parameter.getRoomNum())) {
      throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "roomNum is needed");
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
      agentChatRoom.setDescription(parameter.getHotelName() + LINE_THROUGH + parameter.getRoomNum());
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
        guestUserName + "@" + XMPPServer.getInstance().getServerInfo().getXMPPDomain());
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
      return guestUserName.toLowerCase() + AGENT_ROOM_SUFFIX;
    } else if (roomType == RoomType.HOTEL) {
      return guestUserName.toLowerCase() + HOTEL_ROOM_SUFFIX;
    }
    return guestUserName.toLowerCase();
  }

  private String getServiceName() {
    // TODO
    return "conference";
  }

  private String generatePassword(String code) {
    // TODO
    return code + "_openfire";
  }


}
