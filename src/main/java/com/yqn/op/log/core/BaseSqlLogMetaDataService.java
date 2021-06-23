package com.yqn.op.log.core;

import com.yqn.op.log.enums.OpLogStatus;
import com.yqn.op.log.enums.OpLogType;
import com.yqn.op.log.enums.SqlType;
import com.yqn.op.log.util.JsonUtil;
import com.yqn.op.log.util.ObjBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huayuanlin
 * @date 2021/06/20 20:52
 * @desc the class desc
 */
public abstract class BaseSqlLogMetaDataService implements ISqlLogMetaDataService {

    @Override
    public OpLogMetaDataDO doConvert() {
        OpLogContext opLogContext = OpLogContextProvider.opLogContext();
        BizTrace bizTrace = opLogContext.getBizTrace();
        List<OpLogContent> opLogContents = opLogContext.getSqlMetaDataList()
                .stream().map(this::logContentConvert).collect(Collectors.toList());
        return ObjBuilder.create(OpLogMetaDataDO::new)
                .of(OpLogMetaDataDO::setBizDesc, bizTrace.getBizDesc())
                .of(OpLogMetaDataDO::setOpId, bizTrace.getBizOpId())
                .of(OpLogMetaDataDO::setStatus, OpLogStatus.INIT.getId())
                .of(OpLogMetaDataDO::setTraceId, bizTrace.getTraceId())
                .of(OpLogMetaDataDO::setMetaData, JsonUtil.toJsonString(opLogContents))
                .build();
    }

    /**
     * logContentConvert
     *
     * @param sqlMetaData sqlMetaData
     * @return content
     */
    private OpLogContent logContentConvert(SqlMetaData sqlMetaData) {
        return ObjBuilder.create(OpLogContent::new)
                .of(OpLogContent::setTableName, sqlMetaData.getTableName())
                .of(OpLogContent::setType, convertType(sqlMetaData.getSqlType()).getId())
                .of(OpLogContent::setBefore, sqlMetaData.getBeforeData())
                .of(OpLogContent::setAfter, sqlMetaData.getAfterData())
                .build();
    }


    /**
     *  convert sql type to op log type
     * @param sqlType sqlType
     * @return op log type
     */
    private OpLogType convertType(SqlType sqlType) {
        switch (sqlType) {
            case INSERT:
                return OpLogType.CREATE;
            case DELETE:
                return OpLogType.DELETE;
            default:
                return OpLogType.UPDATE;
        }
    }
}
