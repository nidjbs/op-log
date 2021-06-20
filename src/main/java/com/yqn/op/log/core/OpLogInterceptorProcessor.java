package com.yqn.op.log.core;

import com.yqn.op.log.common.Tuple;

/**
 * @author huayuanlin
 * @date 2021/06/14 20:23
 * @desc the interface desc
 */
public interface OpLogInterceptorProcessor<T> {

    /**
     * parse meta data
     *
     * @param invocation interceptor param
     * @return meta data with invocation result
     */
    Tuple<SqlMetaData, Object> parseMetaData(T invocation);

}
