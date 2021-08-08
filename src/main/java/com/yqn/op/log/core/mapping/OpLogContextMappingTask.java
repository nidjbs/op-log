package com.yqn.op.log.core.mapping;

import com.yqn.op.log.common.OpLogConstant;
import com.yqn.op.log.core.*;
import com.yqn.op.log.common.ObjBuilder;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huayuanlin
 * @date 2021/06/21 00:10
 * @desc the class desc
 */
public class OpLogContextMappingTask extends AbstractOpLogMappingTask<OpLogContext> {

    /*** convert fun */
    private static final Function<SqlMetaData, OpLogContent> CONVERT_FUN = sqlMetaData -> {
        OpLogContent opLogContent = new OpLogContent();
        opLogContent.setTableName(sqlMetaData.getTableName());
        opLogContent.setType(OpLogConstant.SQL_TYPE_CONVERT_FUN.apply(sqlMetaData.getSqlType()).getId());
        opLogContent.setAfter(sqlMetaData.getAfterData());
        opLogContent.setBefore(sqlMetaData.getBeforeData());
        return opLogContent;
    };

    public OpLogContextMappingTask(OpLogContext opLogContext) {
        super(opLogContext);
    }

    @Override
    public OpLogMetaDataWrapper convert() {
        OpLogContext opLogContext = getT();
        BizTrace bizTrace = opLogContext.getBizTrace();
        Long opLogId = opLogContext.getOpLogId();
        List<OpLogContent> opLogContents = opLogContext.getSqlMetaDataList()
                .stream().map(CONVERT_FUN).collect(Collectors.toList());
        return ObjBuilder.create(OpLogMetaDataWrapper::new)
                .of(OpLogMetaDataWrapper::setOpId, bizTrace.getBizOpId())
                .of(OpLogMetaDataWrapper::setTraceId, bizTrace.getTraceId())
                .of(OpLogMetaDataWrapper::setBizDesc, bizTrace.getBizDesc())
                .of(OpLogMetaDataWrapper::setId, opLogId)
                .of(OpLogMetaDataWrapper::setOpLogContents, opLogContents)
                .build();
    }
}
