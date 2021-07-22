package com.yqn.op.log.core.support;

/**
 * @author huayuanlin
 * @date 2021/06/17 10:29
 * @desc the interface desc
 */
public interface BizTraceSupport {

    /**
     * optional
     * @return traceId str
     */
    String traceId();

    /**
     * optional
     * @return opId str
     */
    String opId();

    /**
     * optional
     * @return bizId str
     */
    String bizId();

}
