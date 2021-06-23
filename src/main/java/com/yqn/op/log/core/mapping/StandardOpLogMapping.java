package com.yqn.op.log.core.mapping;

import com.yqn.op.log.core.ISqlLogMetaDataService;
import com.yqn.op.log.core.OpLogMetaDataWrapper;
import com.yqn.op.log.util.SpringBeanUtil;

/**
 * @author huayuanlin
 * @date 2021/06/21 00:32
 * @desc the class desc
 */
public class StandardOpLogMapping implements IOpLogMapping {


    @Override
    public void process(OpLogMetaDataWrapper opLogMetaDataWrapper) {
        ISqlLogMetaDataService service = SpringBeanUtil.getBeanByType(ISqlLogMetaDataService.class);
        boolean isNeedProcess = service.updateStatusToProcessing(opLogMetaDataWrapper.getId());
        if (!isNeedProcess) {
            return;
        }
        // todo process
        
    }

    public static StandardOpLogMapping getInstance() {
        return StandardOpLogMapping.Holder.INSTANCE;
    }

    private static class Holder {
        private static final StandardOpLogMapping INSTANCE = new StandardOpLogMapping();
    }

}
