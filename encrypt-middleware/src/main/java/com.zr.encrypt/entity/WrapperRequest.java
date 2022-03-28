package com.zr.encrypt.entity;

import java.util.List;

/**
 * @Author zhourui
 * @Date 2021/10/12 14:13
 */
public class WrapperRequest {
    private static final long serialVersionUID = 1L;
    private String uid;
    private List<EncryptParameter> encryptParameters;

    public WrapperRequest() {
    }

    public List<EncryptParameter> getEncryptParameters() {
        return this.encryptParameters;
    }

    public void setEncryptParameters(List<EncryptParameter> encryptParameters) {
        this.encryptParameters = encryptParameters;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
