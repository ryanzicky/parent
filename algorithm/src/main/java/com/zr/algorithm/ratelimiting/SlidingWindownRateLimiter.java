package com.zr.algorithm.ratelimiting;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 为了防止顺序流量，可以把固定窗口进一步划分成多个格子，每次向后移动一小格，而不是固定窗口大小，这就是滑动窗口（SlidingWindown）
 * 比如每分钟可以分为6个10中的单元格，每个格子中维护一个计数器，窗口每次向前滑动一个单元格，每当请求到达时，只要窗口中所有单元格
 * 的计数总和不超过阈值都可以放行。TCP协议中数据包的传输，同样也是采用滑动窗口来进行流量控制。
 *
 * 滑动窗口解决了计数器中的瞬时流量高峰问题，其实计数器算法也是滑动窗口的一种，只不过窗口没有进行更细粒度单元的划分，对比计数器
 * 可见，当窗口划分的粒度越细，则流量控制更加精准和严格。
 *
 * 不过当窗口中流量到达阈值时，流量会瞬间切断，在实际应用中我们要的限流效果往往不是把流量一下子掐断，而是让流量平滑地进入系统当中。
 *
 * @Author zhourui
 * @Date 2022/3/21 15:05
 */
public class SlidingWindownRateLimiter {

    private final long permitsPerMinute;

    private final TreeMap<Long, Integer> counters;

    public SlidingWindownRateLimiter(long permitsPerMinute) {
        this.permitsPerMinute = permitsPerMinute;
        this.counters = new TreeMap<>();
    }

    public synchronized boolean tryAcquire() {
        /*获取当前时间所在的子窗口值：10s一个窗口*/
        long currentWindownTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) / 10 * 10;
        /*获取当前窗口的总体量*/
        int currentWindownCount = getCurrentWindownCount(currentWindownTime);

        if (currentWindownCount >= permitsPerMinute) {
            return false;
        }
        /*计数器+1*/
        counters.merge(currentWindownTime, 1, Integer::sum);
        return true;
    }

    /**
     * 获取当前窗口中的所有请求数（并删除所有无效的子窗口计数器）
     *
     * @param currentWindownTime 当前子窗口时间
     * @return 当前窗口中的计数
     */
    private int getCurrentWindownCount(long currentWindownTime) {
        /*计算出窗口的开始位置时间*/
        long startTime = currentWindownTime - 50;
        int result = 0;

        Iterator<Map.Entry<Long, Integer>> iterator =
                counters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();
            if (entry.getKey() < startTime) {
                iterator.remove();
            } else {
                result += entry.getValue();
            }
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        SlidingWindownRateLimiter slidingWindownRateLimiter = new SlidingWindownRateLimiter(60);
        final int[] count = {0};
        CountDownLatch countDownLatch = new CountDownLatch(60);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (slidingWindownRateLimiter.tryAcquire()) {
                        count[0]++;
                        System.out.println("请求成功: " + finalI + ": " + Thread.currentThread().getName());
                    }

                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println(count[0]);
    }
}
