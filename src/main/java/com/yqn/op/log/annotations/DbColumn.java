package com.yqn.op.log.annotations;

import java.lang.annotation.*;

/**
 * @author huayuanlin
 * @date 2021/06/15 22:32
 * @desc the annotation desc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface DbColumn {

    /**
     * this field mapping db column name,when db column name different from the field.
     */
    String value();

}
