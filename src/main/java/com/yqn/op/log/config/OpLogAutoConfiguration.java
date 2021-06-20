package com.yqn.op.log.config;

import com.yqn.op.log.core.OpLogAopScanner;
import com.yqn.op.log.core.mapping.OpLogMappingProcessCenter;
import com.yqn.op.log.util.SpringBeanUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huayuanlin
 * @date 2021/06/20 14:59
 * @desc the class desc
 */
@Configuration
@EnableConfigurationProperties(OpLogConfig.class)
public class OpLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpLogAopScanner opLogAopScanner() {
        return new OpLogAopScanner();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBeanUtil opLogSpringBeanUtil() {
        return new SpringBeanUtil();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpLogMappingProcessCenter opLogMappingProcessCenter() {
        return new OpLogMappingProcessCenter();
    }
}
