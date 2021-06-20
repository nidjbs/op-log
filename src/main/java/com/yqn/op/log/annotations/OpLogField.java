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
public @interface OpLogField {

    /**
     * log field desc
     */
    String value();

    /**
     * db alias，default Camel-Case convert
     */
    String fieldName() default "";

}
