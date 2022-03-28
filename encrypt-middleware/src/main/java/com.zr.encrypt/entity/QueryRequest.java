package com.zr.encrypt.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author zhourui
 * @Date 2021/10/12 14:26
 */
public class QueryRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uid;
    private List<String> encryptIds;

    public QueryRequest() {
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getEncryptIds() {
        return this.encryptIds;
    }

    public void setEncryptIds(List<String> encryptIds) {
        this.encryptIds = encryptIds;
    }
}
