package com.yqn.op.log.core.mapping;

import com.yqn.op.log.core.OpLogContent;
import com.yqn.op.log.core.OpLogMetaDataDO;
import com.yqn.op.log.core.OpLogMetaDataWrapper;
import com.yqn.op.log.util.JsonUtil;
import com.yqn.op.log.common.ObjBuilder;

/**
 * @author huayuanlin
 * @date 2021/06/21 00:05
 * @desc the class desc
 */
public class DbLogOpLogMappingTask extends AbstractOpLogMappingTask<OpLogMetaDataDO> {


    protected DbLogOpLogMappingTask(OpLogMetaDataDO opLogMetaDataDO) {
        super(opLogMetaDataDO);
    }

    @Override
    public OpLogMetaDataWrapper convert() {
        OpLogMetaDataDO opLogMetaData = getT();
        String metaData = opLogMetaData.getMetaData();
        return ObjBuilder.create(OpLogMetaDataWrapper::new)
                .of(OpLogMetaDataWrapper::setId, opLogMetaData.getId())
                .of(OpLogMetaDataWrapper::setOpId, opLogMetaData.getOpId())
                .of(OpLogMetaDataWrapper::setBizId, opLogMetaData.getBizId())
                .of(OpLogMetaDataWrapper::setOpType, opLogMetaData.getOpType())
                .of(OpLogMetaDataWrapper::setTraceId, opLogMetaData.getTraceId())
                .of(OpLogMetaDataWrapper::setOpLogContents, JsonUtil.toArray(metaData, OpLogContent.class))
                .build();
    }
}
