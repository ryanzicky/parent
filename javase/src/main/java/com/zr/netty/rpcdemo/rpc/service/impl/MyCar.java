package com.zr.netty.rpcdemo.rpc.service.impl;

import com.zr.netty.rpcdemo.rpc.service.Car;
import com.zr.netty.rpcdemo.rpc.service.Person;

/**
 * @Author zhourui
 * @Date 2021/5/19 17:22
 */
public class MyCar implements Car {

    @Override
    public String ooxx(String msg) {
        System.out.println("server, get client arg: " + msg);
        return "server res " + msg;
    }

    @Override
    public Person oxox(String name, Integer age) {
        Person p = new Person();
        p.setName(name);
        p.setAge(age);
        return p;
    }
}
