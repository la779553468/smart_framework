package com.jihuangdev.framework.annotation;

import java.lang.annotation.*;

/**
 * ������ע��
 * Created by wancan on 2016/5/21.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
