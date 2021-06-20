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

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
