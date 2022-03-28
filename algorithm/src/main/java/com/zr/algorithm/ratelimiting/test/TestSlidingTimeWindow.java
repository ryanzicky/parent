package com.zr.algorithm.ratelimiting.test;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhourui
 * @Date 2022/3/24 18:28
 */
public class TestSlidingTimeWindow {

    LinkedList<Integer> cells;
    int cellSize = 10;
    int limit = 100;
    static ReentrantLock lock = new ReentrantLock();
    static CountDownLatch latch = new CountDownLatch(11);

    public TestSlidingTimeWindow(int cellSize, int limit) {
        this.cellSize = cellSize;
        this.limit = limit;
        this.cells = new LinkedList<>();
    }

    public static void main(String[] args) throws InterruptedException {
        TestSlidingTimeWindow testSlidingTimeWindow = new TestSlidingTimeWindow(10, 100);
        for (int i = 0; i < 11; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        int reqCount = new Random().nextInt(20);
                        testSlidingTimeWindow.cells.addLast(reqCount);
                        if (testSlidingTimeWindow.cells.size() > testSlidingTimeWindow.cellSize) {
                            testSlidingTimeWindow.cells.removeFirst();
                        }

                        try {
                            Thread.sleep(new Random().nextInt(500));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {

                    } finally {
                        lock.unlock();
                        latch.countDown();
                    }

                }
            }).start();
        }

        latch.await();
        System.out.println(testSlidingTimeWindow.cells.size());
        int count = 0;
        for (Integer cell : testSlidingTimeWindow.cells) {
            System.out.println("cell = " + cell);
            count += cell;
        }
        System.out.println("count = " + count);
        if (count > testSlidingTimeWindow.limit) {
            System.out.println("限流了");
        } else {
            System.out.println("没限流");
        }
    }
}
