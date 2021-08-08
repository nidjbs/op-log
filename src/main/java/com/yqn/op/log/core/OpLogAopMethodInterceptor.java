package com.yqn.op.log.core;

import com.yqn.op.log.annotations.OpLog;
import com.yqn.op.log.common.SmartOptional;
import com.yqn.op.log.config.OpLogConfig;
import com.yqn.op.log.core.mapping.OpLogContextMappingTask;
import com.yqn.op.log.core.mapping.OpLogMappingProcessCenter;
import com.yqn.op.log.core.mybatis.processor.DeleteMybatisOpLogInterceptorProcessor;
import com.yqn.op.log.core.support.BizTraceSupport;
import com.yqn.op.log.core.support.OpBizIdSupport;
import com.yqn.op.log.util.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author huayuanlin
 * @date 2021/06/10 20:29
 * @desc the core aop interceptor class
 */
public class OpLogAopMethodInterceptor implements MethodInterceptor {

    private OpLogAopMethodInterceptor() {

    }

    public static OpLogAopMethodInterceptor getInstance() {
        return OpLogAopMethodInterceptor.Holder.INSTANCE;
    }

    private static class Holder {
        private static final OpLogAopMethodInterceptor INSTANCE = new OpLogAopMethodInterceptor();
    }

    protected static final ThreadLocal<OpLogContext> OP_LOG_CONTEXT =
            ThreadLocal.withInitial(OpLogAopMethodInterceptor::initOpLogContext);

    private static final Logger LOGGER = LoggerFactory.getLogger(OpLogAopMethodInterceptor.class);

    /**
     * init op log context
     *
     * @return result
     */
    private static OpLogContext initOpLogContext() {
        BizTraceSupport bizTraceSupport = SpringBeanUtil.getBeanByType(BizTraceSupport.class);
        OpLogContext opLogContext = new OpLogContext();
        if (bizTraceSupport == null) {
            LOGGER.warn("bizTraceSupport bean not exist");
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
            BizTrace bizTrace = opLogContext.getBizTrace();
            bizTrace.setBizDesc(opLog.bizDesc());
            String bizIdEl = opLog.bizIdEl();
            // get biz id by spring el
            Object doEl = SpringUtil.doExecuteEl(bizIdEl, methodInvocation.getMethod(), methodInvocation.getArguments());
            SmartOptional.ofNullable(doEl)
                    .ifPresent(elResult -> opLogContext.getBizTrace().setBizId(StringUtil.parseStr(elResult)));
            ISqlLogMetaDataService logMetaDataService = SpringBeanUtil.getBeanByType(ISqlLogMetaDataService.class);
            Long logId = logMetaDataService.insert(logMetaDataService.doConvert());
            opLogContext.setOpLogId(logId);
            // async process mapping
            OpLogMappingProcessCenter.submitTask(new OpLogContextMappingTask(opLogContext));
        } catch (Exception exx) {
            LOGGER.warn("sync record log process fail,does not affect the execution logic", exx);
        } finally {
            OP_LOG_CONTEXT.remove();
        }
        return result;
    }
}
