package com.jihuangdev.framework.helper;

import org.slf4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/6/5.
 */
public class ServletHelper {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ServletHelper.class);

    private static final ThreadLocal<ServletHelper> SERVLET_HELPER_THREAD_LOCAL = new ThreadLocal<ServletHelper>();

    private HttpServletRequest request;
    private HttpServletResponse response;

    public ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static void init(HttpServletRequest request, HttpServletResponse response) {
        SERVLET_HELPER_THREAD_LOCAL.set(new ServletHelper(request,response));
    }

    public static void destroy(){
        SERVLET_HELPER_THREAD_LOCAL.remove();
    }

    public static HttpServletRequest getRequest() {
        return SERVLET_HELPER_THREAD_LOCAL.get().request;
    }

    public static HttpServletResponse getResponse() {
        return SERVLET_HELPER_THREAD_LOCAL.get().response;
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

    public static void set(String key, Object value){
        getRequest().setAttribute(key,value);
    }

    public static <T> T get(String key) {
        return (T) getRequest().getAttribute(key);
    }

    public static void remove(String key) {
        getRequest().removeAttribute(key);
    }



}
