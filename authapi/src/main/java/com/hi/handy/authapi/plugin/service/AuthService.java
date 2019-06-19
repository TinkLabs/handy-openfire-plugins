package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.AuthModel;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter.AuthType;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    public static final AuthService INSTANCE = new AuthService();

    private AuthService() {
    }

    public static AuthService getInstance() {
        return INSTANCE;
    }

    public AuthModel auth(AuthParameter parameter) throws UserAlreadyExistsException, UserNotFoundException {
        LOGGER.info("auth");
        LOGGER.info("parameter",parameter);
        AuthModel result = null;
        AuthType type = parameter.getAuthType();
        if (type == AuthType.GUEST_LOGIN) {
            result = GuestService.getInstance().guestLogin(parameter);
        } else if (type == AuthType.GUEST_ENTRY_HOTELCHATROOM) {
            result = GuestService.getInstance().guestGetHotelChatRoom(parameter);
        } else if (type == AuthType.AGENT_LOGIN) {
            result = AgentService.getInstance().agentLogin(parameter);
        } else if (type == AuthType.AGENT_LOGOUT) {
            result = AgentService.getInstance().agentLogout(parameter);
        }else{
            throw new BusinessException(ExceptionConst.BUSINESS_ERROR, "unknow authType");
        }
        return result;
    }
}
