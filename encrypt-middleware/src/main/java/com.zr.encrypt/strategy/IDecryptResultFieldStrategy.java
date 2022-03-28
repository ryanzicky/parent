package com.zr.encrypt.strategy;

import java.util.List;

/**
 * 加密策略
 */
public interface IDecryptResultFieldStrategy {

    /**
     * 返回加密之后的字符
     *
     * @param source 原始字符
     * @return 加密之后的字符
     */
    List<String> decode(List<String> source);

}
