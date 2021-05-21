package com.zr.netty.rpcdemo.rpc;

import com.zr.netty.rpcdemo.rpc.util.Packmsg;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zhourui
 * @Date 2021/5/19 17:13
 */
public class ResponseMappingCallback {
    static ConcurrentHashMap<Long, CompletableFuture> mapping = new ConcurrentHashMap<>();

    public static void addCallBack(long requestID, CompletableFuture cb) {
        mapping.putIfAbsent(requestID, cb);
    }

    public static void runCallBack(Packmsg msg) {
        CompletableFuture cf = mapping.get(msg.getHeader().getRequestID());
        cf.complete(msg.getContent().getRes());
        removeCB(msg.getHeader().getRequestID());
    }

    private static void removeCB(long requestID) {
        mapping.remove(requestID);
    }
}
