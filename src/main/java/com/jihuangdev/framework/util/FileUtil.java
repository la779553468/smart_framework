package com.jihuangdev.framework.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/31.
 */
public class FileUtil {

    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName);
    }

    public static File createFile(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                FileUtils.forceMkdir(parentFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
