package com.yqn.op.log.core;

/**
 * @author huayuanlin
 * @date 2021/06/20 15:46
 * @desc the class desc
 */
public class OpLogMetaDataDO {

    private Long id;

    private String traceId;

    private String opId;

    private String bizId;

    private String bizDesc;
    /**
     * log status: 0-init，1-processing，2-fail，3-complete
     */
    private Integer status;

    /**
     * json
     */
    private String metaData;

    public String getBizDesc() {
        return bizDesc;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setBizDesc(String bizDesc) {
        this.bizDesc = bizDesc;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }


    @Override
    public String toString() {
        return "OpLogMetaDataDO{" +
                "id=" + id +
                ", traceId='" + traceId + '\'' +
                ", opId='" + opId + '\'' +
                ", bizId='" + bizId + '\'' +
                ", bizDesc='" + bizDesc + '\'' +
                ", status=" + status +
                ", metaData='" + metaData + '\'' +
                '}';
    }
}
