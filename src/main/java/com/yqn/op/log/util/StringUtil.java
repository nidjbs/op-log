package com.yqn.op.log.util;

/**
 * @author ： huayuanlin
 * @date ： 2021/5/23 16:59
 * @desc ： StringUtil
 */
public class StringUtil {

    private StringUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !"".equals(str);
    }

    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }
}
