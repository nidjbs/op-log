package com.yqn.op.log.core;

import com.yqn.op.log.annotations.OpLog;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author huayuanlin
 * @date 2021/06/10 20:12
 * @desc the class desc
 */
public class OpLogAopScanner extends AbstractAutoProxyCreator {

    private static final Set<String> ALREADY_PROXY_BEAN_SET = new HashSet<>();

    private static final Object LOCK = new Object();

    private MethodInterceptor interceptor;

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        synchronized (LOCK) {
            if (ALREADY_PROXY_BEAN_SET.contains(beanName)) {
                return bean;
            }
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            if (!existAnnotation(targetClass)) {
                return bean;
            }
            if (interceptor == null) {
                interceptor = new OpLogAopMethodInterceptor();
            }
            try {
                if (!AopUtils.isAopProxy(bean)) {
                    bean = super.wrapIfNecessary(bean, beanName, cacheKey);
                } else {
                    AdvisedSupport curProxyAdvisedSupportField = getCurProxyAdvisedSupportField(bean);
                    Advisor[] advisors = buildAdvisors(beanName, new Object[]{interceptor});
                    Stream.of(advisors).forEach(advisor -> curProxyAdvisedSupportField.addAdvisor(0, advisor));
                }
                ALREADY_PROXY_BEAN_SET.add(beanName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return bean;
        }
    }


    /**
     * is exist annotation
     *
     * @param targetClass target obj
     * @return boolean
     */
    private boolean existAnnotation(Class<?> targetClass) {
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            OpLog opLog = method.getAnnotation(OpLog.class);
            if (opLog != null) {
                return true;
            }
        }
        return false;
    }


    /**
     * get aop bean Advisor manager(support)
     *
     * @param proxy proxy
     * @return bean Advisor
     * @throws Exception ex
     */
    public static AdvisedSupport getCurProxyAdvisedSupportField(Object proxy) throws Exception {
        Field h;
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            h = proxy.getClass().getSuperclass().getDeclaredField("h");
        } else {
            h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        }
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return (AdvisedSupport) advised.get(dynamicAdvisedInterceptor);
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> aClass, String s, TargetSource targetSource) throws BeansException {
        return new Object[]{interceptor};
    }
}
