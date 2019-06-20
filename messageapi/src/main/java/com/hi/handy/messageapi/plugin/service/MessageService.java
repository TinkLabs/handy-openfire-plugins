package com.hi.handy.messageapi.plugin.service;

import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import com.hi.handy.messageapi.plugin.parameter.MessageParameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    public static final MessageService INSTANCE = new MessageService();

    private MessageService() {
    }

    public static MessageService getInstance() {
        return INSTANCE;
    }

    public Object list(MessageParameter parameter) {
        LOGGER.info("list");
        LOGGER.info("parameter", parameter);
        checkParameter(parameter);
        return MessageChatList.getInstance().queryChatListByPaging(parameter);
    }

    private void checkParameter(MessageParameter parameter) {
        if (StringUtils.isBlank(parameter.getUserName())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "userName is needed");
        }
        if (parameter.getPageIndex() == null) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "pageIndex is needed");
        }
        if (parameter.getPageIndex() < 0) {
            throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "pageIndex is wrong");
        }
        if (parameter.getPageSize() == null) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "pageSize is needed");
        }
        if (parameter.getPageSize() <= 0) {
            throw new BusinessException(ExceptionConst.PARAMETER_ERROR, "pageSize is wrong");
        }
    }
}
