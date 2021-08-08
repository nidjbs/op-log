package com.yqn.op.log.config;

import com.yqn.op.log.core.OpLogGlobalAopMethodInterceptor;
import com.yqn.op.log.core.OpLogGlobalAopProxyCreator;
import com.yqn.op.log.core.web.FeignRequestInterceptor;
import com.yqn.op.log.core.web.RpcRequestInterceptor;
import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class OpLogGlobalAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpLogGlobalAopProxyCreator opLogGlobalAopProxyCreator() {
        return new OpLogGlobalAopProxyCreator(OpLogGlobalAopMethodInterceptor.getInstance());
    }

    @Bean
    @ConditionalOnClass(Feign.class)
    public RequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

}
