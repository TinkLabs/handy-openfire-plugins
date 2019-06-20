package com.hi.handy.messageapi.plugin.service;

import java.util.List;

public class BaseService {

    protected String joinListForInSqlString(List<String> ids){
        StringBuffer in_sql=new StringBuffer();
        for (int i = 0; i < ids.size(); i++) {
            in_sql.append("'" + ids.get(i) + "'");
            if (i < ids.size() - 1) {
                in_sql.append(",");
            }
        }
        return in_sql.toString();
    }

    protected String joinListForInSqlLong(List<Long> ids){
        StringBuffer in_sql=new StringBuffer();
        for (int i = 0; i < ids.size(); i++) {
            in_sql.append(ids.get(i));
            if (i < ids.size() - 1) {
                in_sql.append(",");
            }
        }
        return in_sql.toString();
    }
}
