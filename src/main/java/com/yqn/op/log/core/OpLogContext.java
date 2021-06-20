package com.yqn.op.log.core;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/06/12 23:52
 * @desc the class desc
 */
public class OpLogContext {

    private List<SqlMetaData> sqlMetaDataList;
    private int curSqlCount = 0;
    private BizTrace bizTrace;

    public int getCurSqlCount() {
        return curSqlCount;
    }

    public void setCurSqlCount(int curSqlCount) {
        this.curSqlCount = curSqlCount;
    }

    public List<SqlMetaData> getSqlMetaDataList() {
        return sqlMetaDataList;
    }

    public void setSqlMetaDataList(List<SqlMetaData> sqlMetaDataList) {
        this.sqlMetaDataList = sqlMetaDataList;
    }

    public BizTrace getBizTrace() {
        return bizTrace;
    }

    public void setBizTrace(BizTrace bizTrace) {
        this.bizTrace = bizTrace;
    }
}
