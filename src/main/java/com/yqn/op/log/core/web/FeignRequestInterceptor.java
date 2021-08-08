package com.yqn.op.log.core.web;

import com.yqn.op.log.core.OpLogGlobalContext;
import com.yqn.op.log.core.OpLogGlobalContextHolder;
import com.yqn.op.log.util.BeanUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author huayuanlin
 * @date 2021/08/06 18:40
 * @desc the class desc
 */
public class FeignRequestInterceptor implements RequestInterceptor,RpcRequestInterceptor {

    private static final String OP_TYPE_KEY = "opType";
    private static final String BIZ_CODE_KEY = "bizCode";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        OpLogGlobalContext opLogGlobalContext = OpLogGlobalContextHolder.getContext();
        if (opLogGlobalContext != null) {
            requestTemplate.header(OP_TYPE_KEY,opLogGlobalContext.getOpType());
            requestTemplate.header(BIZ_CODE_KEY,opLogGlobalContext.getBizCode());
        }
    }
}
