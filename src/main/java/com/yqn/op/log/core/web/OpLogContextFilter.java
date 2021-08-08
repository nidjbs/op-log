package com.yqn.op.log.core.web;

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
        RequestHeaderWrapper wrapper = new RequestHeaderWrapper((HttpServletRequest) request);
        HttpServletRequest req = ((ServletRequestAttributes) Objects.
                requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        chain.doFilter(wrapper, response);
    }

    private void getHeaderOrCreate(RequestHeaderWrapper wrapper, String key, Supplier<String> supplier) {
        String header = wrapper.getHeader(key);
        if (StringUtil.isEmpty(header)) {
            wrapper.addHeader(key, supplier.get());
        }
    }
}
