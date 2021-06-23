package com.yqn.op.log.core.mybatis.processor;

import com.yqn.op.log.common.OpLogConstant;
import com.yqn.op.log.core.mybatis.MybatisInvocationWrapper;
import com.yqn.op.log.core.mybatis.MybatisOpLogInterceptorProcessor;
import com.yqn.op.log.core.mybatis.MybatisSqlMetaData;
import com.yqn.op.log.util.CollectionsUtil;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author huayuanlin
 * @date 2021/06/17 14:21
 * @desc the update MybatisOpLogInterceptorProcessor
 */
public class UpdateMybatisOpLogInterceptorProcessor extends MybatisOpLogInterceptorProcessor {

    private UpdateMybatisOpLogInterceptorProcessor() {

    }


    @Override
    public List<Map<String, Object>> getBeforeDataList(MybatisInvocationWrapper invocationWrapper) {
        // separate update column and condition
        MybatisSqlMetaData sqlMetaDataByContext = getSqlMetaDataByContext();
        String sql = sqlMetaDataByContext.getSql();
        String selectSqlParam = formatToSelectSqlParam(sql);
        BoundSql boundSql = invocationWrapper.getBoundSql();
        Object[] args = invocationWrapper.getInvocation().getArgs();
        // get connection
        Connection connection = (Connection) args[0];
        String selectSql = getSelectSql(sql, sqlMetaDataByContext.getTableName());
        Configuration configuration = invocationWrapper.getMappedStatement().getConfiguration();
        return doSelect(connection, selectSql, getSqlParams(selectSqlParam, boundSql, configuration));
    }

    /**
     * get sql prams
     *
     * @param selectSqlParam sql
     * @param boundSql       boundSql
     * @param configuration  configuration
     * @return sql prams
     */
    private List<Object> getSqlParams(String selectSqlParam, BoundSql boundSql, Configuration configuration) {
        // count contain param
        char[] chars = selectSqlParam.toCharArray();
        int count = 0;
        for (char aChar : chars) {
            if (aChar == '?') {
                count++;
            }
        }
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        int size = parameterMappings.size();
        List<Object> params = CollectionsUtil.arrayList();
        for (int i = size - count; i < size; i++) {
            params.add(metaObject.getValue(parameterMappings.get(i).getProperty()));
        }
        return params;
    }

    /**
     * update sql to select
     * ex: update A set a = ?, b = ? where id = ?  => select a,b from A where id = ?
     *
     * @param updateSql src sql
     * @param tableName tableName
     * @return select sql
     */
    private String getSelectSql(String updateSql, String tableName) {
        // get param sql : id = ?
        String selectSqlParam = formatToSelectSqlParam(updateSql);
        updateSql = updateSql.replaceAll(" ", "");
        // Remove update
        String var1 = removeStr(updateSql, OpLogConstant.UPDATE);
        // remove table name
        String var2 = removeStr(var1, tableName);
        // remove set
        String var3 = removeStr(var2, OpLogConstant.SET);
        // => a = ? ,
        String var4 = var3.substring(0, var3.lastIndexOf(OpLogConstant.WHERE));
        String var5 = var4.replaceAll("=?", " ");
        return String.format(OpLogConstant.SELECT_SQL_FORMAT, var5, tableName, selectSqlParam);
    }

    private String removeStr(String src, String target) {
        return src.substring(src.indexOf(target) + 1);
    }

    /**
     * format to select sql params sql
     * ex: update A set a = ?, b = ? where id = ?  => id = ?
     *
     * @param updateSql updateSql sql
     * @return select sql params sql
     */
    private String formatToSelectSqlParam(String updateSql) {
        String toUpperCase = updateSql.toUpperCase();
        return toUpperCase.substring(toUpperCase.lastIndexOf("WHERE") + 1);
    }


    public static UpdateMybatisOpLogInterceptorProcessor getInstance() {
        return UpdateMybatisOpLogInterceptorProcessor.Holder.INSTANCE;
    }

    private static class Holder {
        private static final UpdateMybatisOpLogInterceptorProcessor
                INSTANCE = new UpdateMybatisOpLogInterceptorProcessor();
    }
}
