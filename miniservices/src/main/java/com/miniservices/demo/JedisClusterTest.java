package com.miniservices.demo;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;

/**
 * @Author zhourui
 * @Date 2021/9/15 11:33
 */
public class JedisClusterTest {

    private static final String redisHost = "47.105.171.231";
    private static final Integer port = 6379;

    public static void main(String[] args) throws IOException {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);

        HashSet<HostAndPort> jedisClusterNode = new HashSet<>();
        jedisClusterNode.add(new HostAndPort(redisHost, port));

        JedisCluster jedisCluster = null;

        try {
            jedisCluster = new JedisCluster(jedisClusterNode, 6000, 5000, 10, null, config);
            System.out.println(jedisCluster.set("cluster", "zhuge"));
            System.out.println(jedisCluster.get("cluster"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedisCluster != null) {
                jedisCluster.close();
            }
        }
    }
}
