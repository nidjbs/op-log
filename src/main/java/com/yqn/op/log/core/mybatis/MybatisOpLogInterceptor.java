package com.yqn.op.log.core.mybatis;

import com.yqn.op.log.common.Tuple;
import com.yqn.op.log.core.*;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author huayuanlin
 * @date 2021/06/10 20:18
 * @desc the class desc
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MybatisOpLogInterceptor implements Interceptor, ISqlOpLogInterceptor {


    @Override
    public Object intercept(Invocation invocation) {
        // parse
        MybatisOpLogInterceptorProcessor instance = MybatisOpLogInterceptorProcessor.getInstance();
        Tuple<SqlMetaData, Object> sqlMetaDataObjectTuple = instance.parseMetaData(invocation);
        Object r = sqlMetaDataObjectTuple.getR();
        SqlMetaData sqlMetaData = sqlMetaDataObjectTuple.getT();
        OpLogContext opLogContext = OpLogContextProvider.opLogContext();
        // is need log
        if (!sqlMetaData.isNeedLog()) {
            opLogContext.getSqlMetaDataList().remove(opLogContext.getCurSqlCount());
            return r;
        }
        opLogContext.setCurSqlCount(opLogContext.getCurSqlCount() + 1);
        return r;
    }

    @Override
    public Object plugin(Object target) {
        if (!(target instanceof StatementHandler)) {
            return target;
        }
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
