package com.yqn.op.log.common;

/**
 * @author huayuanlin
 * @date 2021/06/18 10:02
 * @desc the class desc
 */
public interface CallBackWithReturn<P, R, T> {


    default Tuple<T, R> call(CallBack<P, R> callBack, P p, Tuple<T, R> tuple) {
        R call = callBack.call(p);
        tuple.setR(call);
        return tuple;
    }

    default Tuple<T, R> call(CallBack<P, R> callBack, P p) {
        Tuple<T, R> context = new Tuple<>();
        return call(callBack, p, context);
    }

    /**
     * process action
     *
     * @param p prams
     * @return the tuple result
     */
    Tuple<T, R> process(P p);


}
