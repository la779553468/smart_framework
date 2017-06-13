package com.jihuangdev.proxy;

import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by wancan on 2016/9/7.
 */
public abstract class AspectProxy implements Proxy {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {
            if (intercept(cls, method, params)) {
                before(cls, method, params);
                result = proxyChain.doProxyChain();
                after(cls, method, params);
            }

        } catch (Exception e) {
            logger.error("Proxy error", e);
            error(cls, method, params, e);
        } finally {
            end();
        }

        return result;

    }

    public boolean intercept(Class cls, Method method, Object[] params) {
        return true;
    }

    public void before(Class cls, Method method, Object[] params) {
    }

    public void after(Class cls, Method method, Object[] params) {
    }

    public void end() {
    }

    public void error(Class cls, Method method, Object[] params, Exception e) {
    }

    public void begin() {
    }


}
