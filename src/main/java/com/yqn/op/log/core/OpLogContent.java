package com.yqn.op.log.core;

import java.util.Map;

/**
 * @author huayuanlin
 * @date 2021/06/20 21:27
 * @desc the class desc
 */
public class OpLogContent {
    private String tableName;
    private Integer type;
    private Map<String,Object> before;
    private Map<String,Object> after;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<String, Object> getBefore() {
        return before;
    }

    public void setBefore(Map<String, Object> before) {
        this.before = before;
    }

    public Map<String, Object> getAfter() {
        return after;
    }

    public void setAfter(Map<String, Object> after) {
        this.after = after;
    }
}
