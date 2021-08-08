package com.yqn.op.log.core.support;

import com.yqn.op.log.config.OpLogGlobalConfig;

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

    /**
     * optional
     * but, the OpLogGlobalConfig.bizCode higher priority
     * @see OpLogGlobalConfig
     * @return bizCode str
     */
    String bizCode();

}
