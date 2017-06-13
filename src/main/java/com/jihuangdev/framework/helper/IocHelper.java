package com.jihuangdev.framework.helper;

import com.jihuangdev.framework.annotation.Inject;
import com.jihuangdev.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by wancan on 2016/5/21.
 */
public class IocHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (!beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
                Class<?> clazz = entry.getKey();
                Object instance = entry.getValue();
                Field[] fields = clazz.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Inject.class)) {
                            Class<?> fieldClass = field.getType();
                            Object fieldObj = beanMap.get(fieldClass);
                            if (fieldObj != null) {
                                ReflectionUtil.setField(instance, field, fieldObj);
                            }
                        }
                    }
                }
            }
        }
    }
}
