package com.jihuangdev.framework;

import com.jihuangdev.framework.helper.*;
import com.jihuangdev.framework.util.ClassUtil;

/**
 * Created by wancan on 2016/5/21.
 */
public class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class,
                AopHelper.class
        };
        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName(), true);
        }
    }
}
