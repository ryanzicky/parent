package com.example.demo.case2;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * @Author zhourui
 * @Date 2021/8/7 22:13
 */
public class DistributedLockTest {

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        final DistribuutedLock lock1 = new DistribuutedLock();
        final DistribuutedLock lock2 = new DistribuutedLock();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock1.zkLock();
                    System.out.println("线程1启动，获取到锁");
                    Thread.sleep(5000);

                    lock1.unZkLock();
                    System.out.println("线程1释放锁");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock2.zkLock();
                    System.out.println("线程2启动，获取到锁");
                    Thread.sleep(5000);

                    lock2.unZkLock();
                    System.out.println("线程2释放锁");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
