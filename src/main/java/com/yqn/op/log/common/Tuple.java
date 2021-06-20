package com.yqn.op.log.common;

/**
 * @author huayuanlin
 * @date 2021/06/18 10:22
 * @desc the Tuple class
 */
public class Tuple<T,R> {

    private T t;
    private R r;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public R getR() {
        return r;
    }

    public void setR(R r) {
        this.r = r;
    }
}
