package com.jihuangdev.framework.bean;

/**
 * Created by wancan on 2016/5/21.
 */
public class FormParam {
    private String fieldName;
    private String fieldValue;

    public FormParam(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
