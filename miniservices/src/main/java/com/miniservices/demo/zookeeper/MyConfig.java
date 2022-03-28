package com.miniservices.demo.zookeeper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author zhourui
 * @Date 2021/9/18 15:49
 */
@Data
@ToString
@NoArgsConstructor
public class MyConfig {

    private String key;
    private String name;
}
