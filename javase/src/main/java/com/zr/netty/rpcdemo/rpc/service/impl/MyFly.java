package com.zr.netty.rpcdemo.rpc.service.impl;

import com.zr.netty.rpcdemo.rpc.service.Fly;

/**
 * @Author zhourui
 * @Date 2021/5/19 17:22
 */
public class MyFly implements Fly {

    @Override
    public void ooxx(String msg) {
        System.out.println("server, get client arg: " + msg);
    }
}
