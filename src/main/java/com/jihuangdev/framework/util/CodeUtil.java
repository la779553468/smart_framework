package com.jihuangdev.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * Created by wancan on 2016/5/21.
 */
public class CodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(CodeUtil.class);

    public static String encodeUrl(String string) {
        String target = null;
        try {
            target = URLEncoder.encode(string, "utf-8");
        } catch (Exception e) {
            logger.error("encode URL fail", e);
        }
        return target;
    }

    public static String decodeUrl(String string) {
        String target = null;
        try {
            target = URLEncoder.encode(string, "utf-8");
        } catch (Exception e) {
            logger.error("decode url fail", e);
        }
        return target;
    }
}
