package com.yqn.op.log.config;

import com.yqn.op.log.core.*;
import com.yqn.op.log.core.mapping.IMappingLogDbService;
import com.yqn.op.log.core.mapping.JdbcTemplateMappingLogDbServiceImpl;
import com.yqn.op.log.core.mapping.OpLogMappingProcessCenter;
import com.yqn.op.log.core.mapping.ProcessRawMappingBean;
import com.yqn.op.log.core.web.FeignRequestInterceptor;
import com.yqn.op.log.util.SpringBeanUtil;
import feign.Feign;
import feign.RequestInterceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
    public OpLogAopProxyCreator opLogAopScanner() {
        return new OpLogAopProxyCreator(new OpLogAopMethodInterceptor());
    }

    @Bean
    @ConditionalOnMissingBean
    public ProcessRawMappingBean processRawMappingBean() {
        return new ProcessRawMappingBean();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBeanUtil opLogSpringBeanUtil() {
        return new SpringBeanUtil();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISqlLogMetaDataService iSqlLogMetaDataService() {
        return new JdbcTemplateSqlLogMetaDataServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public IMappingLogDbService jdbcMappingLogService() {
        return new JdbcTemplateMappingLogDbServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpLogMappingProcessCenter opLogMappingProcessCenter() {
        return new OpLogMappingProcessCenter();
    }

    @Bean
    @ConditionalOnClass(Feign.class)
    public RequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
