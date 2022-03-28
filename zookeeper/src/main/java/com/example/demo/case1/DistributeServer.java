package com.example.demo.case1;

import org.apache.zookeeper.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * @Author zhourui
 * @Date 2021/8/7 21:19
 */
public class DistributeServer{

    private String connectString = "47.105.171.231:2181,47.105.171.231:2182,47.105.171.231:2183";
    private int sessionTimeout = 2000;
    ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        DistributeServer server = new DistributeServer();
        // 1. 获取zk连接
       server.getConnect();
        // 2. 注册服务器到zk集群
        server.regist(args[0]);
        // 3. 启动业务逻辑(睡觉)
        server.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void regist(String hostName) throws KeeperException, InterruptedException {
        String create = zk.create("/servers/" + hostName, hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println(hostName + " is online.");
    }

    private void getConnect() throws IOException {
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }
}
