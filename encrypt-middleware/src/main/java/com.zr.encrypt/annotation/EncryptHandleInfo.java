package com.zr.encrypt.annotation;

import com.zr.encrypt.strategy.IDecryptResultFieldStrategy;
import com.zr.encrypt.strategy.IEncryptResultFieldStrategy;

import java.lang.annotation.*;

/**
 * 通过注解来表明，我们需要对那个字段进行加密
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EncryptHandleInfo {
    String wrapperName();

    String paramName();

    /**
     * 加密策略 -- 和加密字段，一一对应
     */
    Class<? extends IEncryptResultFieldStrategy> encryptStrategy();

    Class<? extends IDecryptResultFieldStrategy> decodeStrategy();
}
