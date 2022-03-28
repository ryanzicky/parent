package com.zr.algorithm.ratelimiting.pkg1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author zhourui
 * @Date 2022/3/21 19:27
 */
public class CounterLimiter {

    private int windownSize; // 窗口大小，单位为毫秒
    private int limit; // 窗口内限流大小
    private AtomicInteger count; // 当前窗口的计数器

    public CounterLimiter() {
    }

    public CounterLimiter(int windownSize, int limit) {
        this.windownSize = windownSize;
        this.limit = limit;

        count = new AtomicInteger(0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    count.set(0);
                    try {
                        Thread.sleep(windownSize);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public boolean tryAcquire() {
        int newCount = count.addAndGet(1);
        if (newCount > limit) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CounterLimiter counterLimiter = new CounterLimiter(1000, 20);
        int count = 0;
        for (int i = 0; i < 50; i++) {
            if (counterLimiter.tryAcquire()) {
                count++;
            }
        }

        System.out.println("第一波50次请求中通过： " + count + "， 限流： " + (50 - count));
        Thread.sleep(1000);
        count = 0;
        for (int i = 0; i < 50; i++) {
            if (counterLimiter.tryAcquire()) {
                count++;
            }
        }

        System.out.println("第二波50次请求中通过： " + count + "， 限流： " + (50 - count));
    }
}
