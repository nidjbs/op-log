package com.yqn.op.log.core;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/06/20 22:51
 * @desc the class desc
 */
public class OpLogMetaDataWrapper {

    private String traceId;

    private String opId;

    private String bizId;

    private String bizDesc;

    private List<OpLogContent> opLogContents;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizDesc() {
        return bizDesc;
    }

    public void setBizDesc(String bizDesc) {
        this.bizDesc = bizDesc;
    }

    public List<OpLogContent> getOpLogContents() {
        return opLogContents;
    }

    public void setOpLogContents(List<OpLogContent> opLogContents) {
        this.opLogContents = opLogContents;
    }
}
