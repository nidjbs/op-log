package com.yqn.op.log.core.mybatis.processor;

import com.yqn.op.log.core.mybatis.MybatisInvocationWrapper;
import com.yqn.op.log.core.mybatis.MybatisOpLogInterceptorProcessor;
import com.yqn.op.log.util.MapsUtil;

import java.util.List;
import java.util.Map;

/**
 * @author huayuanlin
 * @date 2021/06/19 13:39
 * @desc the class desc
 */
public class SoftDeleteMybatisOpLogInterceptorProcessor extends MybatisOpLogInterceptorProcessor {


    @Override
    public List<Map<String, Object>> getBeforeDataList(MybatisInvocationWrapper invocationWrapper) {
        return DeleteMybatisOpLogInterceptorProcessor.getInstance().getBeforeDataList(invocationWrapper);
    }

    public static SoftDeleteMybatisOpLogInterceptorProcessor getInstance() {
        return SoftDeleteMybatisOpLogInterceptorProcessor.Holder.INSTANCE;
    }

    private static class Holder {
        private static final SoftDeleteMybatisOpLogInterceptorProcessor
                INSTANCE = new SoftDeleteMybatisOpLogInterceptorProcessor();
    }
}
