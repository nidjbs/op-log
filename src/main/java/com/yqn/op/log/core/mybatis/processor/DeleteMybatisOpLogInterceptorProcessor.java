package com.yqn.op.log.core.mybatis.processor;

import com.yqn.op.log.common.OpLogConstant;
import com.yqn.op.log.core.mybatis.MybatisInvocationWrapper;
import com.yqn.op.log.core.mybatis.MybatisOpLogInterceptorProcessor;
import com.yqn.op.log.core.mybatis.MybatisSqlMetaData;
import com.yqn.op.log.util.MapsUtil;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author huayuanlin
 * @date 2021/06/17 14:18
 * @desc the delete MybatisOpLogInterceptorProcessor
 */
public class DeleteMybatisOpLogInterceptorProcessor extends MybatisOpLogInterceptorProcessor {

    private DeleteMybatisOpLogInterceptorProcessor() {

    }


    @Override
    public List<Map<String, Object>> getBeforeDataList(MybatisInvocationWrapper invocationWrapper) {
        MybatisSqlMetaData sqlMetaDataByContext = getSqlMetaDataByContext();
        String formatToSelectSqlParam = formatToSelectSqlParam(sqlMetaDataByContext.getSql());
        Object[] args = invocationWrapper.getInvocation().getArgs();
        // get connection
        Connection connection = (Connection) args[0];
        String selectSql = String.format(OpLogConstant.SELECT_SQL_FORMAT, "*",
                sqlMetaDataByContext.getTableName(), formatToSelectSqlParam);
        BoundSql boundSql = invocationWrapper.getBoundSql();
        Configuration configuration = invocationWrapper.getMappedStatement().getConfiguration();
        List<Object> params = getAllSqlParams(boundSql, configuration);
        return doSelect(connection, selectSql, params);
    }

    /**
     * format to select sql params sql
     *
     * @param deleteSql delete sql
     * @return select sql params sql
     */
    private String formatToSelectSqlParam(String deleteSql) {
        String toUpperCase = deleteSql.toUpperCase();
        return toUpperCase.substring(toUpperCase.lastIndexOf("WHERE") + 1);
    }


    public static DeleteMybatisOpLogInterceptorProcessor getInstance() {
        return DeleteMybatisOpLogInterceptorProcessor.Holder.INSTANCE;
    }

    private static class Holder {
        private static final DeleteMybatisOpLogInterceptorProcessor
                INSTANCE = new DeleteMybatisOpLogInterceptorProcessor();
    }
}
