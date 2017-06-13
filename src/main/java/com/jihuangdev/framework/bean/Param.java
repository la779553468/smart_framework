package com.jihuangdev.framework.bean;

import com.jihuangdev.framework.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wancan on 2016/5/21.
 */
public class Param {
    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

    public Param(List<FormParam> formParamMap) {
        this.formParamList = formParamMap;
    }

    public Param(List<FormParam> formParamMap, List<FileParam> fileParamMap) {
        this.formParamList = formParamMap;
        this.fileParamList = fileParamMap;
    }

    public Map<String, Object> getFieldMap() {
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        if (CollectionUtils.isNotEmpty(formParamList)) {
            for (FormParam formParam : formParamList) {
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if (fieldMap.containsKey(fieldName)) {
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName,fieldValue);
            }
        }
        return fieldMap;
    }

    public Map<String, List<FileParam>> getFileMap() {
        Map<String,List<FileParam>> fileMap = new HashMap<String, List<FileParam>>();
        if (CollectionUtils.isNotEmpty(fileParamList)) {
            for (FileParam fileParam : fileParamList) {
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileParams;
                if (fileMap.containsKey(fieldName)) {
                    fileParams = fileMap.get(fieldName);
                }else {
                    fileParams = new ArrayList<FileParam>();
                }
                fileParams.add(fileParam);
                fileMap.put(fieldName,fileParams);
            }
        }
        return fileMap;
    }

    public List<FileParam> getFileList (String fieldName) {
        return getFileMap().get(fieldName);
    }

    public FileParam getFile(String fieldName) {
        List<FileParam> fileParamList = getFileList(fieldName);
        if (CollectionUtils.isNotEmpty(fileParamList) && fileParamList.size() == 1) {
            return fileParamList.get(0);
        }
        return null;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(formParamList) && CollectionUtils.isEmpty(fileParamList) ;
    }

    public String getString(String name) {
        return (String)(getFieldMap().get(name));
    }

    public double getDouble(String name) {
        return (Double)(getFieldMap().get(name));
    }

    public long getLong(String name) {
        return (Long)(getFieldMap().get(name));
    }
}
