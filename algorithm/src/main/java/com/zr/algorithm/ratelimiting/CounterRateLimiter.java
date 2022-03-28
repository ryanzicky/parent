package com.zr.algorithm.ratelimiting;

/**
 * 固定窗口又称计数器算法（Fixed Windown）限流算法，是最简单的限流算法
 * 通过在单位时间内维护的计数器来控制该时间单位内的最大访问量
 *
 * 假设限制每分钟请求量不超过60，设置一个计数器，当请求达到时如果计数器达到阈值，则拒绝请求，
 * 否则计数器+1，每分钟重置技术器为0
 *
 * 固定窗口最大的有点在于易于实现，并且内存占用小，我们只需要存储时间窗口中的技术即可，它能够确保处理更多的最新请求，
 * 不会因为旧请求的堆积导致新请求饿死。当然也面临着临界问题，当两个窗口交界处，瞬时流量可能为2n。
 *
 * @Author zhourui
 * @Date 2022/3/21 14:47
 */
public class CounterRateLimiter {

    /*每秒限制请求书*/
    private final long permitsPerSecond;

    /*上一窗口的开始时间*/
    public long timeStamp = System.currentTimeMillis();

    /*计数器*/
    private int counter;

    public CounterRateLimiter(long permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
    }

    public boolean tryAcquire() {
        long now = System.currentTimeMillis();
        /*窗口内请求数量小于阈值，更新计数放行，否则拒绝请求*/
        if (now - timeStamp < 1000) {
            if (counter < permitsPerSecond) {
                /*请求数小于阈值，统计数+1*/
                counter++;
                return true;
            }
            return false;
        }
        /*时间窗口过期，重置计数器和时间戳*/
        counter = 0;
        timeStamp = now;
        return true;
    }

    public static void main(String[] args) {
        CounterRateLimiter counterRateLimiter = new CounterRateLimiter(60);
        final int[] count = {0};
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (counterRateLimiter.tryAcquire()) {
                        count[0]++;
                        System.out.println("请求成功: " + finalI + ": " + Thread.currentThread().getName());
                    }
                }
            }).start();
        }
        System.out.println(count[0]);
    }
}
