package com.hi.handy.group.plugin.service;

import com.hi.handy.group.plugin.model.ResultModel;
import com.hi.handy.group.plugin.parameter.GroupParameter;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    public static final GroupService INSTANCE = new GroupService();

    private GroupService() {
    }

    public static GroupService getInstance() {
        return INSTANCE;
    }

    public ResultModel auth(GroupParameter parameter) throws UserAlreadyExistsException, UserNotFoundException {
        LOGGER.info("auth");
        LOGGER.info("parameter",parameter);
        ResultModel result = null;
//        BaseParameter.ApiType type = parameter.getApiType();
//        if (type == BaseParameter.ApiType.GUEST_LOGIN) {
//            result = GuestService.getInstance().guestLogin(parameter);
//        } else if (type == AuthType.GUEST_ENTRY_HOTELCHATROOM) {
//            result = GuestService.getInstance().guestGetHotelChatRoom(parameter);
//        } else if (type == AuthType.AGENT_LOGIN) {
//            result = AgentService.getInstance().agentLogin(parameter);
//        } else if (type == AuthType.AGENT_LOGOUT) {
//            result = AgentService.getInstance().agentLogout(parameter);
//        }else{
//            throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "unknow authType");
//        }
        return result;
    }
}
