package com.yqn.op.log.core;

import com.yqn.op.log.annotations.OpLogGlobal;
import com.yqn.op.log.common.ObjBuilder;
import com.yqn.op.log.config.OpLogGlobalConfig;
import com.yqn.op.log.core.support.BizTraceSupport;
import com.yqn.op.log.util.AssertUtil;
import com.yqn.op.log.util.SpringBeanUtil;
import com.yqn.op.log.util.SpringUtil;
import com.yqn.op.log.util.StringUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author huayuanlin
 * @date 2021/08/07 22:03
 * @desc the class desc
 */
public class OpLogGlobalAopMethodInterceptor implements MethodInterceptor {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            // try init global context
            initGlobalContext(invocation);
            return invocation.proceed();
        }finally {
            // clean context
            OpLogGlobalContextHolder.clean();
        }
    }

    /**
     * Try to initialize the global context.
     * prioritized: BizTraceSupport < OpLogGlobalConfig < the OpLogGlobal annotation
     *
     * @see BizTraceSupport
     * @see OpLogGlobalConfig
     * @see OpLogGlobal
     * @param invocation method invocation
     */
    private void initGlobalContext(MethodInvocation invocation) {
        BizTraceSupport bizTraceSupport = SpringBeanUtil.getBeanByType(BizTraceSupport.class);
        String bizCode = bizTraceSupport.bizCode();
        String bizId = bizTraceSupport.bizId();
        String traceId = bizTraceSupport.traceId();
        String opId = bizTraceSupport.opId();
        OpLogGlobalConfig logGlobalConfig = SpringBeanUtil.getBeanByType(OpLogGlobalConfig.class);
        AssertUtil.notNull(logGlobalConfig, "logGlobalConfig bean");
        if (StringUtil.isNotEmpty(logGlobalConfig.getBizCode())) {
            bizCode = logGlobalConfig.getBizCode();
        }
        OpLogGlobal opLogGlobal = invocation.getMethod().getAnnotation(OpLogGlobal.class);
        AssertUtil.notNull(opLogGlobal, "opLogGlobal annotation");
        String bizIdEl = opLogGlobal.bizIdEl();
        Object elResult = SpringUtil.doExecuteEl(bizIdEl, invocation.getMethod(), invocation.getArguments());
        String parseBizId = StringUtil.parseStr(elResult);
        if (StringUtil.isNotEmpty(parseBizId)) {
            bizId = parseBizId;
        }
        String opType = opLogGlobal.opType();
        OpLogGlobalContext opLogGlobalContext = ObjBuilder.create(OpLogGlobalContext::new)
                .of(OpLogGlobalContext::setBizCode, bizCode)
                .of(OpLogGlobalContext::setOpType, opType)
                .of(OpLogGlobalContext::setBizId, bizId)
                .of(OpLogGlobalContext::setTraceId,traceId)
                .of(OpLogGlobalContext::setOpId,opId)
                .build();
        OpLogGlobalContextHolder.init(opLogGlobalContext);
    }
}
