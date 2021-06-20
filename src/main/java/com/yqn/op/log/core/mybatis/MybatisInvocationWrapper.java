package com.yqn.op.log.core.mybatis;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;

/**
 * @author huayuanlin
 * @date 2021/06/16 20:13
 * @desc the class desc
 */
public class MybatisInvocationWrapper {

    private Invocation invocation;

    private MappedStatement mappedStatement;

    private BoundSql boundSql;

    private Object helper;

    public Object getHelper() {
        return helper;
    }

    public void setHelper(Object helper) {
        this.helper = helper;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }

    public MappedStatement getMappedStatement() {
        return mappedStatement;
    }

    public void setMappedStatement(MappedStatement mappedStatement) {
        this.mappedStatement = mappedStatement;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

    public void setBoundSql(BoundSql boundSql) {
        this.boundSql = boundSql;
    }
}
