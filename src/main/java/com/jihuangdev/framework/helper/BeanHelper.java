package com.jihuangdev.framework.helper;

import com.jihuangdev.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wancan on 2016/5/21.
 */
public class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanSet = ClassHelper.getBeanClassSet();
        for (Class<?> clazz : beanSet) {
            Object obj = ReflectionUtil.newInstance(clazz);
            BEAN_MAP.put(clazz, obj);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (!BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("can not get bean by class");
        }
        return (T) BEAN_MAP.get(clazz);
    }

    public static void setBean(Class cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }
}
