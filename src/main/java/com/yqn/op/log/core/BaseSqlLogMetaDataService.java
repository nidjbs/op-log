package com.yqn.op.log.core;

import com.yqn.op.log.common.OpLogConstant;
import com.yqn.op.log.enums.OpLogStatus;
import com.yqn.op.log.util.JsonUtil;
import com.yqn.op.log.util.ObjBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huayuanlin
 * @date 2021/06/20 20:52
 * @desc the class desc
 */
public abstract class BaseSqlLogMetaDataService implements ISqlLogMetaDataService, IDbInsertService<OpLogMetaDataDO> {

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
                .of(OpLogContent::setType, OpLogConstant.SQL_TYPE_CONVERT_FUN
                        .apply(sqlMetaData.getSqlType()).getId())
                .of(OpLogContent::setBefore, sqlMetaData.getBeforeData())
                .of(OpLogContent::setAfter, sqlMetaData.getAfterData())
                .build();
    }

    @Override
    public void insertBatch(List<OpLogMetaDataDO> list) {

    }
}
