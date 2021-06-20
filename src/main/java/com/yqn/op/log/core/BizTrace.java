package com.yqn.op.log.core;

/**
 * @author huayuanlin
 * @date 2021/06/17 10:22
 * @desc the class desc
 */
public class BizTrace {

    /**
     * biz trace id,can be null
     */
    private String traceId;

    /**
     * biz op user id,can be null
     */
    private String bizOpId;

    /**
     * biz op desc
     */
    private String bizDesc;


    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getBizOpId() {
        return bizOpId;
    }

    public void setBizOpId(String bizOpId) {
        this.bizOpId = bizOpId;
    }

    public String getBizDesc() {
        return bizDesc;
    }

    public void setBizDesc(String bizDesc) {
        this.bizDesc = bizDesc;
    }
}
