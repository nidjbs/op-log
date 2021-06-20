package com.yqn.op.log.annotations;

import com.yqn.op.log.core.OpLogBiz;

import java.lang.annotation.*;

/**
 * @author huayuanlin
 * @date 2021/06/07 23:27
 * @desc the annotation desc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface OpLog {

    String bizDesc();

}
