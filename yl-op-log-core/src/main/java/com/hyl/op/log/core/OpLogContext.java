package com.hyl.op.log.core;

import java.util.List;

import com.hyl.op.log.util.CollectionsUtil;

/**
 * @author huayuanlin
 * @date 2021/06/12 23:52
 * @desc the class desc
 */
public class OpLogContext {

    private final List<SqlMetaData> sqlMetaDataList = CollectionsUtil.arrayList();
    private Long opLogId;
    private SqlMetaData curSqlMetaData;
    private boolean runAbleTag = true;
    private OpLogGlobalContext opLogGlobalContext;

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


    public OpLogGlobalContext getOpLogGlobalContext() {
        return opLogGlobalContext;
    }

    public void setOpLogGlobalContext(OpLogGlobalContext opLogGlobalContext) {
        this.opLogGlobalContext = opLogGlobalContext;
    }
}
