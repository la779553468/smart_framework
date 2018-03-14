package com.jihuangdev.framework.helper;

import com.jihuangdev.framework.annotation.Controller;
import com.jihuangdev.framework.annotation.Service;
import com.jihuangdev.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wancan on 2016/5/21.
 */
public class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Service.class)) {
                set.add(clazz);
            }
        }
        return set;
    }

    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                set.add(clazz);
            }
        }
        return set;
    }

    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.addAll(getControllerClassSet());
        set.addAll(getServiceClassSet());
        return set;
    }

    /**
     *  获取父类（接口）下所有子类（实现类）
     *
     * @param superClass
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class cls : CLASS_SET) {
            //isAssignableFrom 判断判定此 Class 对象所表示的类或接口与指定的cls参数所表示的类或接口是否相同，或是否是其超类或超接口，class
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     *  获取带有指定注解的类
     *
     * @return
     */
    public static Set<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class cls : CLASS_SET) {
            if (cls.isAnnotationPresent(annotationClass)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

}
