package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.dao.HdUserPropertyDao;
import com.hi.handy.authapi.plugin.entity.HdUserPropertyEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.AuthModel;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentService extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentService.class);

    private AgentService() {
    }

    public static AgentService getInstance() {
        return INSTANCE;
    }

    public static final AgentService INSTANCE = new AgentService();

    public AuthModel agentLogin(AuthParameter parameter) throws UserAlreadyExistsException, UserNotFoundException {
        LOGGER.info("agentLogin");
        LOGGER.info("parameter",parameter);
        if (parameter.getAuthType() != BaseParameter.AuthType.AGENT_LOGIN) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is wrong");
        }
        if (StringUtils.isBlank(parameter.getEmail())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "email is needed");
        }
        if (StringUtils.isBlank(parameter.getPassword())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "password is needed");
        }
        if (parameter.getPassword().trim().length() < 8) {
            throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "password length no less than 8");
        }

        String agentUserName = parameter.getEmail().replace(AT_SYMBOL, LINE_THROUGH);
        HdUserPropertyEntity hdUserPropertyEntity = HdUserPropertyDao.getInstance().searchByUserName(agentUserName);
        if(hdUserPropertyEntity == null) throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "email or password is wrong");
        String passwordGenerate = generatePassword(parameter.getPassword());
        if(!passwordGenerate.equals(hdUserPropertyEntity.getPassword())) throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "email or password is wrong");

        // find openFire user by email,if user is not exist then create
        UserManager userManager = UserManager.getInstance();
        User user;
        if (!userManager.isRegisteredUser(agentUserName)) {
            String password = generatePassword(agentUserName);
            user = userManager.createUser(agentUserName, password, parameter.getDisplayName(),parameter.getEmail());
        } else {
            user = userManager.getUser(agentUserName);
        }

        AuthModel result = new AuthModel();
        result.setUserName(user.getUsername());
        result.setDisplayName(user.getName());
        result.setEmail(user.getEmail());
        return result;
    }
}
