package com.zr.encrypt.strategy.impl;

import com.zr.encrypt.encrypt.EncryptEncodeDecodeService;
import com.zr.encrypt.enums.DataType;
import com.zr.encrypt.strategy.IEncryptResultFieldStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankNoEncryptStrategy implements IEncryptResultFieldStrategy {
    @Autowired
    private EncryptEncodeDecodeService encryptEncodeDecodeService;
    @Override
    public String encrypt(String source) {
        return encryptEncodeDecodeService.getEncodeValue(source, DataType.BANK_NO, null);
    }


}
