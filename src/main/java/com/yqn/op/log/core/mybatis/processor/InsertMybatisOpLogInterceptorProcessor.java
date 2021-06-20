package com.yqn.op.log.core.mybatis.processor;

import com.yqn.op.log.core.mybatis.MybatisInvocationWrapper;
import com.yqn.op.log.core.mybatis.MybatisOpLogInterceptorProcessor;
import com.yqn.op.log.util.CollectionsUtil;
import com.yqn.op.log.util.MapsUtil;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/06/15 22:49
 * @desc the insert MybatisOpLogInterceptorProcessor
 */
@SuppressWarnings("all")
public class InsertMybatisOpLogInterceptorProcessor extends MybatisOpLogInterceptorProcessor {

    private InsertMybatisOpLogInterceptorProcessor() {

    }


    @Override
    protected Map<String, Object> getAfterData(MybatisInvocationWrapper invocationWrapper) {
        BoundSql boundSql = invocationWrapper.getBoundSql();
        Configuration configuration = invocationWrapper.getMappedStatement().getConfiguration();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        // batch insert
        Map<String, Object> result = MapsUtil.hashMap();
        if (parameterObject instanceof Map) {
            Set<List<Object>> temp = CollectionsUtil.hashSet();
            ((Map<String, Object>) parameterObject).forEach((k, v) -> {
                if (v instanceof List) {
                    temp.add((List<Object>) v);
                } else {
                    LOGGER.warn("op log batch insert only support list params");
                }
            });
            String itemNamePre = "item_";
            Iterator<List<Object>> iterator = temp.iterator();
            int itemCount = 0;
            while (iterator.hasNext()) {
                result.put(itemNamePre + itemCount, iterator.next());
                itemCount++;
            }
        } else {
            parameterMappings.forEach(parameterMapping -> {
                String property = parameterMapping.getProperty();
                Object value = metaObject.getValue(property);
                result.put(property, value);
            });
        }
        return result;
    }

    public static InsertMybatisOpLogInterceptorProcessor getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final InsertMybatisOpLogInterceptorProcessor
                INSTANCE = new InsertMybatisOpLogInterceptorProcessor();
    }
}
