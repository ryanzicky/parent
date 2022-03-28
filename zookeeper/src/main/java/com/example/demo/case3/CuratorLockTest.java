package com.example.demo.case3;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author zhourui
 * @Date 2021/8/8 11:37
 */
public class CuratorLockTest {

    public static void main(String[] args) {
        // 1. 创建分布式锁1
        InterProcessMutex lock1 = new InterProcessMutex(geTCuratorFramework(), "/locks");

        // 2. 创建分布式锁2
        InterProcessMutex lock2 = new InterProcessMutex(geTCuratorFramework(), "/locks");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock1.acquire();
                    System.out.println("线程1 获取到锁");

                    lock1.acquire();
                    System.out.println("线程1 再次获取到锁");

                    Thread.sleep(5000);

                    lock1.release();
                    System.out.println("线程1 释放锁");

                    lock1.release();
                    System.out.println("线程1 再次释放锁");
                } catch (Exception e) {

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock2.acquire();
                    System.out.println("线程2 获取到锁");

                    lock2.acquire();
                    System.out.println("线程2 再次获取到锁");

                    Thread.sleep(5000);

                    lock2.release();
                    System.out.println("线程2 释放锁");

                    lock2.release();
                    System.out.println("线程2 再次释放锁");
                } catch (Exception e) {

                }
            }
        }).start();
    }

    private static CuratorFramework geTCuratorFramework() {
        ExponentialBackoffRetry policy = new ExponentialBackoffRetry(3000, 3);

        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("47.105.171.231:2181,47.105.171.231:2182,47.105.171.231:2183")
                .connectionTimeoutMs(2000)
                .sessionTimeoutMs(2000)
                .retryPolicy(policy)
                .build();

        // 启动客户端
        client.start();

        System.out.println("zookeeper 启动成功");
        return client;
    }
}
