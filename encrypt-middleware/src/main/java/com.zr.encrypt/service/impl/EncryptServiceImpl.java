package com.zr.encrypt.service.impl;

import com.zr.encrypt.entity.EncryptDataResponse;
import com.zr.encrypt.entity.EncryptParameter;
import com.zr.encrypt.entity.QueryRequest;
import com.zr.encrypt.entity.WrapperRequest;
import com.zr.encrypt.service.EncryptService;
import com.zr.encrypt.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhourui
 * @Date 2021/10/12 14:34
 */
@Slf4j
@Service
public class EncryptServiceImpl implements EncryptService {

    @Override
    public Map<String, EncryptDataResponse> storageData(WrapperRequest wrapperRequest) {
        List<EncryptParameter> encryptParameters = wrapperRequest.getEncryptParameters();
        if (CollectionUtils.isEmpty(encryptParameters)) {
            return null;
        }
        Map<String, EncryptDataResponse> responseMap = new HashMap<>();
        encryptParameters.forEach(encryptParameter -> {
            responseMap.put(encryptParameter.getValue(), encryptParam(encryptParameter.getValue(), encryptParameter.getCode()));
        });
        return responseMap;
    }

    @Override
    public Map<String, EncryptDataResponse> queryData(QueryRequest queryRequest) {
        List<String> encryptIds = queryRequest.getEncryptIds();
        if (CollectionUtils.isEmpty(encryptIds)) {
            return null;
        }
        Map<String, EncryptDataResponse> responseMap = new HashMap<>();
        encryptIds.forEach(encryptId -> {
            responseMap.put(encryptId, decryptParam(encryptId, queryRequest.getUid()));
        });
        return responseMap;
    }

    private EncryptDataResponse decryptParam(String source, String key) {
        EncryptDataResponse encryptDataResponse = new EncryptDataResponse();
        encryptDataResponse.setEncryptId(source);
        encryptDataResponse.setContent(source);
        encryptDataResponse.setMask(decrypt(source, key));

        return encryptDataResponse;
    }

    private String decrypt(String source, String key) {
        return EncryptUtil.DESdecode(source, key);
    }

    private EncryptDataResponse encryptParam(String source, String key) {
        EncryptDataResponse encryptDataResponse = new EncryptDataResponse();
        encryptDataResponse.setEncryptId(source);
        encryptDataResponse.setContent(source);
        encryptDataResponse.setMask(encrypt(source, key));

        return encryptDataResponse;
    }

    private String encrypt(String source, String key) {
        return EncryptUtil.DESencode(source, key);
    }
}
