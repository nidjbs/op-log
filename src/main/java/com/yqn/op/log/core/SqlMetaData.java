package com.yqn.op.log.core;

import com.yqn.op.log.enums.SqlType;

import java.util.Map;

/**
 * @author huayuanlin
 * @date 2021/06/13 00:42
 * @desc the class desc
 */
public class SqlMetaData {

    private SqlType sqlType;

    private boolean needLog = false;

    private String tableName;

    private String sql;

    private Map<String, Object> beforeData;

    private Map<String, Object> afterData;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public boolean isNeedLog() {
        return needLog;
    }

    public void setNeedLog(boolean needLog) {
        this.needLog = needLog;
    }

    public void setSqlType(SqlType sqlType) {
        this.sqlType = sqlType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, Object> getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(Map<String, Object> beforeData) {
        this.beforeData = beforeData;
    }

    public Map<String, Object> getAfterData() {
        return afterData;
    }

    public void setAfterData(Map<String, Object> afterData) {
        this.afterData = afterData;
    }
}