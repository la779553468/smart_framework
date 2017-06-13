package com.jihuangdev.framework.helper;

import com.jihuangdev.framework.bean.FormParam;
import com.jihuangdev.framework.bean.Param;
import com.jihuangdev.framework.util.CodeUtil;
import com.jihuangdev.framework.util.StreamUtil;
import com.jihuangdev.framework.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
public final class RequestHelper {
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        formParamList.addAll(parseParameterNames(request));
        formParamList.addAll(parseInputStream(request));
        return new Param(formParamList);
    }

    private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException {
        List<FormParam> formParams = new ArrayList<FormParam>();
        String body = CodeUtil.decodeUrl(StreamUtil.getString(request.getInputStream()));
        if (StringUtils.isNotEmpty(body)) {
            String[] kvs = body.split("&");
            if (ArrayUtils.isNotEmpty(kvs)) {
                for(String kv : kvs) {
                    String[] array = kv.split("=");
                    if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        formParams.add(new FormParam(fieldName,fieldValue));
                    }
                }
            }
        }
        return formParams;
    }

    private static List<FormParam> parseParameterNames(HttpServletRequest request) {
        List<FormParam> formParams = new ArrayList<FormParam>();
        Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);
            if (ArrayUtils.isNotEmpty(fieldValues)) {
                String fieldValue;
                if (fieldValues.length == 1) {
                    fieldValue = fieldValues[0];
                }else {
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i <fieldValues.length; i++) {
                        sb.append(fieldValues[i]);
                        if (i != fieldValues.length-1) {
                            sb.append(StringUtil.SEPARATOR);
                        }
                    }
                    fieldValue = sb.toString();
                }
                formParams.add(new FormParam(fieldName,  fieldValue));
            }
        }
        return formParams;
    }
}
