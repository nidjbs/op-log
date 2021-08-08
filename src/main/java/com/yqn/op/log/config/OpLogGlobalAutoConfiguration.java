package com.yqn.op.log.config;

import com.yqn.op.log.core.OpLogGlobalAopMethodInterceptor;
import com.yqn.op.log.core.OpLogGlobalAopProxyCreator;
import com.yqn.op.log.core.web.FeignRequestInterceptor;
import com.yqn.op.log.core.web.OpLogContextFilter;
import com.yqn.op.log.core.web.RpcRequestInterceptor;
import feign.Feign;
import feign.RequestInterceptor;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huayuanlin
 * @date 2021/08/07 21:40
 * @desc the class desc
 */
@Configuration
@EnableConfigurationProperties(OpLogGlobalConfig.class)
public class OpLogGlobalAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpLogGlobalAopProxyCreator opLogGlobalAopProxyCreator() {
        return new OpLogGlobalAopProxyCreator(new OpLogGlobalAopMethodInterceptor());
    }

    @Bean
    public FilterRegistrationBean<OpLogContextFilter> opLogContextFilter() {
        FilterRegistrationBean<OpLogContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new OpLogContextFilter());
        registration.addUrlPatterns("/*");
        registration.setName("op_log_context");
        registration.setEnabled(true);
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }

}
