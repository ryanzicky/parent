package com.miniservices.demo.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhourui
 * @Date 2021/9/18 17:22
 */
public class CuratorTest {

    private final static String CONNECT_STR = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(5 * 1000, 10);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECT_STR, retryPolicy);
        curatorFramework.start();

        String pathWithParent = "/test";
        byte[] bytes = curatorFramework.getData().forPath(pathWithParent);
        System.out.println(new String(bytes));

        while (true) {
            byte[] bytes1 = curatorFramework.getData().forPath(pathWithParent);
            System.out.println(new String(bytes1));

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
