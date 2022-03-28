package com.zr.algorithm.ratelimiting.sentinel;

/**
 * @Author zhourui
 * @Date 2022/3/24 17:40
 */
public class CounterLocal {

    public long timeStamp = System.currentTimeMillis();
    public long interval = 1000 * 60;
    public int reqCount = 0;
    public final int limit = 100;

    public boolean limit() {
        long now = System.currentTimeMillis();
        if (now - timeStamp < interval) {
            reqCount++;
            return reqCount <= limit;
        } else {
            timeStamp = now;
            reqCount = 1;
            return true;
        }
    }
}
