package com.jihuangdev.framework.helper;

import com.jihuangdev.framework.bean.FileParam;
import com.jihuangdev.framework.bean.FormParam;
import com.jihuangdev.framework.bean.Param;
import com.jihuangdev.framework.util.FileUtil;
import com.jihuangdev.framework.util.StreamUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/31.
 */
public final class UploadHelper {
    private static ServletFileUpload servletFileUpload;

    public static void init(ServletContext servletContext){
        File respository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,respository));

        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if (uploadLimit != 0) {
            servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
        }
    }

    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    public static Param createParam(HttpServletRequest request) throws FileUploadException, IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();

        Map<String,List<FileItem>> fileItemMap = servletFileUpload.parseParameterMap(request);
        if (!fileItemMap.isEmpty()) {
            for (Map.Entry<String,List<FileItem>> entry : fileItemMap.entrySet()) {
                String fieldName = entry.getKey();
                List<FileItem> fileItemList = entry.getValue();
                if (CollectionUtils.isNotEmpty(fileItemList)) {
                    for (FileItem fileItem : fileItemList) {
                        if (fileItem.isFormField()) {
                            String fieldValue = fileItem.getString("utf-8");
                            formParamList.add(new FormParam(fieldName,fieldValue));
                        }else {
                            String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes("utf-8")));
                            if (StringUtils.isNotEmpty(fileName)) {
                                long fileSize = fileItem.getSize();
                                String contentType = fileItem.getContentType();
                                InputStream inputStream = fileItem.getInputStream();
                                fileParamList.add(new FileParam(fieldName,fileName,fileSize,contentType,inputStream));
                            }
                        }
                    }
                }
            }
        }

        return new Param(formParamList,fileParamList);
    }

    public static void uploadFile(String basePath, FileParam fileParam) {
        try {
            if (fileParam != null) {
                String filePath = basePath+fileParam.getFileName();
                FileUtil.createFile(filePath);
                InputStream inputStream = fileParam.getInputStream();
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream,outputStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(String basePath, List<FileParam> fileParams) {
        if (CollectionUtils.isNotEmpty(fileParams)) {
            for (FileParam fileParam : fileParams) {
                uploadFile(basePath, fileParam);
            }
        }
    }
}
