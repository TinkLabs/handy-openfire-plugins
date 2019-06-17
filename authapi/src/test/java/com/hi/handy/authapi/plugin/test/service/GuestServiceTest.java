package com.hi.handy.authapi.plugin.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter;
import com.hi.handy.authapi.plugin.service.GuestService;
import com.hi.handy.authapi.plugin.test.BaseTest;
import com.hi.handy.message.plugin.test.util.OutPrintUtils;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.junit.Test;

public class GuestServiceTest extends BaseTest {

    @Test
    public void guestLogin_Test() throws JsonProcessingException, UserAlreadyExistsException, UserNotFoundException {
        AuthParameter authParameter = new AuthParameter();
        authParameter.setAuthType(BaseParameter.AuthType.GUEST_LOGIN);
        authParameter.setDisplayName("xiaoming3000");
        authParameter.setEmail("xiaoming3000@tinklabs.com");
        authParameter.setHotelId(1l);
        authParameter.setHotelName("酒店3");
        authParameter.setRoomNum("303");
        authParameter.setZoneId(1l);
        Object object = GuestService.getInstance().guestLogin(authParameter);
        OutPrintUtils.printlnJson(object);
    }
}

