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

    /**
     * parse str,support Integer Long Sting
     *
     * @param obj obj param
     * @return str
     */
    public static String parseStr(Object obj) {
        String str;
        if (obj instanceof String) {
            str = (String) obj;
        } else if (obj instanceof Long || obj instanceof Integer) {
            str = String.valueOf(obj);
        } else {
            str = null;
        }
        return str;
    }
}
