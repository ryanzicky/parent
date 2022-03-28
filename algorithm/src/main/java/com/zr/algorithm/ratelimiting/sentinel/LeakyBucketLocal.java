package com.zr.algorithm.ratelimiting.sentinel;

/**
 * @Author zhourui
 * @Date 2022/3/24 17:45
 */
public class LeakyBucketLocal {

    public long timeStamp = System.currentTimeMillis(); // 当前时间
    public long water = 0; // 当前水量
    public long capacity; // 桶的容量
    public long rate = 0; // 流入速率

    public boolean checkWater() {
        long now = System.currentTimeMillis();
        water = Math.max(0, water + ((now - timeStamp) / 1000) * rate); // 先执行漏水，计算当前水量
        timeStamp = now;
        if ((water + 1) < capacity) {
            water++;
            return true;
        } else {
            water = 0;
            return false;
        }
    }

}
