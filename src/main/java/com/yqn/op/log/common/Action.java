package com.yqn.op.log.common;

/**
 * @author huayuanlin
 * @date 2021-05-11 09:59:47
 * @desc 无参无返回值函数式接口
 */
@FunctionalInterface
public interface Action {

    /**
     * action
     */
    void apply();
}
