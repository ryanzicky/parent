package com.zr.algorithm.ratelimiting;

import java.util.Map;
import java.util.TreeMap;

/**
 * 滑动日志限速算法需要记录请求的时间戳，通常使用有序集合来存储，我们可以在单个有序集合中跟踪用户在一个时间段内所有的请求
 *
 * 假设我们要限制给定T时间内的请求不超过N，我们只需要存储最近T时间之内的请求日志，每当请求到来时判断最近T时间内的请求总数是否
 * 超过阈值
 *
 * 滑动窗口能够避免突发流量，实现较为精准的限流；同样更加灵活，能够支持更加复杂的限流策略，如多级限流，每分钟不超过100次
 * 没小时不超过300次，每天不超过1000次，我们只需要保存最近24小时所有的请求日志即可时间
 *
 * 灵活不是没有代价的，带来的缺点就是占用存储空间要高于其他限流算法
 *
 * @Author zhourui
 * @Date 2022/3/21 17:51
 */
public class SlidingLogRateLimiter {

    /**
     * 每分钟限制计数
     */
    private static final long PERMITS_PER_MINUTE = 60;

    /**
     * 请求日志计数器，k-为请求的时间（s），value-为当前时间的请求数量
     */
    private final TreeMap<Long, Integer> requestLogCountMap = new TreeMap<>();

    public synchronized boolean tryAcquire() {
        /*最小粒度为s*/
        long currentTimeStamp = System.currentTimeMillis();
        /*获取当前窗口的请求总数*/
        int currentWindownCount = getCurrentWindownCount(currentTimeStamp);
        if (currentWindownCount >= PERMITS_PER_MINUTE) {
            return false;
        }
        /*请求成功,将当前请求日志加入到日志中*/
        requestLogCountMap.merge(currentTimeStamp, 1, Integer::sum);
        return true;
    }

    /**
     * 统计当前时间窗口内的请求数
     *
     * @param currentTimeStamp 当前时间
     * @return
     */
    private int getCurrentWindownCount(long currentTimeStamp) {
        /*计算出窗口的开始位置时间*/
        long startTime = currentTimeStamp - 59;
        /*遍历当前存储的计数器，删除无效的子窗口计数器，并累加当前窗口中的所有计数器值和*/
        return requestLogCountMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey() >= startTime)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }
}
