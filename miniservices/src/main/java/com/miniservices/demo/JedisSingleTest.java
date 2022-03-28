package com.miniservices.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.Arrays;
import java.util.List;

/**
 * @Author zhourui
 * @Date 2021/9/13 17:26
 */
public class JedisSingleTest {

    private static final String redisHost = "47.105.171.231";
    private static final Integer port = 6379;

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisHost, port, 3000, null);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            /*==========================jedis普通示例=========================*/
            /*System.out.println(jedis.set("single1", "zhuge"));
            System.out.println(jedis.get("single1"));*/

            /*==========================管道示例=========================*/
            /*Pipeline pipelined = jedis.pipelined();
            for (int i = 0; i < 10; i++) {
                pipelined.incr("pipelineKey");
                pipelined.set("zhuge" + i, "zhuge");
                
            }
            List<Object> results = pipelined.syncAndReturnAll();
            System.out.println(results);*/

            /*==========================lua脚本示例=========================*/
            jedis.set("product_stock_10016", "15");
            String script = " local count = redis.call('get', KEYS[1])" +
                            " local a = tonumber(count) " +
                            " local b = tonumber(ARGV[1]) " +
                            " if a >= b then " +
                            " redis.call('set', KEYS[1], a - b) " +
                            // 模拟语法报错胡滚操作
                            " bb == 0" +
                            " return 1 " +
                            " end " +
                            " return 0 ";
            Object obj = jedis.eval(script, Arrays.asList("product_stock_10016"), Arrays.asList("10"));
            System.out.println(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
