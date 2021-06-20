package com.yqn.op.log.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huayuanlin
 * @date 2021/06/04 17:48
 * @desc Non-intrusive builder
 */
public class ObjBuilder<T> {

    private Supplier<T> createInstance;
    
    private List<Consumer<T>> consumers;

    private ObjBuilder() {
        throw new UnsupportedOperationException();
    }

    /**
     * create builder instance
     *
     * @param constructor build
     * @param <T>         build type
     * @return builder instance
     */
    public static <T> ObjBuilder<T> create(Supplier<T> constructor) {
        ObjBuilder<T> instance = new ObjBuilder<>();
        instance.createInstance = constructor;
        instance.consumers = new ArrayList<>(8);
        return instance;
    }

    /**
     * set obj attributes
     *
     * @param consumer bi consumer
     * @param p        params
     * @param <P>      t
     * @return builder instance
     */
    public <P> ObjBuilder<T> of(BiConsumer<T, P> consumer, P p) {
        consumers.add(instance -> consumer.accept(instance, p));
        return this;
    }

    /**
     * build obj
     *
     * @return obj
     */
    public T build() {
        T t = createInstance.get();
        for (Consumer<T> consumer : consumers) {
            consumer.accept(t);
        }
        return t;
    }

}
