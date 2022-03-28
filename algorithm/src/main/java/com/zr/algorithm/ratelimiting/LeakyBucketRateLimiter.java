package com.zr.algorithm.ratelimiting;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 漏桶算法：请求就像水一样以任意速度注入漏桶，而桶会按照固定的速率将水漏掉，当注入速度持续大于漏出的速度时，漏桶会变满
 * 此时新进入的请求将会被丢弃。限流和整形是漏桶算法的两个核心能力。
 *
 * 想要以恒定的速率漏出流量，通常还应配合一个FIFO队列来实现，当tryAcquire返回true时，将请求入队，然后再以固定频率从队列
 * 中取出请求进行处理。
 *
 * 漏桶算法存在的目的主要是用来平滑突发的流量，提供一种机制来确保网络中的突发流量被整合成平滑稳定的流量
 * 不过由于漏桶对流量的控制过于严格，在有些场景下不能充分使用系统资源，因为漏桶的漏出速率是固定的，
 * 即使在某一时刻下游能够处理更大的流量，漏桶也不允许突发流量通过。
 *
 * @Author zhourui
 * @Date 2022/3/21 15:26
 */
public class LeakyBucketRateLimiter {

    /*桶的容量*/
    private final int capacity;

    /*漏出速率*/
    private final int permitsPerSecond;

    /*剩余水量*/
    private long leftWater;

    /*上次注入时间*/
    private long timeStamp = System.currentTimeMillis();

    public LeakyBucketRateLimiter(int capacity, int permitsPerSecond) {
        this.capacity = capacity;
        this.permitsPerSecond = permitsPerSecond;
    }

    public synchronized boolean tryAcquire() {
        /*1. 计算剩余水量*/
        long now = System.currentTimeMillis();
        long timeGap = (now - timeStamp) / 1000;
        leftWater = Math.max(0, leftWater - timeGap * permitsPerSecond);
        timeStamp = now;

        /*如果未满，则方形；否则拦截*/
        if (leftWater < capacity) {
            leftWater += 1;
            return true;
        }
        return false;
    }

    public void testLeakyBucketRateLimiter() throws InterruptedException {
        ThreadPoolExecutor singleThread = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, null, (RejectedExecutionHandler) null);


        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(20, 20);
        Queue<Integer> queue = new LinkedList<>();

        singleThread.execute(() -> {
            int count = 0;
            while (true) {
                count++;
                boolean flag = rateLimiter.tryAcquire();

                if (flag) {
                    queue.offer(count);
                    System.out.println(count + "-------流量放行-------");
                } else {
                    System.out.println(count + "流量被限制");
                }
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (!queue.isEmpty()) {
                System.out.println(queue.poll() + "被处理");
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        while (true) {
            Thread.sleep(10000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor singleThread = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, null, (RejectedExecutionHandler) null);


        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(20, 20);
        Queue<Integer> queue = new LinkedList<>();

        singleThread.execute(() -> {
            int count = 0;
            while (true) {
                count++;
                boolean flag = rateLimiter.tryAcquire();

                if (flag) {
                    queue.offer(count);
                    System.out.println(count + "-------流量放行-------");
                } else {
                    System.out.println(count + "流量被限制");
                }
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (!queue.isEmpty()) {
                System.out.println(queue.poll() + "被处理");
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        while (true) {
            Thread.sleep(10000);
        }
    }
}
