package com.yqn.op.log.core;

import com.yqn.op.log.annotations.OpLog;
import com.yqn.op.log.common.SmartOptional;
import com.yqn.op.log.config.OpLogConfig;
import com.yqn.op.log.core.mapping.OpLogContextMappingTask;
import com.yqn.op.log.core.mapping.OpLogMappingProcessCenter;
import com.yqn.op.log.core.support.BizTraceSupport;
import com.yqn.op.log.util.*;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * @author huayuanlin
 * @date 2021/06/10 20:29
 * @desc the core aop interceptor class
 */
public class OpLogAopMethodInterceptor extends OpLogMethodInterceptor {

    protected static final ThreadLocal<OpLogContext> OP_LOG_CONTEXT =
            ThreadLocal.withInitial(OpLogAopMethodInterceptor::initOpLogContext);

    protected static final ThreadLocal<Boolean> CUR_OPEN_OP_LOG = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private static final Logger LOGGER = LoggerFactory.getLogger(OpLogAopMethodInterceptor.class);

    /**
     * init op log context
     *
     * @return result
     */
    private static OpLogContext initOpLogContext() {
        OpLogContext opLogContext = new OpLogContext();
        OpLogGlobalContext opLogGlobalContext = OpLogGlobalContextHolder.getContext();
        SmartOptional.ofNullable(opLogGlobalContext).ifPresent(opLogContext::setOpLogGlobalContext);
        BizTraceSupport bizTraceSupport = SpringBeanUtil.safeGetBeanByType(BizTraceSupport.class);
        if (bizTraceSupport == null) {
            LOGGER.debug("bizTraceSupport bean not exist");
            return opLogContext;
        }
        if (opLogGlobalContext == null) {
            opLogGlobalContext = new OpLogGlobalContext();
            opLogContext.setOpLogGlobalContext(opLogGlobalContext);
        }
        StringUtil.setIfNotEmpty(opLogGlobalContext::setOpId, bizTraceSupport.opId());
        StringUtil.setIfNotEmpty(opLogGlobalContext::setTraceId, bizTraceSupport.traceId());
        StringUtil.setIfNotEmpty(opLogGlobalContext::setBizId, bizTraceSupport.bizId());
        StringUtil.setIfNotEmpty(opLogGlobalContext::setBizCode, bizTraceSupport.bizCode());
        return opLogContext;
    }


    @Override
    protected Class<? extends Annotation> annotation() {
        return OpLog.class;
    }

    @Override
    protected Object doIntercept(MethodInvocation invocation) throws Throwable {
        boolean curExistTx = ManualTxUtil.curExistTx();
        if (!curExistTx) {
            return runWithManualTx(invocation);
        } else {
            return runWithTx(invocation);
        }
    }

    /**
     * run with manual tx
     *
     * @param methodInvocation methodInvocation
     * @return result
     */
    private Object runWithManualTx(MethodInvocation methodInvocation) {
        ManualTxParams<Object> manualTxParams = new ManualTxParams<>();
        OpLogConfig opLogConfig = SpringBeanUtil.getBeanByType(OpLogConfig.class);
        AssertUtil.notNull(opLogConfig, "opLogConfig");
        manualTxParams.setTimeout(opLogConfig.getManualTxTimeOut());
        manualTxParams.setFunc(param -> {
            try {
                return this.invoke(methodInvocation);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
        return ManualTxUtil.startTxWithOutParams(manualTxParams);
    }

    /**
     * run with tx
     *
     * @param methodInvocation methodInvocation
     * @return result
     */
    private Object runWithTx(MethodInvocation methodInvocation) throws Throwable {
        Object result;
        CUR_OPEN_OP_LOG.set(Boolean.TRUE);
        // process the execution logic (sql invocation)
        result = methodInvocation.proceed();
        try {
            // sync record log
            OpLogContext opLogContext = OpLogContextProvider.opLogContext();
            if (opLogContext == null) {
                return result;
            }
            OpLog opLog = methodInvocation.getMethod().getDeclaredAnnotation(OpLog.class);
            AssertUtil.notNull(opLog, "op Log annotation");
            OpLogGlobalContext opLogGlobalContext = opLogContext.getOpLogGlobalContext();
            StringUtil.setIfNotEmpty(opLogGlobalContext::setOpType, opLog.bizType());
            String bizIdEl = opLog.bizIdEl();
            // get biz id by spring el
            Object doEl = SpringUtil.doExecuteEl(bizIdEl, methodInvocation.getMethod(), methodInvocation.getArguments());
            SmartOptional.ofNullable(doEl).ifPresent(elResult -> opLogGlobalContext.setBizId(StringUtil.parseStr(elResult)));
            ISqlLogMetaDataService logMetaDataService = SpringBeanUtil.getBeanByType(ISqlLogMetaDataService.class);
            Long logId = logMetaDataService.insert(logMetaDataService.doConvert());
            opLogContext.setOpLogId(logId);
            // async process raw log
            OpLogMappingProcessCenter.submitTask(new OpLogContextMappingTask(opLogContext));
        } catch (Exception exx) {
            LOGGER.warn("sync op log process fail,does not affect the execution logic", exx);
        } finally {
            OP_LOG_CONTEXT.remove();
            CUR_OPEN_OP_LOG.remove();
        }
        return result;
    }
}
