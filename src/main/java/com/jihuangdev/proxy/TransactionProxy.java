package com.jihuangdev.proxy;

import com.jihuangdev.framework.annotation.Transaction;
import com.jihuangdev.framework.helper.DatabaseHelper;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/5/31.
 */
public class TransactionProxy implements Proxy {

    private  static ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if(!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAG_HOLDER.set(true);

            try {
                DatabaseHelper.beginTransaction();
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                DatabaseHelper.rollbackTransaction();
            } finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
