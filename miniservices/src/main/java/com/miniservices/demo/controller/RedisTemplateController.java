package com.miniservices.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhourui
 * @Date 2021/9/17 16:52
 */
@RestController
@RequestMapping("/redisTemplate")
@Slf4j
public class RedisTemplateController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Redisson redisson;

    @GetMapping("/deduct_stock")
    public String deductStock() {
        String lockKey = "product_001";
//        String clienId = UUID.randomUUID().toString();
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            /*Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clienId, 10, TimeUnit.SECONDS);
            if (!result) {
                return "false";
            }*/
            // 加锁，看门狗(实现锁续命)
            redissonLock.lock();
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int readStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", readStock + "");
                log.info(Thread.currentThread().getName() + ": 扣减成功,剩余库存: {}", readStock);
            } else {
                log.info(Thread.currentThread().getName() + ": 扣减失败,库存不足");
            }
        } catch (Exception e) {
            redissonLock.unlock();
            /*if (clienId.equals(stringRedisTemplate.opsForValue().get(lockKey))) {
                stringRedisTemplate.delete(lockKey);
            }*/
        }
        return "true";
    }

    @GetMapping("/test")
    public String test() {
        int total = 100;
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    deductStock();
                }
            });

            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            thread.start();
        }
        return "true";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/addStock")
    public String addStock() {
        stringRedisTemplate.opsForValue().set("stock", "50");
        return "true";
    }
}
