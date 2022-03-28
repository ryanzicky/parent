package com.miniservices.demo.zookeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhourui
 * @Date 2021/9/18 15:43
 */
@Slf4j
public class ConfigerCenter {

    private final static String CONNECT_STR = "127.0.0.1";
    private final static Integer SESSION_TIMEOUT = 30 * 1000;

    private static ZooKeeper zooKeeper = null;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(CONNECT_STR, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getType() == Event.EventType.None
                    && watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    log.info("连接已建立");
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();

        MyConfig myConfig = new MyConfig();
        myConfig.setKey("anyKey");
        myConfig.setName("anyName");

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsBytes(myConfig);
        String result = zooKeeper.create("/myconfig", bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        Watcher watcher = new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeDataChanged
                    && event.getPath() != null
                    && event.getPath().equals("/myconfig")) {
                    log.info("PATH: {} 发生了数据变化", event.getPath());

                    byte[] data = zooKeeper.getData("/myconfig", this, null);

                    MyConfig newConfig = objectMapper.readValue(new String(data), MyConfig.class);
                    log.info("数据发生变化: {}", newConfig);

                }
            }
        };
        byte[] data = zooKeeper.getData("/myconfig", watcher, null);
        log.info("原始数据: {}", objectMapper.readValue(new String(data), MyConfig.class));

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
