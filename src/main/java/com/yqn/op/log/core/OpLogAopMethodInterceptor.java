package com.yqn.op.log.core;

import com.yqn.op.log.annotations.OpLog;
import com.yqn.op.log.config.OpLogConfig;
import com.yqn.op.log.core.mapping.OpLogContextMappingTask;
import com.yqn.op.log.core.mapping.OpLogMappingProcessCenter;
import com.yqn.op.log.util.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author huayuanlin
 * @date 2021/06/10 20:29
 * @desc the class desc
 */
public class OpLogAopMethodInterceptor implements MethodInterceptor {

    protected static final ThreadLocal<OpLogContext> OP_LOG_CONTEXT = ThreadLocal.withInitial(OpLogAopMethodInterceptor::initOpLogContext);

    /**
     * init op log context
     *
     * @return result
     */
    private static OpLogContext initOpLogContext() {
        BizTraceSupport bizTraceSupport = SpringBeanUtil.getBeanByType(BizTraceSupport.class);
        OpLogContext opLogContext = new OpLogContext();
        if (bizTraceSupport == null) {
            return opLogContext;
        }
        String opId = bizTraceSupport.opId();
        String traceId = bizTraceSupport.traceId();
        BizTrace bizTrace = new BizTrace();
        bizTrace.setBizOpId(opId);
        bizTrace.setTraceId(traceId);
        opLogContext.setBizTrace(bizTrace);
        opLogContext.setSqlMetaDataList(CollectionsUtil.arrayList());
        return opLogContext;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        boolean curExistTx = ManualTxUtil.curExistTx();
        if (!curExistTx) {
            return runWithManualTx(methodInvocation);
        } else {
            return runWithTx(methodInvocation);
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
                return invoke(methodInvocation);
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
        try {
            // process (sql invocation)
            result = methodInvocation.proceed();
            // sync record log
            OpLogContext opLogContext = OpLogContextProvider.opLogContext();
            if (opLogContext == null) {
                return result;
            }
            OpLog opLog = methodInvocation.getMethod().getAnnotation(OpLog.class);
            BizTrace bizTrace = opLogContext.getBizTrace();
            bizTrace.setBizDesc(opLog.bizDesc());
            ISqlLogMetaDataService logMetaDataService = SpringBeanUtil.getBeanByType(ISqlLogMetaDataService.class);
            logMetaDataService.insert(logMetaDataService.doConvert());
            // async process mapping
            OpLogMappingProcessCenter.submitTask(new OpLogContextMappingTask(opLogContext));
        } finally {
            OP_LOG_CONTEXT.remove();
        }
        return result;
    }

}
