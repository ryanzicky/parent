package com.zr.algorithm.ratelimiting.sentinel;

/**
 * @Author zhourui
 * @Date 2022/3/24 16:43
 */
public class LeakyBucket {

    public long timeStamp = System.currentTimeMillis(); // 当前时间
    public long capacity; // 桶的容量
    public long rate; // 水桶出的素的(每秒系统能处理的请求数)
    public long water; // 当前水量(当前累计请求数)

    public boolean limit() {
        long now = System.currentTimeMillis();
        water = Math.max(0, water - ((now - timeStamp) / 1000) * rate); // 先执行漏水，计算剩余水量
        timeStamp = now;
        if ((water + 1) < capacity) {
            // 尝试加水，并且水还没满
            water += 1;
            return true;
        } else {
            // 水满，拒绝加水
            return false;
        }
    }
}
