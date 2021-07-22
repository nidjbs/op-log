package com.yqn.op.log.core.mybatis.processor;

import com.yqn.op.log.core.mybatis.MybatisInvocationWrapper;
import com.yqn.op.log.core.mybatis.MybatisOpLogInterceptorProcessor;
import com.yqn.op.log.util.BeanUtil;
import com.yqn.op.log.util.CollectionsUtil;
import com.yqn.op.log.util.MapsUtil;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

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
    public List<Map<String, Object>> getAfterDataList(MybatisInvocationWrapper invocationWrapper) {
        BoundSql boundSql = invocationWrapper.getBoundSql();
        Configuration configuration = invocationWrapper.getMappedStatement().getConfiguration();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        List<Map<String, Object>> resultList = CollectionsUtil.arrayList();
        // batch insert
        if (parameterObject instanceof Map) {
            Set<List<Object>> temp = CollectionsUtil.hashSet();
            ((Map<String, Object>) parameterObject).forEach((k, v) -> {
                if (v instanceof List) {
                    temp.add((List<Object>) v);
                } else {
                    LOGGER.debug("op log batch insert only support list params");
                }
            });
            Iterator<List<Object>> iterator = temp.iterator();
            while (iterator.hasNext()) {
                List<Object> next = iterator.next();
                next.forEach(e -> resultList.add(BeanUtil.toMap(e)));
            }
        } else {
            Map<String, Object> result = MapsUtil.hashMap();
            parameterMappings.forEach(parameterMapping -> {
                String property = parameterMapping.getProperty();
                Object value = metaObject.getValue(property);
                result.put(property, value);
            });
            resultList.add(result);
        }
        return resultList;
    }

    public static InsertMybatisOpLogInterceptorProcessor getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final InsertMybatisOpLogInterceptorProcessor
                INSTANCE = new InsertMybatisOpLogInterceptorProcessor();
    }
}
