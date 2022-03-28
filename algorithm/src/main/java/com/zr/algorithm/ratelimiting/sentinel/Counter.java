package com.zr.algorithm.ratelimiting.sentinel;

/**
 * @Author zhourui
 * @Date 2022/3/24 15:15
 */
public class Counter {

    public long timeStamp = System.currentTimeMillis(); // 当前时间
    public int reqCount = 0; // 初始化计数器
    public final int limit = 100; // 时间窗口内最大请求数
    public final long interval = 1000 * 60; // 时间窗口 60ms

    public boolean limit() {
        long now = System.currentTimeMillis();
        if (now < timeStamp + interval) {
            // 在时间窗口内
            reqCount++;
            return reqCount <= limit;
        } else {
            // 时间重置
            timeStamp = now;
            // 请求次数重置
            reqCount = 1;
            return true;
        }
    }
}
