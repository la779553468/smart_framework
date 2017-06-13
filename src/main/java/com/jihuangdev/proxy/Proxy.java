package com.jihuangdev.proxy;

/**
 * Created by wancan on 2016/9/7.
 */
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
