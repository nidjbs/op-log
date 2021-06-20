package com.yqn.op.log.core.mybatis;

import com.yqn.op.log.common.CallBackWithReturn;
import com.yqn.op.log.common.FrameworkException;
import com.yqn.op.log.common.Tuple;
import com.yqn.op.log.config.OpLogConfig;
import com.yqn.op.log.core.OpLogContext;
import com.yqn.op.log.core.OpLogContextProvider;
import com.yqn.op.log.core.OpLogInterceptorProcessor;
import com.yqn.op.log.core.SqlMetaData;
import com.yqn.op.log.core.mybatis.processor.UpdateMybatisOpLogInterceptorProcessor;
import com.yqn.op.log.enums.SqlType;
import com.yqn.op.log.util.*;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huayuanlin
 * @date 2021/06/14 20:57
 * @desc the mybatis op log interceptor processor
 */
public class MybatisOpLogInterceptorProcessor implements OpLogInterceptorProcessor<Invocation>, CallBackWithReturn<Invocation, Object, SqlMetaData> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MybatisOpLogInterceptorProcessor.class);

    private static final String DELEGATE_MAPPED_STATEMENT = "delegate.mappedStatement";
    private static final String DELEGATE_BOUND_SQL = "delegate.boundSql";

    private static final Map<SqlType, Function<String, String>> PARSE_TABLE_NAME_FUN = new HashMap<>(16);

    static {
        PARSE_TABLE_NAME_FUN.put(SqlType.INSERT, sql -> getTableName(sql, "INSERT INTO "));
        PARSE_TABLE_NAME_FUN.put(SqlType.UPDATE, sql -> getTableName(sql, "UPDATE "));
        PARSE_TABLE_NAME_FUN.put(SqlType.DELETE, sql -> getTableName(sql, "DELETE FROM "));
        PARSE_TABLE_NAME_FUN.put(SqlType.SOFT_DELETE, sql -> getTableName(sql, "UPDATE "));
        PARSE_TABLE_NAME_FUN.put(SqlType.UNKNOWN, sql -> "");
    }


    public static MybatisOpLogInterceptorProcessor getInstance() {
        return MybatisOpLogInterceptorProcessor.Holder.INSTANCE;
    }

    private static class Holder {
        private static final MybatisOpLogInterceptorProcessor INSTANCE = new MybatisOpLogInterceptorProcessor();
    }

    /**
     * get table name
     *
     * @param sql sql
     * @param key subString key
     * @return table name
     */
    private static String getTableName(String sql, String key) {
        String toUpperCase = sql.toUpperCase();
        String format = toUpperCase.replaceAll("[\\s\\t\\n\\r]", " ").replaceAll("\\s+", " ");
        format = format.substring(toUpperCase.indexOf(key) + key.length());
        return format.substring(0, format.indexOf(" ")).replaceAll("`", "").replace("(", "").replace(")", "");
    }

    public static void main(String[] args) {
        String sql = "insert into sy_order()\n" +
                "         SET desception = ?,\n" +
                "            \n" +
                "            \n" +
                "                order_no = ?\n" +
                "            \n" +
                "            \n" +
                "                order_date = ?\n" +
                "            \n" +
                "            \n" +
                "                state = ? \n" +
                "        where id = ?";
        String apply = PARSE_TABLE_NAME_FUN.get(SqlType.INSERT).apply(sql);
        System.out.println(apply);
    }

    @Override
    public final Tuple<SqlMetaData, Object> parseMetaData(Invocation invocation) {
        // parse
        StatementHandler statementHandler = PluginUtil.getTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(DELEGATE_MAPPED_STATEMENT);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        MybatisSqlMetaData sqlMetaData = new MybatisSqlMetaData();
        List<SqlMetaData> sqlMetaDataList = OpLogContextProvider.opLogContext().getSqlMetaDataList();
        sqlMetaDataList.add(sqlMetaData);
        SqlType sqlType = sqlTypeConvert(sqlCommandType);
        sqlMetaData.setSqlType(sqlType);
        // not support sql type
        if (sqlType == SqlType.UNKNOWN) {
            LOGGER.warn("unknown sql type!");
            Tuple<SqlMetaData, Object> sqlMetaDataObjectTuple = process(invocation);
            sqlMetaDataObjectTuple.setT(sqlMetaData);
            return sqlMetaDataObjectTuple;
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue(DELEGATE_BOUND_SQL);
        Configuration configuration = mappedStatement.getConfiguration();
        if (sqlType == SqlType.UPDATE) {
            if (isSoftDelete(boundSql, configuration)) {
                sqlMetaData.setSqlType(SqlType.SOFT_DELETE);
            }
        }
        String sql = boundSql.getSql();
        sqlMetaData.setSql(sql);
        sqlMetaData.setTableName(PARSE_TABLE_NAME_FUN.get(sqlType).apply(sql));
        OpLogConfig opLogConfig = SpringBeanUtil.getBeanByType(OpLogConfig.class);
        boolean isNeedLog = opLogConfig.getLogTables().contains(sqlMetaData.getTableName().toLowerCase());
        sqlMetaData.setNeedLog(isNeedLog);
        // need log
        if (!isNeedLog) {
            Tuple<SqlMetaData, Object> sqlMetaDataObjectTuple = process(invocation);
            sqlMetaDataObjectTuple.setT(sqlMetaData);
            return sqlMetaDataObjectTuple;
        }
        MybatisOpLogInterceptorProcessor mybatisOpLogInterceptorProcessor = OpLogInterceptorProcessorProvider
                .mybatisOpLogInterceptorProcessor(sqlType);
        AssertUtil.notNull(mybatisOpLogInterceptorProcessor, "mybatisOpLogInterceptorProcessor");
        MybatisInvocationWrapper invocationWrapper = new MybatisInvocationWrapper();
        invocationWrapper.setBoundSql(boundSql);
        invocationWrapper.setInvocation(invocation);
        invocationWrapper.setMappedStatement(mappedStatement);
        // before data
        Map<String, Object> beforeData = mybatisOpLogInterceptorProcessor.getBeforeData(invocationWrapper);
        Tuple<SqlMetaData, Object> process = process(invocation);
        // after data
        Map<String, Object> afterData = mybatisOpLogInterceptorProcessor.getAfterData(invocationWrapper);
        sqlMetaData.setBeforeData(beforeData);
        sqlMetaData.setAfterData(afterData);
        process.setT(sqlMetaData);
        return process;
    }


    /**
     * process
     *
     * @param invocation invocation param
     * @return tuple result
     */
    @Override
    public Tuple<SqlMetaData, Object> process(Invocation invocation) {
        return call(e -> {
            try {
                return e.proceed();
            } catch (Exception exx) {
                throw new FrameworkException(exx);
            }
        }, invocation);
    }

    /**
     * get sql meta data by context
     *
     * @return meta data
     */
    protected MybatisSqlMetaData getSqlMetaDataByContext() {
        OpLogContext opLogContext = OpLogContextProvider.opLogContext();
        SqlMetaData sqlMetaData = opLogContext.getSqlMetaDataList().get(opLogContext.getCurSqlCount());
        AssertUtil.notNull(sqlMetaData, "sqlMetaData");
        return (MybatisSqlMetaData) sqlMetaData;
    }


    /**
     * get sql params value
     *
     * @param boundSql      boundSql
     * @param configuration configuration
     * @return params value list
     */
    protected List<Object> getAllSqlParams(BoundSql boundSql, Configuration configuration) {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        return parameterMappings.stream().map(parameterMapping -> {
            Object value;
            String propertyName = parameterMapping.getProperty();
            if (boundSql.hasAdditionalParameter(propertyName)) {
                value = boundSql.getAdditionalParameter(propertyName);
            } else if (parameterObject == null) {
                value = null;
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                value = metaObject.getValue(propertyName);
            }
            return value;
        }).collect(Collectors.toList());
    }

    /**
     * do select
     *
     * @param connection jdbc connection
     * @param sql        prepare sql
     * @param params     param values list
     * @return do select result
     */
    protected Map<String, Object> doSelect(Connection connection, String sql, List<Object> params) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                Map<String, Object> map = new LinkedHashMap<>(columnCount);
                if (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        // columnName
                        String columnName = resultSetMetaData.getColumnName(i);
                        int columnType = resultSetMetaData.getColumnType(i);
                        // column value
                        Object value = getColumnValue(resultSet, i, columnType);
                        map.put(columnName, value);
                    }
                }
                return map;
            }
        } catch (Exception e) {
            throw new FrameworkException("get delete before log error", e);
        }
    }

    /**
     * get column value
     *
     * @param resultSet  resultSet
     * @param i          result index
     * @param columnType columnType
     * @return column value
     * @throws SQLException exception
     */
    protected Object getColumnValue(ResultSet resultSet, int i, int columnType) throws SQLException {
        Object value;
        // process time type
        if (Types.DATE == columnType) {
            java.sql.Date date = resultSet.getDate(i);
            value = date == null ? null : new java.util.Date(date.getTime());
        } else if (Types.TIME == columnType || Types.TIME_WITH_TIMEZONE == columnType) {
            Time time = resultSet.getTime(i);
            value = time == null ? null : new java.util.Date(time.getTime());
        } else if (Types.TIMESTAMP == columnType || Types.TIMESTAMP_WITH_TIMEZONE == columnType) {
            Timestamp timestamp = resultSet.getTimestamp(i);
            value = timestamp == null ? null : new java.util.Date(timestamp.getTime());
        } else {
            value = resultSet.getObject(i);
        }
        return value;
    }

    /**
     * get before data
     *
     * @param invocationWrapper wrapper params
     * @return before data
     */
    protected Map<String, Object> getBeforeData(MybatisInvocationWrapper invocationWrapper) {
        return MapsUtil.hashMap();
    }

    /**
     * get after data
     *
     * @param invocationWrapper wrapper params
     * @return after data
     */
    protected Map<String, Object> getAfterData(MybatisInvocationWrapper invocationWrapper) {
        return MapsUtil.hashMap();
    }


    /**
     * Sql type convert
     */
    private SqlType sqlTypeConvert(SqlCommandType sqlCommandType) {
        // insert into ..  update ...  delete
        switch (sqlCommandType) {
            case INSERT:
                return SqlType.INSERT;
            case DELETE:
                return SqlType.DELETE;
            case UPDATE:
                return SqlType.UPDATE;
            default:
                return SqlType.UNKNOWN;
        }
    }

    /**
     * check is soft delete by config
     *
     * @param boundSql      the boundSql
     * @param configuration the session configuration
     * @return result
     */
    private boolean isSoftDelete(BoundSql boundSql, Configuration configuration) {
        String sql = boundSql.getSql();
        OpLogConfig opLogConfig = SpringBeanUtil.getBeanByType(OpLogConfig.class);
        AssertUtil.notNull(opLogConfig, "opLogConfig");
        String softDeleteColumn = opLogConfig.getSoftDeleteColumn();
        String softDeleteColumnDeleteValue = opLogConfig.getSoftDeleteColumnDeleteValue();
        boolean hasConfig = StringUtil.isEmpty(softDeleteColumn) || StringUtil.isEmpty(softDeleteColumnDeleteValue);
        if (hasConfig) {
            return false;
        }
        String formatSql = sql.replaceAll("[\\s\\t\\n\\r]", "").replaceAll(" ", "");
        List<String> checkSoftDeleteStrList = getCheckSoftDeleteStrList(softDeleteColumn, softDeleteColumnDeleteValue);
        boolean anyMatch = checkSoftDeleteStrList.stream().anyMatch(formatSql::contains);
        // when the sql contains the config deleteColumn=deleteColumnValue str,means is soft delete
        if (anyMatch) {
            return true;
        }
        return doCheckSoftDeleteBySqlParams(boundSql, configuration, softDeleteColumn, softDeleteColumnDeleteValue);
    }

    /**
     * get check soft delete str list
     *
     * @param column      column
     * @param columnValue columnValue
     * @return list result
     */
    private List<String> getCheckSoftDeleteStrList(String column, String columnValue) {
        column = ConverterUtil.camelToUnderline(column);
        column = column.replaceAll(" ", "").toUpperCase();
        columnValue = columnValue.replaceAll(" ", "").toUpperCase();
        List<String> result = CollectionsUtil.arrayList();
        if ("TRUE".equals(columnValue) || "FALSE".equals(columnValue)) {
            result.add(column + "=" + columnValue);
            result.add(column + "=" + ("TRUE".equals(columnValue) ? 1 : 0));
        } else {
            result.add(column + "=" + columnValue);
        }
        return result;
    }

    /**
     * do check is soft delete
     * when the sql params values contains the config deleteColumn and is deleteValue means is soft delete
     */
    private boolean doCheckSoftDeleteBySqlParams(BoundSql boundSql, Configuration configuration,
                                                 String column, String columnValue) {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        return parameterMappings.stream().anyMatch(parameterMapping -> {
            String propertyName = parameterMapping.getProperty();
            String helperStr = propertyName;
            if (propertyName.contains(".")) {
                helperStr = propertyName.substring(propertyName.lastIndexOf('.') + 1);
            }
            if (helperStr.equals(column)) {
                Object value = metaObject.getValue(propertyName);
                if (value instanceof String) {
                    return columnValue.equals(value);
                } else if (value instanceof Integer) {
                    return value.equals(Integer.parseInt(columnValue));
                } else if (value instanceof Boolean) {
                    return Boolean.parseBoolean(columnValue) == (Boolean) value;
                } else {
                    return false;
                }
            }
            return false;
        });
    }
}