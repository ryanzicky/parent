package com.zr.algorithm.ratelimiting.sentinel;

import java.util.LinkedList;
import java.util.Random;

/**
 * 滑动时间窗口限流实现
 * 假设某个服务最多只能每秒处理100个请求，我们可以设置一个1秒钟的滑动时间窗口
 * 窗口中有10个格子，每个格子100ms，每100ms移动一次，每次移动都需要记录当前服务请求的次数
 *
 * @Author zhourui
 * @Date 2022/3/24 15:26
 */
public class SlidingTimeWindow {

    // 服务访问次数，可以放在Redis中，实现分布式系统的访问计数
    Long counter = 0L;
    // 使用LinkedList来记录滑动窗口的10个格子
    LinkedList<Long> slots = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        SlidingTimeWindow timeWindow = new SlidingTimeWindow();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    timeWindow.doCheck();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            // 判断限流标记
            timeWindow.counter++;
            Thread.sleep(new Random().nextInt(15));
        }
    }

    void doCheck() throws InterruptedException {
        while (true) {
            Thread.sleep(100);
            slots.addLast(counter);
            if (slots.size() > 10) {
                slots.removeFirst();
            }
            // 比较最后一个和第一个，两者相差100以上就限流
            if ((slots.peekLast() - slots.peekFirst()) > 100) {
                System.out.println("限流了...");
                // 修改限流标标记为true
            } else {
                // 限流标记改为false
            }
        }
    }
}
