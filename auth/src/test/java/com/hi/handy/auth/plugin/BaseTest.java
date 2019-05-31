package com.hi.handy.auth.plugin;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.database.DefaultConnectionProvider;
import org.junit.BeforeClass;

public class BaseTest {

    @BeforeClass
    public static void setUp() {
        DefaultConnectionProvider conProvider = new DefaultConnectionProvider();
        conProvider.setDriver("com.mysql.jdbc.Driver");
        conProvider.setConnectionTimeout(1.0);
        conProvider.setMinConnections(5);
        conProvider.setMaxConnections(25);
        conProvider.setServerURL("jdbc:mysql://127.0.0.1:3320/openfire?rewriteBatchedStatements=true");
        conProvider.setUsername("root");
        conProvider.setPassword("123456");
        conProvider.setTestSQL(DbConnectionManager.getTestSQL("com.mysql.jdbc.Driver"));
        DbConnectionManager.setConnectionProvider(conProvider);
    }
}
