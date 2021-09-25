package com.yqn.op.log.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author huayuanlin
 * @date 2021/09/12 23:32
 * @desc the class desc
 */
public abstract class OpLogMethodInterceptor implements MethodInterceptor {

    @Override
    public final Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Annotation annotation = method.getAnnotation(annotation());
        if (annotation == null) {
            return invocation.proceed();
        } else {
            return doIntercept(invocation);
        }
    }

    /**
     * annotation class on interception method
     *
     * @return annotation
     */
    protected abstract Class<? extends Annotation> annotation();

    /**
     * do intercept
     *
     * @return result
     * @param invocation invocation
     * @throws Throwable exx
     */
    protected abstract Object doIntercept(MethodInvocation invocation) throws Throwable;
}
