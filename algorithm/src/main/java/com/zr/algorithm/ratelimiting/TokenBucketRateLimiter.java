package com.zr.algorithm.ratelimiting;

/**
 * 如何在能够限制流量速率的前提下，又能够允许突发流量呢？
 *  令牌桶算法：
 *      令牌桶算法是以恒定速率向令牌桶发送令牌，请求到达时，尝试从令牌桶中拿令牌，只有拿到令牌才能放行，否则将会被拒绝
 *
 *  令牌桶算法具有以下特点：
 *      1. 以恒定速率发放令牌，假设限流速率为v/s，则表示没1/v秒发放一个令牌
 *      2. 假设令牌桶容量是b，如果令牌桶已满，则新的令牌会被丢弃
 *      3. 请求能够通过限流器的前提是令牌桶中有令牌。
 *
 *  令牌桶算法中值得关注的参数有两个，即限流速率v/s，和令牌桶容量b，速率a表示限流器一般情况下的限流速率，而b则是burst的简写。
 *  表示限流器允许的最大突发流量。
 *
 *  比如b=10，当令牌桶满的时候有10个可用令牌，此时允许10个请求同时通过限流器（允许流量一定程度的突发）。
 *  这10个请求瞬间消耗完令牌后，后续的流量只能按照速率r通过限流器
 *
 *  需要逐一的是，非常容易被想到的实现是生产者消费者模式，用一个生产者线程定时向队列中添加令牌，而试图通过限流器的线程作为消费者线程，
 *  只有从阻塞队列中获取到令牌，才允许通过限流器
 *
 *  由于线程调度的不确定性，在高并发场景时，定时器误差非常大，同时定时器本身会创建调度线程，也会对系统的性能产生影响
 *
 *
 * @Author zhourui
 * @Date 2022/3/21 16:54
 */
public class TokenBucketRateLimiter {

    /*令牌桶的容量*/
    private final long capacity;

    /*令牌发放率*/
    private final long generateperSeconds;

    /*最后一个令牌发放的时间*/
    long lastTokenTime = System.currentTimeMillis();

    /*当前令牌数量*/
    private long currentTokens;

    public TokenBucketRateLimiter(long capacity, long generateperSeconds) {
        this.capacity = capacity;
        this.generateperSeconds = generateperSeconds;
    }

    /**
     * 尝试获取令牌
     *
     * @return true 表示获取到令牌，方形；否则为限流
     */
    public synchronized boolean tryAcquire() {
        /**
         * 计算令牌当前数量
         * 请求时间在最后令牌是产生时间差大于等于1s（为啥1s），则
         *  1. 重新计算令牌桶中的令牌书
         *  2. 将最后一个令牌发放时间重置为当前时间
         */
        long now = System.currentTimeMillis();
        if (now - lastTokenTime >= 1000) {
            long newPermits = (now - lastTokenTime) / 1000 * generateperSeconds;
            currentTokens = Math.min(currentTokens + newPermits, capacity);
            lastTokenTime = now;
        }

        if (currentTokens > 0) {
            currentTokens --;
            return true;
        }
        return false;
    }
}
