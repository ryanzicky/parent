package com.miniservices.demo;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @Author zhourui
 * @Date 2021/9/17 15:22
 */
public class RedussionBloomFilter {

    private static final String redisHost = "47.105.171.231";
    private static final Integer port = 6379;

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+ redisHost + ":" + port);

        // 构造redission
        RedissonClient redission = Redisson.create(config);
        RBloomFilter<Object> bloomFilter = redission.getBloomFilter("nameList");
        // 初始化布隆过滤器:预计元素100000000L，误差率为3%，根据这两个参数会计算出底层的bit数组大小
        bloomFilter.tryInit(100000000L, 0.03);
        // 将zhuge插入到布隆过滤器重
        bloomFilter.add("zhuge");

        // 判断下面内容是否在布隆过滤器中
        System.out.println(bloomFilter.contains("guojia")); // false
        System.out.println(bloomFilter.contains("baiqi")); // false
        System.out.println(bloomFilter.contains("zhuge")); // true
    }

}
