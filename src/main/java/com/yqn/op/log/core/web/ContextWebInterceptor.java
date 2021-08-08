package com.yqn.op.log.core.web;

import com.yqn.op.log.util.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huayuanlin
 * @date 2021/08/06 17:14
 * @desc the class desc
 */
public class ContextWebInterceptor implements HandlerInterceptor {

    private static final String OP_LOG_CONTEXT_KEY = "OP_LOG_CONTEXT";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String context = request.getHeader(OP_LOG_CONTEXT_KEY);
        if (StringUtil.isEmpty(context)) {

        }
        return true;
    }
}
