package com.yqn.op.log.core.web;

import com.yqn.op.log.common.OpLogConstant;
import com.yqn.op.log.core.OpLogGlobalContext;
import com.yqn.op.log.core.OpLogGlobalContextHolder;
import com.yqn.op.log.util.JsonUtil;
import com.yqn.op.log.util.StringUtil;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author huayuanlin
 * @date 2021/08/06 17:53
 * @desc the class desc
 */
public class OpLogContextFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String contextJson = ((HttpServletRequest) request).getHeader(OpLogConstant.CONTEXT_HANDLER_KEY);
        if (StringUtil.isNotEmpty(contextJson)) {
            OpLogGlobalContext opLogGlobalContext = JsonUtil.fromJson(contextJson, OpLogGlobalContext.class);
            try {
                OpLogGlobalContextHolder.init(opLogGlobalContext);
                chain.doFilter(request, response);
            } finally {
                OpLogGlobalContextHolder.clean();
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
