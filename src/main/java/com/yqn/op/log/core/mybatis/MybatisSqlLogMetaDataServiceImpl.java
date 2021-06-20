package com.yqn.op.log.core.mybatis;

import com.yqn.op.log.core.BaseSqlLogMetaDataService;
import com.yqn.op.log.core.ISqlLogMetaDataService;
import com.yqn.op.log.core.OpLogMetaDataDO;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/06/20 20:50
 * @desc the class desc
 */
public class MybatisSqlLogMetaDataServiceImpl extends BaseSqlLogMetaDataService {

    @Override
    public void insert(OpLogMetaDataDO opLogMetaDataDO) {

    }

    @Override
    public List<OpLogMetaDataDO> listNotProcessor() {
        return null;
    }

    @Override
    public void update(OpLogMetaDataDO opLogMetaDataDO) {

    }
}
