package com.hi.handy.authapi.plugin.service;

import com.hi.handy.authapi.plugin.dao.HdMessageDao;
import com.hi.handy.authapi.plugin.dao.OfMessageArchiveDao;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.NotifyModel;
import com.hi.handy.authapi.plugin.model.NotifyType;
import com.hi.handy.authapi.plugin.model.ResultModel;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.BaseParameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageService extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private MessageService() {}

    public static MessageService getInstance() {
        return INSTANCE;
    }

    public static final MessageService INSTANCE = new MessageService();

    public ResultModel messageDelete(AuthParameter parameter){
        LOGGER.debug("message delete");
        LOGGER.debug("parameter",parameter);
        if (parameter.getAuthType() != BaseParameter.AuthType.MESSAGE_DELETE) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "authType is wrong");
        }
        if (StringUtils.isBlank(parameter.getMessageId())) {
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE, "messageId is needed");
        }
        // delete message
        Boolean result = OfMessageArchiveDao.getInstance().deleteBymessageId(parameter.getMessageId()) &&
                         HdMessageDao.getInstance().deleteBymessageId(parameter.getMessageId());
        // nofity
        if(result){
            // clear chache
            // TODO
            // send broadcast
            notify(new NotifyModel(NotifyType.DELETEMESSAGE,parameter.getMessageId()));
        }
        return new ResultModel(result);
    }
}
