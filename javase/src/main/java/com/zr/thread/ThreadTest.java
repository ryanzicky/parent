package com.zr.thread;

import java.util.concurrent.*;

/**
 * @Author zhourui
 * @Date 2021/12/20 10:24
 */
public class ThreadTest {

    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    /**
     * CompletableFuture:
     *      1. 创建异步对象
     *      2. 完成时调用
     *      3. 完成时执行
     *      4. 线程串行化
     *      5. 两任务组合，都要完成
     *
     *
     *
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");
        /*CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果: " + i);
        }, executor);*/

        /**
         * 方法完成后的感知
         *
         */
        /*CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 / 0;
            System.out.println("运行结果: " + i);
            return i;
        }, executor).whenComplete((res, exception) -> {
            // 虽然得到异常信息，但是没法修改返回数据
            System.out.println("异步任务成功完成了...结果是..." + res + ": 异常是..." + exception);
        }).exceptionally(throwable -> {
            // 可以感知数据，同时返回默认值
            return 10;
        });*/

        /**
         * 方法完成后的处理
         */
        /*CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果: " + i);
            return i;
        }, executor).handle((res, thr) -> {
            if (res != null) {
                return res * 2;
            }
            if (thr != null) {
                return 0;
            }
            return 0;
        });*/


        /**
         * 线程串行化
         *      1.thenRun：不能呢个获取到上一步的执行结果
         *          .thenRunAsync(() -> {
         *                  System.out.println("任务2启动了...");
         *         }, executor);
         *
         *     2. thenAcceptAsync：能接受上一步结果，但是无返回值
         *          .thenAcceptAsync(res -> {
         *             System.out.println("线程2启动了..." + res);
         *         }, executor);
         *     3.thenApplyAsync：能接受上一步的结果，并且有返回值
         *          .thenApplyAsync(res -> {
         *             System.out.println("线程2启动了..." + res);
         *
         *             return "hello " + res;
         *         }, executor);
         *
         */
        /*CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果: " + i);
            return i;
        }, executor).thenApplyAsync(res -> {
            System.out.println("线程2启动了..." + res);

            return "hello " + res;
        }, executor);*/


        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1线程: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("任务1结束: " + i);
            return i;
        }, executor);

        CompletableFuture<Object> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2线程: " + Thread.currentThread().getId());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2结束: ");
            return "hello";
        }, executor);

        /*future1.runAfterBothAsync(future2, () -> {
            System.out.println("任务3开始..." + Thread.currentThread().getId());
        }, executor);

        future1.thenAcceptBothAsync(future2, (f1, f2) -> {
            System.out.println("任务3开始...之前的结果..." + f1 + "----->" + f2);
        }, executor);

        CompletableFuture<String> future = future1.thenCombineAsync(future2, (f1, f2) -> {
            return f1 + "----->" + f2 + "--------> Haha";
        }, executor);*/

        /**
         * 两个任务，只要有一个完成，我们就执行任务3
         * runAfterEitherAsync：不感知结果，自己没有返回值
         * acceptEitherAsync：感知结果，自己没有返回值
         *
         */
        /*future1.runAfterEitherAsync(future2, () -> {
            System.out.println("任务3开始...之前的结果...");
        }, executor);*/

        /*future1.acceptEitherAsync(future2, (res) -> {
            System.out.println("任务3开始...之前的结果..." + res);
        }, executor);*/

        /*CompletableFuture<String> completableFuture = future1.applyToEitherAsync(future2, (res) -> {
            System.out.println("任务3开始...之前的结果..." + res);
            return res.toString() + "哈哈";
        }, executor);*/

        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("task1");
            return "task1";
        }, executor);

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("task2");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "task2";
        }, executor);

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("task3");
            return "task3";
        }, executor);

        CompletableFuture<Void> allOf = CompletableFuture.allOf(task1, task2, task3);
        allOf.get();

        System.out.println("main...end..." + task1.get() + "->" + task2.get() + "->" + task3.get());

    }

    /**
     * 1. 继承Thread
     * 2. 实现Runnable接口
     * 3. 实现Call接口+FutureTask（可以拿到返回结果，可以处理异常）
     * 4. 线程池
     *
     * 将所有的多线程异步任务都交给线程池执行
     *
     * 区别：
     *      1.2 不能得到返回值，3可以获取返回值
     *      1，2，3都不能控制资源
     *      4可以控制资源，性能稳定
     *
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void thread(String[] args) throws ExecutionException, InterruptedException {
//        Threado1 threado1 = new Threado1();
//        threado1.start();

//        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
//        new Thread(futureTask).start();
//
//        // 阻塞等待线程执行完成，获取返回结果
//        Integer integer = futureTask.get();
//        System.out.println("main...end..." + integer);

        executor.execute(new Runnable01() {});
        System.out.println("main...end...");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static class Threado1 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果: " + i);
        }
    }

    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果: " + i);
        }
    }

    public static class Callable01 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果: " + i);
            return i;
        }
    }
}
