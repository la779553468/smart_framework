package com.jihuangdev.framework;

import com.jihuangdev.framework.helper.*;
import com.jihuangdev.framework.util.ClassUtil;

/**
 * Created by wancan on 2016/5/21.
 * * bean容器初始化步骤
 * 1.通过当前线程的类加载器，获取指定包下的所有URL类型的枚举集合资源
 * 2.遍历此枚举集合，通过文件过滤器的方式获取所有的class文件和目录类型的文件，然后通过递归方式遍历出所有class文件，用Class.forName()装载class文件，
 *   将注解类型为controller和service的class放入set集合中
 * 3.遍历set集合，将其类名与初始化的类实例增加到beanMap中，通过此beanMap获取类实例
 * 4.初始化Aop插件，将类对应的代理对象映射到beanMap的value中
 *   4.1.获取所有继承自AspectProxy抽象类的子类，判断其是否存在Aspect注解，存在则根据aspect.value()获取要切入的注解类，根据注解获取添加了该注解的class集合，将这个子类
 *       的Class与注解获得的class集合映射到proxyMap；事务注解需单独映射Service注解的class集合并加入到proxyMap
 *   4.2.将proxyMap转换成目标类与代理对象列表的映射targetMap
 *   4.3.通过cglib的Enhancer.create()创建代理类，创建时增加MethodInterceptor；这样在方法调用时，通过这个方法拦截器,通过创建代理时传入的代理列表，
 *       形成一个代理链执行切面操作，最后调用代理类的父类（目标类）方法，返回执行结果
 *   4.4.根据targetMap,更新beanMap集合
 * 5.初始化注入，获取beanMap中key的类的属性对象，判断其是否存在injection注解，若存在，将field设置为可访问，将属性对应的实例注入到beanMap中该class对应的实例中
 * 6.Action关系映射
 *
 */
public class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class,
        };
        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName(), true);
        }
    }
}
