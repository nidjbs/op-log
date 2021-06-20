package com.yqn.op.log.core;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/06/20 19:48
 * @desc the interface desc
 */
public interface ISqlLogMetaDataService {

    /**
     * covert to do log
     *
     * @return log do
     */
    OpLogMetaDataDO doConvert();

    /**
     * insert
     *
     * @param opLogMetaDataDO log do
     */
    void insert(OpLogMetaDataDO opLogMetaDataDO);

    /**
     * list not processor log
     *
     * @return list log
     */
    List<OpLogMetaDataDO> listNotProcessor();

    /**
     * update log
     *
     * @param opLogMetaDataDO do log
     */
    void update(OpLogMetaDataDO opLogMetaDataDO);

}
