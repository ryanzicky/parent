package com.zr.encrypt.service;

import com.zr.encrypt.entity.EncryptDataResponse;
import com.zr.encrypt.entity.QueryRequest;
import com.zr.encrypt.entity.WrapperRequest;

import java.util.Map;

/**
 * @Author zhourui
 * @Date 2021/10/12 14:33
 */
public interface EncryptService {

    Map<String, EncryptDataResponse> storageData(WrapperRequest wrapperRequest);

    Map<String, EncryptDataResponse> queryData(QueryRequest queryRequest);
}
