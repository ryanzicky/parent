package com.zr.encrypt.entity;

import lombok.Data;

import java.lang.reflect.Field;

@Data
public class DecodeEntity {
    private Object obj;
    private Field field;
    private String encode;
}
