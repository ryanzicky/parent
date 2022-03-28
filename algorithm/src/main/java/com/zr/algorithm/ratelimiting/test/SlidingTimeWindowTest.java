package com.zr.algorithm.ratelimiting.test;

import java.util.LinkedList;

/**
 * @Author zhourui
 * @Date 2022/3/24 18:03
 */
public class SlidingTimeWindowTest {

    volatile int reqCounter = 0;
    LinkedList<Integer> slots = new LinkedList<>();
    volatile static boolean flag = true;
    int slotSize = 100;
    int slotSplit = 10;

    public static void main(String[] args) {
        SlidingTimeWindowTest slidingTimeWindowTest = new SlidingTimeWindowTest();
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        boolean test = slidingTimeWindowTest.test();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public boolean test() throws InterruptedException {
        reqCounter++;
        while (true) {
            Thread.sleep(100);
            slots.addLast(reqCounter);
            if (slots.size() > slotSplit) {
                slots.removeFirst();
            }
            if ((slots.peekLast() - slots.peekFirst()) > slotSize) {
                System.out.println("限流了");
                flag = false;
                return false;
            } else {
                flag = true;
                return true;
            }
        }
    }
}
