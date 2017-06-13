package com.jihuangdev.framework;

import com.jihuangdev.framework.bean.Data;
import com.jihuangdev.framework.bean.Handler;
import com.jihuangdev.framework.bean.Param;
import com.jihuangdev.framework.bean.View;
import com.jihuangdev.framework.helper.*;
import com.jihuangdev.framework.util.JsonUtil;
import com.jihuangdev.framework.util.ReflectionUtil;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by wancan on 2016/5/21.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();
        ServletContext servletContext = config.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssertPath() + "*");
        UploadHelper.init(servletContext);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws IOException,ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        ServletHelper.init(request,response);
        try {
            String requestMethod = request.getMethod().toLowerCase();
            String requestPath = request.getPathInfo();

            if (requestPath.equals("/favicon.ico")) {
                return;
            }

            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null) {
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);

                //优化支持参数为null
                Param param = null;
                if (UploadHelper.isMultipart(request)) {
                    try {
                        param = UploadHelper.createParam(request);
                    } catch (FileUploadException e) {
                        new RuntimeException("file upload exption", e);
                    }
                } else {
                    param = RequestHelper.createParam(request);
                }

                Method actionMethod = handler.getActionMethod();
                Object result;
                if (param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                }

                // 方法调用结果为View时
                if (result instanceof View) {
                    View view = (View) result;
                    String path = view.getPath();
                    if (StringUtils.isNotEmpty(path)) {
                        if (path.startsWith("/")) {
                            response.sendRedirect(request.getContextPath() + path);
                        } else {
                            Map<String, Object> model = view.getModel();
                            for (Map.Entry<String, Object> entry : model.entrySet()) {
                                request.setAttribute(entry.getKey(), entry.getValue());
                            }
                            request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
                        }
                    }
                } else if (result instanceof Data) {
                    Data data = (Data) result;
                    Object model = data.getModel();
                    if (model != null) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");
                        PrintWriter pw = response.getWriter();
                        String json = JsonUtil.toJson(model);
                        pw.write(json);
                        pw.flush();
                        pw.close();
                    }
                }

            }
        }finally {
            ServletHelper.destroy();
        }
    }
}
