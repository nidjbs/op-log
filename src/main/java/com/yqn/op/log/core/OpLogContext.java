package com.yqn.op.log.core;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/06/12 23:52
 * @desc the class desc
 */
public class OpLogContext {

    private List<SqlMetaData> sqlMetaDataList;
    private BizTrace bizTrace;
    private Long opLogId;
    private SqlMetaData curSqlMetaData;
    private boolean runAbleTag = true;

    public void reset() {
        this.curSqlMetaData = null;
        this.runAbleTag = true;
    }

    public boolean isRunAbleTag() {
        return runAbleTag;
    }

    public void setRunAbleTag(boolean runAbleTag) {
        this.runAbleTag = runAbleTag;
    }

    public SqlMetaData getCurSqlMetaData() {
        return curSqlMetaData;
    }

    public void setCurSqlMetaData(SqlMetaData curSqlMetaData) {
        this.curSqlMetaData = curSqlMetaData;
    }

    public Long getOpLogId() {
        return opLogId;
    }

    public void setOpLogId(Long opLogId) {
        this.opLogId = opLogId;
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
