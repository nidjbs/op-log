package com.yqn.op.log.core.mybatis;

import com.yqn.op.log.common.FrameworkException;
import com.yqn.op.log.core.mybatis.processor.DeleteMybatisOpLogInterceptorProcessor;
import com.yqn.op.log.core.mybatis.processor.InsertMybatisOpLogInterceptorProcessor;
import com.yqn.op.log.core.mybatis.processor.SoftDeleteMybatisOpLogInterceptorProcessor;
import com.yqn.op.log.core.mybatis.processor.UpdateMybatisOpLogInterceptorProcessor;
import com.yqn.op.log.enums.SqlType;

/**
 * @author huayuanlin
 * @date 2021/06/16 11:28
 * @desc the class desc
 */
public class OpLogInterceptorProcessorProvider {

    private OpLogInterceptorProcessorProvider() {
        throw new UnsupportedOperationException();
    }

    /**
     * get mybatis op log interceptor processor
     *
     * @param sqlType sql type
     * @return interceptor processor
     */
    public static MybatisOpLogInterceptorProcessor mybatisOpLogInterceptorProcessor(SqlType sqlType) {
        switch (sqlType) {
            case INSERT:
                return InsertMybatisOpLogInterceptorProcessor.getInstance();
            case DELETE:
                return DeleteMybatisOpLogInterceptorProcessor.getInstance();
            case SOFT_DELETE:
                return SoftDeleteMybatisOpLogInterceptorProcessor.getInstance();
            case UPDATE:
                return UpdateMybatisOpLogInterceptorProcessor.getInstance();
            default:
                throw new FrameworkException("not found op log interceptor processor");
        }
    }

}
