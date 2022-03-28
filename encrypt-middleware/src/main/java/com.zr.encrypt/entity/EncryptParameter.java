package com.zr.encrypt.entity;

import com.zr.encrypt.enums.DataType;

import java.io.Serializable;

/**
 * @Author zhourui
 * @Date 2021/10/12 14:14
 */
public class EncryptParameter implements Serializable {

    private static final long serialVersionUID = 4257433146216866328L;
    private String code;
    private String value;
    private DataType dataType;

    public EncryptParameter() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
