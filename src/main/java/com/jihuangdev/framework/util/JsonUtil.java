package com.jihuangdev.framework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wancan on 2016/5/21.
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String toJson(T obj) {
        String json = null;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("convert pojo to json fail", e);
        }
        return json;
    }

    public static <T> T fromJson(String json, Class<T> type) {
        T pojo = null;
        try {
            pojo = OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            logger.error("convert json to pojo fail", e);
        }
        return pojo;
    }
}
