package com.yqn.op.log.config;

import com.yqn.op.log.util.SpringBeanUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

/**
 * @author huayuanlin
 * @date 2021/08/05 21:15
 * @desc the class desc spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
 */
public class TestBean extends InstantiationAwareBeanPostProcessorAdapter implements SmartInitializingSingleton {


    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        if (beanClass == DataSourceProperties.class) {
            System.out.println("aaa");
        }
        return null;
    }

    @Override
    public void afterSingletonsInstantiated() {
        DataSource beanByType = SpringBeanUtil.getBeanByType(DataSource.class);
        DataSource dataSourceProperties = SpringBeanUtil.getBeanByName("dataSource", DataSource.class);
//        System.out.println(dataSourceProperties);
        System.out.println("12124");
    }
}
