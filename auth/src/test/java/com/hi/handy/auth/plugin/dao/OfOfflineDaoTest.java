package com.hi.handy.auth.plugin.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.auth.plugin.BaseTest;
import com.hi.handy.auth.plugin.util.OutPrintUtils;
import com.hi.handy.auth.plugin.model.OfOfflineMessageModel;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class OfOfflineDaoTest extends BaseTest {

    @Test
    public void searchByUserName_Test() throws JsonProcessingException {
        List<OfOfflineMessageModel> ofOfflineMessageModels = OfOfflineDao.getInstance().searchByUserName(1,"301");
        Assert.assertNotNull(ofOfflineMessageModels);
        OutPrintUtils.printlnJson(ofOfflineMessageModels);
    }
}
