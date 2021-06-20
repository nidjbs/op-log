package com.yqn.op.log.core.mapping;

import com.yqn.op.log.core.OpLogMetaDataWrapper;

/**
 * @author huayuanlin
 * @date 2021/06/21 00:32
 * @desc the class desc
 */
public class StandardOpLogMapping implements IOpLogMapping {


    @Override
    public void process(OpLogMetaDataWrapper opLogMetaDataWrapper) {

    }

    public static StandardOpLogMapping getInstance() {
        return StandardOpLogMapping.Holder.INSTANCE;
    }

    private static class Holder {
        private static final StandardOpLogMapping INSTANCE = new StandardOpLogMapping();
    }

}
