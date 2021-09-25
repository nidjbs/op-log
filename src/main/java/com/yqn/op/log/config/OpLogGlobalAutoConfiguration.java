package com.yqn.op.log.config;

import com.yqn.op.log.core.OpLogGlobalAopMethodInterceptor;
import com.yqn.op.log.core.OpLogGlobalAopProxyCreator;
import com.yqn.op.log.util.SpringBeanUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huayuanlin
 * @date 2021/08/07 21:40
 * @desc the class desc
 */
@Configuration
@EnableConfigurationProperties(OpLogGlobalConfig.class)
@ConditionalOnProperty(prefix = "op.log.global", name = "enable", havingValue = "true")
public class OpLogGlobalAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpLogGlobalAopProxyCreator opLogGlobalAopProxyCreator() {
        return new OpLogGlobalAopProxyCreator(new OpLogGlobalAopMethodInterceptor());
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBeanUtil opLogSpringBeanUtil() {
        return new SpringBeanUtil();
    }
}
