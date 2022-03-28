package com.zr.algorithm.ratelimiting.sentinel;

/**
 * @Author zhourui
 * @Date 2022/3/24 17:51
 */
public class TokenBucketLocal {

    public long timeStamp = System.currentTimeMillis();
    public long tokens; // 当前令牌数
    public long capacity; // 桶的容量
    public long rate; // 放令牌速率

    public boolean checkToken() {
        long now = System.currentTimeMillis();
        tokens = Math.min(capacity, (now - timeStamp) / 1000 * rate);
        timeStamp = now;
        if (tokens < 1) {
            return false;
        } else {
            tokens--;
            return true;
        }
    }
}
