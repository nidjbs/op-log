package com.yqn.op.log.core.mapping;

import com.yqn.op.log.core.OpLogMetaDataDO;
import com.yqn.op.log.core.OpLogMetaDataWrapper;

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



        return null;
    }
}
