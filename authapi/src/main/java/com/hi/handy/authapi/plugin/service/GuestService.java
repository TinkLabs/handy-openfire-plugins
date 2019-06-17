package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.dao.*;
import com.hi.handy.authapi.plugin.entity.HdGroupEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.AuthModel;
import com.hi.handy.authapi.plugin.model.ChatRoomModel;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class GuestService extends BaseService{

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestService.class);
    private GuestService() {
    }

    public static GuestService getInstance() {
        return INSTANCE;
    }

    public static final GuestService INSTANCE = new GuestService();

    public AuthModel guestLogin(AuthParameter parameter) throws UserNotFoundException, UserAlreadyExistsException {
        LOGGER.info("guestLogin");
        LOGGER.info("parameter",parameter);
        if (parameter.getAuthType() != BaseParameter.AuthType.GUEST_LOGIN) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is wrong");
        }
        if (StringUtils.isBlank(parameter.getBarcode())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "barcode is needed");
        }
        if (StringUtils.isBlank(parameter.getDisplayName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "displayName is needed");
        }
        if (StringUtils.isBlank(parameter.getEmail())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "email is needed");
        }
        if (parameter.getHotelId() == null) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "hotelId is needed");
        }
        if (StringUtils.isBlank(parameter.getHotelName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "hotelName is needed");
        }
        if (StringUtils.isBlank(parameter.getRoomNum())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "roomNum is needed");
        }
        if (parameter.getZoneId() == null) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "zoneId is needed");
        }
        if (StringUtils.isBlank(parameter.getZoneName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "zoneName is needed");
        }

        String guestUserName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
        // make sure create or update
        parameter.setUserType(BaseParameter.UserType.GUEST);
        Long count = HdUserPropertyDao.getInstance().countByUserName(guestUserName);
        if (count == null || count < 1L) {
            HdUserPropertyDao.getInstance().createUserProperty(guestUserName, parameter);
        } else {
            HdUserPropertyDao.getInstance().updateAllUserProperty(guestUserName, parameter);
        }

        // find openfire user by email,if user is not exist then create
        UserManager userManager = UserManager.getInstance();
        User user;
        if (!userManager.isRegisteredUser(guestUserName)) {
            String password = generatePassword(guestUserName);
            user = userManager.createUser(guestUserName, password, parameter.getDisplayName(),parameter.getEmail());
        } else {
            user = userManager.getUser(guestUserName);
        }

        String chatRoomInfo = parameter.getZoneId()+LINE_THROUGH+
                                parameter.getHotelId()+LINE_THROUGH+
                                parameter.getHotelName()+LINE_THROUGH+
                                parameter.getRoomNum()+ROOMINFO_SUFFIX+
                                guestUserName;

        // find chat room by email,if not exist create chat room
        ChatRoomModel chatRoom = getOrCreateVIPChatRoom(chatRoomInfo, parameter);
        String groupId = HdGroupRelationDao.getInstance().searchByRelationId(parameter.getZoneId());
        HdGroupEntity hdGroupEntity = new HdGroupEntity();
        if(StringUtils.isNoneBlank(groupId)) {
            hdGroupEntity = HdGroupDao.getInstance().searchById(groupId);
        }

        // the chat is online
        Boolean isOnline = groupIsOnline(groupId);

        // return user info and chat room
        ChatRoomModel chatRoomModel = new ChatRoomModel();
        chatRoomModel.setName(hdGroupEntity.getName());
        chatRoomModel.setIcon(hdGroupEntity.getIcon());
        chatRoomModel.setStatus(isOnline);
        chatRoomModel.setRoomName(chatRoom.getRoomName());
        chatRoomModel.setRoomJID(chatRoom.getRoomJID());
        AuthModel result = new AuthModel();
        result.setUserName(user.getUsername());
        result.setDisplayName(user.getName());
        result.setEmail(user.getEmail());
        result.setChatRoom(chatRoomModel);
        return result;
    }

    public AuthModel guestGetHotelChatRoom(AuthParameter parameter) throws UserAlreadyExistsException, UserNotFoundException {
        LOGGER.info("guestGetHotelChatRoom");
        LOGGER.info("parameter",parameter);
        if (parameter.getAuthType() != BaseParameter.AuthType.GUEST_ENTRY_HOTELCHATROOM) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is wrong");
        }
        if (StringUtils.isBlank(parameter.getBarcode())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "barcode is needed");
        }
        if (parameter.getHotelId() == null) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "hotelId is needed");
        }
        if (StringUtils.isBlank(parameter.getHotelName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "hotelName is needed");
        }
        if (StringUtils.isBlank(parameter.getRoomNum())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "roomNum is needed");
        }
        if (parameter.getZoneId() == null) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "zoneId is needed");
        }
        if (StringUtils.isBlank(parameter.getZoneName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "zoneName is needed");
        }

        String guestUserName = parameter.getBarcode();
        parameter.setDisplayName(HOTEL_CHAT_ROOM_GUEST_DEFAULT_DISPLAY_NAME);
        parameter.setUserType(BaseParameter.UserType.GUEST);
        Long count = HdUserPropertyDao.getInstance().countByUserName(guestUserName);
        if (count == null || count < 1L) {
            HdUserPropertyDao.getInstance().createUserProperty(guestUserName, parameter);
        } else {
            HdUserPropertyDao.getInstance().updateAllUserProperty(guestUserName, parameter);
        }

        // find chat room by email,if not exist create chat room
        UserManager userManager = UserManager.getInstance();
        User user;
        if (!userManager.isRegisteredUser(guestUserName)) {
            String password = generatePassword(guestUserName);
            user = userManager.createUser(guestUserName, password, parameter.getDisplayName(),guestUserName+EMAIL_SUFFIX);
        } else {
            user = userManager.getUser(guestUserName);
        }
        // find chat room by email,if not exist create chat room
        String chatRoomInfo = parameter.getZoneId()+LINE_THROUGH+
                              parameter.getHotelId()+LINE_THROUGH+
                              parameter.getHotelName()+LINE_THROUGH+
                              parameter.getRoomNum()+ROOMINFO_SUFFIX+
                              guestUserName;

        ChatRoomModel chatRoom = getOrCreateHotelChatRoom(chatRoomInfo, parameter);
        String groupId = HdGroupRelationDao.getInstance().searchByRelationId(parameter.getHotelId());
        HdGroupEntity hdGroupEntity = new HdGroupEntity();
        if(StringUtils.isNoneBlank(groupId)) {
            hdGroupEntity = HdGroupDao.getInstance().searchById(groupId);
        }
        // the chat is online
        Boolean isOnline = groupIsOnline(groupId);
        // return user info and chat room
        ChatRoomModel chatRoomModel = new ChatRoomModel();
        chatRoomModel.setName(hdGroupEntity.getName());
        chatRoomModel.setIcon(hdGroupEntity.getIcon());
        chatRoomModel.setStatus(isOnline);
        chatRoomModel.setRoomType(ChatRoomModel.RoomType.HOTEL);
        chatRoomModel.setRoomName(chatRoom.getRoomName());
        chatRoomModel.setRoomJID(chatRoom.getRoomJID());
        AuthModel result = new AuthModel();
        result.setUserName(user.getUsername());
        result.setDisplayName(user.getName());
        result.setEmail(user.getEmail());
        result.setChatRoom(chatRoomModel);
        return result;
    }

    private ChatRoomModel getOrCreateVIPChatRoom(String roomInfo, AuthParameter parameter) {
        String roomName;
        // create chat room
        roomName = getFullRoomName(ChatRoomModel.RoomType.VIP, roomInfo);
        MUCRoom agentChatRoom = XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatService(getServiceName()).getChatRoom(roomName);
        if (agentChatRoom == null) {
            agentChatRoom = createRoom(ChatRoomModel.RoomType.VIP, roomInfo, parameter);
        } else {
            updateHotelInfo(ChatRoomModel.RoomType.VIP, agentChatRoom, parameter);
        }
        return chatRoomConvert(ChatRoomModel.RoomType.VIP, agentChatRoom);
    }

    private ChatRoomModel getOrCreateHotelChatRoom(String name, AuthParameter parameter) {
        String roomName;
        // create chat room
        roomName = getFullRoomName(ChatRoomModel.RoomType.HOTEL, name);
        MUCRoom chatRoom = XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatService(getServiceName()).getChatRoom(roomName);
        if (chatRoom == null) {
            chatRoom = createRoom(ChatRoomModel.RoomType.HOTEL, name, parameter);
        }
        return chatRoomConvert(ChatRoomModel.RoomType.HOTEL, chatRoom);
    }

    private String getFullRoomName(ChatRoomModel.RoomType roomType, String roomName) {
        if (roomType == ChatRoomModel.RoomType.VIP) {
            return VIP_CHAT_ROOM_SUFFIX +roomName.toLowerCase();
        } else if (roomType == ChatRoomModel.RoomType.HOTEL) {
            return HOTEL_CHAT_ROOM_SUFFIX+roomName.toLowerCase();
        }
        return roomName.toLowerCase();
    }

    private String getServiceName() {
        // TODO
        return DEFAULT_SERVICE_NAME;
    }

    private void updateHotelInfo(ChatRoomModel.RoomType roomType, MUCRoom agentChatRoom, AuthParameter parameter) {
        String naturalLanguageName = parameter.getHotelId() + LINE_THROUGH + parameter.getRoomNum();
        if (naturalLanguageName.equals(agentChatRoom.getNaturalLanguageName())) {
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

    private MUCRoom createRoom(ChatRoomModel.RoomType roomType, String name, AuthParameter parameter) {
        JID owner = new JID(name + AT_SYMBOL + XMPPServer.getInstance().getServerInfo().getXMPPDomain());
        String roomName = getFullRoomName(roomType, name);

        MUCRoom room = null;
        try {
            room = XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatService(getServiceName()).getChatRoom(roomName, owner);
            // hotelId_romNum
            room.setNaturalLanguageName(parameter.getHotelId() + LINE_THROUGH + parameter.getRoomNum());
            // hotelName_romNum
            room.setDescription(parameter.getHotelName() + LINE_THROUGH + parameter.getRoomNum());
            room.setPersistent(true);
            room.setPublicRoom(true);
            room.setMaxUsers(10);
            room.setRolesToBroadcastPresence(new ArrayList());
            //setRoles(room, mucRoomEntity);
            //room.addNone(owner, room.getRole());
            room.setCreationDate(new Date());
            room.setModificationDate(new Date());
            room.unlock(room.getRole());
            room.saveToDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return room;
    }

    private ChatRoomModel chatRoomConvert(ChatRoomModel.RoomType roomType, MUCRoom agentChatRoom) {
        if (agentChatRoom == null) {
            return null;
        }
        ChatRoomModel chatRoomModel = new ChatRoomModel();
        chatRoomModel.setRoomName(agentChatRoom.getName());
        chatRoomModel.setRoomType(roomType);
        return chatRoomModel;
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

    private Boolean groupIsOnline(String groupId){
        List<String> userNames = queryGroupAgentByGroupId(groupId);
        List<String> onlineUsers = findOnlineUsers(userNames);
        if(onlineUsers!=null&&onlineUsers.size()>0){
            return true;
        }else {
            return false;
        }
    }

    private List<String> queryGroupAgentByGroupId(String groupId){
        return HdGroupAgentDao.getInstance().searchByGroupId(groupId);
    }
}
