package com.jihuangdev.framework.helper;

import com.jihuangdev.framework.annotation.Aspect;
import com.jihuangdev.framework.annotation.Service;
import com.jihuangdev.proxy.AspectProxy;
import com.jihuangdev.proxy.Proxy;
import com.jihuangdev.proxy.ProxyManager;
import com.jihuangdev.proxy.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by wancan on 2016/9/7.
 * 初始化步骤
 *
 */
public class AopHelper {

    private static final Logger logger = LoggerFactory.getLogger(AopHelper.class);
    private static Map<Class<?>, List<Proxy>> targetMap;  //目标类与代理列表的映射
    static {
        try {
            // key:切面(也可叫代理)类（例如ControllerAspect）   value: 拦截的类
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            /*转换为被拦截类与切面对面映射*/
            targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> entry : targetMap.entrySet()) {
                Class<?> targetClass = entry.getKey();
                List<Proxy> proxyList = entry.getValue();
                /*生成代理类*/
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                /*更新bean容器中对象*/
                BeanHelper.setBean(targetClass, proxy);  //将代理对象设置到bean map中
            }
        } catch (Exception e) {
            logger.error("aop fail", e);
        }
    }

    //返回代理对象
    public static Object getTargetProxy(Class targetClass) {
        List<Proxy> proxyList = targetMap.get(targetClass);
        if(proxyList == null) {
            return null;
        }
        return ProxyManager.createProxy(targetClass,proxyList);
    }

    //获取使用了aspect.value() 注解的类集合
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        // 获取aspect的value值（一个注解，如Controller,Service）
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            //获取所有使用了该注解的值
            targetClassSet.addAll(ClassHelper.getClassByAnnotation(annotation));
        }
        return targetClassSet;
    }

    //获取所有 key为继承自AspectProxy类的子类，value为需要拦截的类集合
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();

        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        Set<Class<?>> serviceSet = ClassHelper.getClassByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class,serviceSet);
    }

    /**
     * 返回代理拦截类 ? extends AspectProxy
     * @param proxyMap
     * @throws Exception
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        //获取所有AspectProxy的子类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            //类使用了注解Aspect
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>, Set<Class<?>>> entry : proxyMap.entrySet()) {
            Class<?> proxyClass = entry.getKey();
            Set<Class<?>> targetClassSet = entry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }
}
