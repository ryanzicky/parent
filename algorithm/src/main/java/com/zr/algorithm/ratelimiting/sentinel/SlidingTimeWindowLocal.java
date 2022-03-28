package com.zr.algorithm.ratelimiting.sentinel;

import java.util.LinkedList;

/**
 * @Author zhourui
 * @Date 2022/3/24 17:55
 */
public class SlidingTimeWindowLocal {

    static int counter = 0;
    LinkedList<Integer> slots = new LinkedList<>();
    volatile static boolean flag = true;
    private int slotSize = 10;
    private int limit = 100;

    public static void main(String[] args) throws InterruptedException {
        SlidingTimeWindowLocal slidingTimeWindow = new SlidingTimeWindowLocal();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    slidingTimeWindow.doCheck();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (flag) {
            counter++;
            Thread.sleep(100);
        }
    }

    private boolean doCheck() throws InterruptedException {
        while (true) {
            Thread.sleep(100);
            slots.addLast(counter);
            if (slots.size() > slotSize) {
                slots.removeFirst();
            }
            if ((slots.peekLast() - slots.peekFirst()) > limit) {
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
