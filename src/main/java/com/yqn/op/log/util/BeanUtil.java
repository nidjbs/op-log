package com.yqn.op.log.util;

import org.springframework.cglib.beans.BeanMap;

import java.util.Map;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/07/04 15:24
 * @desc the class desc
 */
public class BeanUtil {

    private BeanUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * bean to map
     *
     * @param bean bean not null
     * @return map
     */
    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return MapsUtil.hashMap();
        }
        Map<String, Object> result = MapsUtil.hashMap();
        BeanMap beanMap = BeanMap.create(bean);
        for (Object key : beanMap.keySet()) {
            result.put((String) key, beanMap.get(key));
        }
        return result;
    }
}
