package com.jihuangdev.framework.helper;

import com.jihuangdev.framework.annotation.Inject;
import com.jihuangdev.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by wancan on 2016/5/21.
 * 容器初始化步骤
 * 1.通过当前线程的类加载器，获取指定包下的所有URL类型的枚举集合资源
 * 2.遍历此枚举集合，通过文件过滤器的方式获取所有的class文件和目录类型的文件，通过递归方式遍历出所有class文件，通过Class.forName()装载class文件，
 *   将其注解类型为controller和service的class放入set集合中
 * 3.遍历class集合，将其类名与初始化的类实例增加到map中，通过此map获取类实例，若考虑切面编程，则类实例应为代理对象
 * 4.遍历class集合，获取类的属性对象，判断其是否存在injection注解，若存在，将field设置为可访问，将属性对应的实例注入到map中该class对应的实例中
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
