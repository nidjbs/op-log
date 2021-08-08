package com.yqn.op.log.core.web;

import com.yqn.op.log.common.OpLogConstant;
import com.yqn.op.log.core.OpLogGlobalContext;
import com.yqn.op.log.core.OpLogGlobalContextHolder;
import com.yqn.op.log.util.BeanUtil;
import com.yqn.op.log.util.JsonUtil;
import com.yqn.op.log.util.StringUtil;
import com.yqn.op.log.util.WebUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author huayuanlin
 * @date 2021/08/06 18:40
 * @desc Used to transfer context between services
 */
public class FeignRequestInterceptor implements RequestInterceptor, RpcRequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        OpLogGlobalContext opLogGlobalContext = OpLogGlobalContextHolder.getContext();
        if (opLogGlobalContext != null) {
            requestTemplate.header(OpLogConstant.CONTEXT_HANDLER_KEY, JsonUtil.toJsonString(opLogGlobalContext));
        }
    }
}
