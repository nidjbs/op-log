package com.yqn.op.log.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author huayuanlin
 * @date 2021-05-26 20:56:02
 * @desc the SpringBeanUtil
 */
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    public static <T> T getBeanByType(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBeanByName(String beanName, Class<T> tClass) {
        return (T) applicationContext.getBean(beanName);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
