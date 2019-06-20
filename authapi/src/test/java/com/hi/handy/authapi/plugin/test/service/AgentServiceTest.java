package com.hi.handy.authapi.plugin.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter;
import com.hi.handy.authapi.plugin.service.AgentService;
import com.hi.handy.authapi.plugin.test.BaseTest;
import com.hi.handy.authapi.plugin.test.util.OutPrintUtils;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.junit.Test;

public class AgentServiceTest extends BaseTest {

    @Test
    public void guestLogin_Test() throws JsonProcessingException, UserAlreadyExistsException, UserNotFoundException {
        AuthParameter authParameter = new AuthParameter();
        authParameter.setAuthType(BaseParameter.AuthType.AGENT_LOGOUT);
        authParameter.setEmail("dave@tinklabs.com");
        Object object = AgentService.getInstance().agentLogout(authParameter);
        OutPrintUtils.printlnJson(object);
    }
}

