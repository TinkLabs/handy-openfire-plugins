package com.hi.handy.group.plugin.test;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.database.DefaultConnectionProvider;
import org.junit.BeforeClass;

@SuppressWarnings("Duplicates")
public class BaseTest {

    @BeforeClass
    public static void setUp() {
        DefaultConnectionProvider conProvider = new DefaultConnectionProvider();
        conProvider.setDriver("com.mysql.jdbc.Driver");
        conProvider.setConnectionTimeout(1.0);
        conProvider.setMinConnections(5);
        conProvider.setMaxConnections(25);
        conProvider.setServerURL("jdbc:mysql://handy-dev.csojfhhlojxj.ap-southeast-1.rds.amazonaws.com:3306/openfire?rewriteBatchedStatements=true");
        conProvider.setUsername("admin");
        conProvider.setPassword("mango1501");
        conProvider.setTestSQL(DbConnectionManager.getTestSQL("com.mysql.jdbc.Driver"));
        DbConnectionManager.setConnectionProvider(conProvider);
    }
}
