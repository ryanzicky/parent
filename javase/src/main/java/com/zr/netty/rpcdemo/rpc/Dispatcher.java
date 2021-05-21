package com.zr.netty.rpcdemo.rpc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zhourui
 * @Date 2021/5/19 15:11
 */
public class Dispatcher {

    private static Dispatcher dis = null;
    static {
        dis = new Dispatcher();
    }
    public static Dispatcher getDis() {
        return dis;
    }

    private Dispatcher() {

    }

    public static ConcurrentHashMap<String, Object> invokeMap = new ConcurrentHashMap<>();

    public void register(String k, Object obj) {
        invokeMap.put(k, obj);
    }

    public Object get(String k) {
        return invokeMap.get(k);
    }
}
