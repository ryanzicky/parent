package com.zr.encrypt.strategy;

/**
 * 加密策略
 */
public interface IEncryptResultFieldStrategy {

    /**
     * 返回加密之后的字符
     *
     * @param source 原始字符
     * @return 加密之后的字符
     */
    String encrypt(String source);

}
