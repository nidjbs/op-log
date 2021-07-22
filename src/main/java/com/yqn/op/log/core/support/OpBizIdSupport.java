package com.yqn.op.log.core.support;

import com.yqn.op.log.core.OpLogContext;
import com.yqn.op.log.core.OpLogContextProvider;
import com.yqn.op.log.util.AssertUtil;
import com.yqn.op.log.util.StringUtil;

/**
 * @author huayuanlin
 * @date 2021/07/03 18:15
 * @desc the class desc
 */
public class OpBizIdSupport {

    /**
     * set biz id for log op log
     *
     * @param bizId bizId, support Integer Long String
     */
    public static void setBizId(Object bizId) {
        AssertUtil.notNull(bizId, "bizId param");
        OpLogContext opLogContext = OpLogContextProvider.opLogContext();
        AssertUtil.notNull(opLogContext, "op log context");
        opLogContext.getBizTrace().setBizId(StringUtil.parseStr(bizId));
    }
}
