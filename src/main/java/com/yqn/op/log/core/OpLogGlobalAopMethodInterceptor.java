package com.yqn.op.log.core;

import com.yqn.op.log.annotations.OpLogGlobal;
import com.yqn.op.log.common.ObjBuilder;
import com.yqn.op.log.config.OpLogGlobalConfig;
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

    private OpLogGlobalAopMethodInterceptor() {

    }

    public static OpLogGlobalAopMethodInterceptor getInstance() {
        return OpLogGlobalAopMethodInterceptor.Holder.INSTANCE;
    }

    private static class Holder {
        private static final OpLogGlobalAopMethodInterceptor INSTANCE = new OpLogGlobalAopMethodInterceptor();
    }

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

    private void initGlobalContext(MethodInvocation invocation) {
        OpLogGlobalConfig logGlobalConfig = SpringBeanUtil.getBeanByType(OpLogGlobalConfig.class);
        AssertUtil.notNull(logGlobalConfig, "logGlobalConfig bean");
        String bizCode = logGlobalConfig.getBizCode();
        OpLogGlobal opLogGlobal = invocation.getMethod().getAnnotation(OpLogGlobal.class);
        AssertUtil.notNull(opLogGlobal, "opLogGlobal annotation");
        String bizIdEl = opLogGlobal.bizIdEl();
        Object elResult = SpringUtil.doExecuteEl(bizIdEl, invocation.getMethod(), invocation.getArguments());
        String bizId = StringUtil.parseStr(elResult);
        String opType = opLogGlobal.opType();
        OpLogGlobalContext opLogGlobalContext = ObjBuilder.create(OpLogGlobalContext::new)
                .of(OpLogGlobalContext::setBizCode, bizCode)
                .of(OpLogGlobalContext::setOpType, opType)
                .of(OpLogGlobalContext::setBizId, bizId)
                .build();
        OpLogGlobalContextHolder.init(opLogGlobalContext);
    }
}
