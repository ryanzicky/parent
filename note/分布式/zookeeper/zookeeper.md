### Zookeeper
    zookeeper是一个分布式协调框架，主要用来解决分布式应用中经常遇到的一些数据管理问题。
    如：统一命名服务，状态同步服务，集群管理，分布式应用配置项的管理

### Zookeeper核心概念
    Zookeeper可以理解为一个用于存储少量数据的基于内存的数据库，主要有一下两个核心概念：
    文件系统数据结构 + 监听通知机制
    
    1. 文件系统数据结构
    
    2. 监听通知机制
        一次性

    3. ACL权限管理

    4. 持久化，数据快照
    
    基本特性 -> 使用 -> 分析zookeeper底层实现 -> zab,通知机制

### curator
    服务端启动集群:
        1. myid
        2. zoo.cfg:
            server.1=127.0.0.1:2001:3001
            server.2=127.0.0.1:2002:3002
            server.3=127.0.0.1:2003:3003
            server.4=127.0.0.1:2004:3004:observer
        leader
        follower
        observer
    客户端连接到集群: zkCli.cmd -server 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184

### zookeeper 5.0新特性
    动态配置

### 分布式锁
    1. 非公平锁
        竟态
        惊群效应

    2. 公平锁
        
        
    