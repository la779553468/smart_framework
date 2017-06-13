package com.jihuangdev.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wancan on 2016/5/19.
 */
public class PropsUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

    public static Properties loadProps(String fileName) {
        Properties properties = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("load properties file failure", e);
        } finally {
            if (is == null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("close input stream failure", e);
                }
            }
        }
        return properties;
    }

    public static String getString(Properties properties, String key) {
        return getString(properties, key, "");
    }

    public static String getString(Properties properties, String key, String defoutValue) {
        String value = defoutValue;
        if (properties.containsKey(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }
}
