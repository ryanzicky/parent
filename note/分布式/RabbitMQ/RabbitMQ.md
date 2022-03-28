### 1. MQ的基本概念
    1.1 MQ概述
    MQ全称 Message Queue(消息队列),是在消息的传输过程中保存消息的容器，多用于
    分布式系统之间进行通信

    1.2 MQ的优势
        1. 应用解耦
            提高系统容错性和可维护性
        2. 异步提速
            提升用户体验和系统吞吐量
        3. 削峰填谷
            提高系统稳定性

### 常见MQ产品
    RabbitMQ
        单机吞吐量：万级(其次)
        消息延迟：微妙级
        并发能力强，性能极好，延时低，社区活跃，管理界面丰富
    ActiveMQ
        万级(最差)
        毫秒级
        老牌产品多，成熟度高，文档较多
    RocketMQ
        十万级(最好)
        毫秒级
        MQ功能比较完备，扩展性佳
    Kafka
        十万级(次之)
        毫秒以内
        只支持主要的MQ功能，毕竟是为大数据领域准备的

### RabbitMQ简介
    AMQP：高级消息队列协议，是一个网络协议，是应用层协议的一个开放标准，为面向消息的中间件设计
    类似HTTP

    Broker: 接收和分发消息的应用，RabbitMQ Server就是Message Broker
    Virtual Host: 处于多租户和完全因素设计的，把AMQP的基本组件划分到一个虚拟的分组中，类似于网络中的
                    namespace 概念，当多个不同的用户使用一个RabbitMQ Server提供的服务时，可以划分出多个
                    vhost，每个用户在自己的vhost创建exchange/queue等
    Connection: publisher / consumer 和broker之间的TCP连接
    Channel: 如果每一次访问RabbitMQ都建立一个Connection，在消息量大的时候建立TCP Connection的开销是将是巨大的，
            效率也低，Channel是在Connection内部建立的逻辑连接，如果应用程序支持多线程，通常每个thread创建单独的channel
            进行通讯，AMQP method 包含了channel id帮助客户端和message broker 识别channel，所以channel之间是完全隔离的。
    Exchange: message 到达broker的第一站，根据分发规则，匹配查询表中的routing key，分发消息到queue中去。
                常用的类型有：direct(point-to-point)，topic(publish-subscribe) and fanout(multicast)
    Queue: 消息最终被送到这里等待consumer取走
    Binding: exchange 和 queue之间的虚拟连接，binding中可以包含routing key，Binding信息被保存到exchange中的查询表中，
            用于message的分发依据
    
### RabbitMQ工作模式
    简单模式，work queues，Publish/Subscribe 发布与订阅模式
    Routing 路由模式，Topics 主题模式，RPC远程调用模式(不太算MQ)

    1. 简单模式 HelloWorld
        一个生产者，一个消费者，不需要设置交换机(使用默认的交换机)
    2. 队列模式 Work Queues
        一个生产者，多个消费者(竞争关系)，不需要设置交换机(使用默认的交换机)

        比简单模式多了一个或多个消费端，多个消费端共同消费同一个队列中的消息
        应用场景: 对于任务过重或任务较多情况使用工作队列可以提高任务处理的速度
        轮询消费

    3. 发布/订阅 Publish/Subscribe
        需要设置类型为Fanout的交换机，并且交换机和队列进行绑定，当发送消息到交换机后，交换机将消息发送到绑定的队列

        在订阅模型中，多了一个Exchange角色，而且过程有变化：
        P: 生产者，也就是要发送消息的程序，但是不再发送到队列中，而是发给x(交换机)
        C: 消费者，消息的接受者，会一直等待消息的到来
        Queue: 消息队列，接受消息，缓存消息
        Exchange: 交换机(X), 一方面，接收生产者发送的消息，另一方面，知道如何处理消息，例如递交给
            某个特别队列，递交给所有队列，或是将消息丢弃，取决于Exchange的类型：
            1. Fanout: 广播，将消息交给所有绑定到交换机的队列
            2. Direct: 定向，把消息交给符合指定Routing key的队列
            3. Topic: 通配符，把消息交给符合routing key(路由模式)的队列
        Exchange(交换机)只负责转发消息，不具备存储消息的能力

    工作队列模式与发布订阅模式的区别：
        1. 工作队列模式不用定义交换机，发布订阅模式需要定义交换机
        2. 发布订阅模式的生产方是面向交换机发送消息，工作队列模式的生产方是面向队列发送消息(底层使用默认交换机)
        3. 发布订阅模式需要设置队列和交换机的绑定，工作队列模式不需要设置，实际上工作队列模式会将队列绑定到默认的交换机
    
    4. 路由 Routing
        需要设置类型为Direct的交换机，交换机和队列进行绑定，并且指定routing key，当发送消息到交换机后，交换机
        会根据routing key将消息发送到对应的队列

        1. 队列与交换机的绑定，不能是任意绑定了，而是要指定一个Routing Key(路由key)
        2. 消息的发送方在向Exchange发送消息时，也必须指定消息的Routing Key
        3. Exchange 不再把消息交给每一个绑定的队列，而是根据消息的Routing Key进行判断，只有队列的Routing Key与
        消息的Routing Key完全一致，才会接受到消息
        
    5. 主题 Topics
        需要设置类型为Topic的交换机，交换机和队列进行绑定，并且指定通配符方式的routing key，当发送消息到交换机后，交换机
        会根据routing key将消息发送到对应的队列

        通配符模式
        1. Topic 与Direct相比，都是可以根据Routing Key把消息路由到不同的队列，只不过Topic类型Exchange可以
        让队列在绑定Routing Key的时候使用通配符
        2. RoutingKey一般是由一个或多个单词组成，多个单词之间以“.”分隔
        3. 通配符规则：
            # 匹配一个或多个词
            * 匹配不多不少恰好一个词
    6. RPC

### Confirm 和 Return
    Confirm代表生产者将消息传送到Broker时产生的状态，后续会出现两种情况：
        1. ack 代表broker已经将数据接收
        2. nacj 代表broker拒收消息，原因有多种，队列已满，限流，IO异常
    Return 代表消息被Broker正常接收(ack)后，但Broker没有对应的队列进行投递时产生的状态，消息被
    退回给生产者
    
    上面两种状态只代表生产者与Broker之间消息投递的情况，与消费者是否接收/确认消息无关

### 高级特性
    1. 消息的可靠传递
        Confirm 确认模式
        Return 退回模式

    2. Consumer Ack
        ack 指 Acknowledge，确认，表示消费端收到消息后的确认方式。
        有三种确认方式：
            1. 自动确认：acknowledge = ‘none’
            2. 手动确认：acknowledge = ‘manual’
            3. 根据异常情况确认：acknowledge = ‘auto’ (这种方式使用麻烦)
    
        自动确认，当消息被Consumer接收到，则自动确认收到，并将响应message从RabbitMQ的消息缓存中移除，
        但是在实际业务处理中，很可能消息接收到，业务处理出现异常，那么该消息就会丢失，如果设置了手动确认方式，
        则需要在业务处理成功后，调用channel.basicAck()，手动签收，如果出现异常，则调用
        channel.basicNack()方法，让其自动重新发送消息

    3. 消息可靠性：
        1. 持久化：
            1. exchange 要持久化
            2. queue 要持久化
            3. message 要持久化
        
        2. 生产方确认Confirm
        3. 消费方确认Ack
        4. Broker高可用

### RabbitMQ简单模式

### RabbitMQ队列模式
    轮询平均消费

### RabbitMQ发布订阅模式
    Fanout 广播，将消息交给所有绑定到交换机的队列
### RabbitMQ路由模式
    Direct 需要指定 RoutingKey
### RabbitMQ Topic主题模式
    Topic 通配符
### RabbitMQ 生产者消费者
### 防止消息丢失，死信队列
### 消息确认机制
    confirm 确认模式
        消息从生产者到交换机 有可能成功，有可能失败
        
    return 退回模式
        当消息到达不了队列的时候返回给生产者

### 削峰限流
### TTL
    TTL全称(Time To Live(存活时间/过期时间))
    当消息到达存活时间后，还没有被消费，会被自动清除
    RabbitMQ可以对消息设置过期时间,也可以对整个队列(Queue)设置过期时间
### 死信队列
    DLX：Dead Letter Exchange(死信交换机)，当消息成功Dead Message后，可以被重新发送到另一个交换机，这个交换机就是DLX
    
    1. 队列消息长度达到限制
    2. 消费者拒绝消费消息，basicNack/basicReject, 并且不把消息重新放入原目标队列，request = false;
    3. 原队列存在消息过期设置，消息到达超时时间未被消费;

    总结：
        1. 死信交换机和死信队列和普通的没有区别
        2. 当消息成为死信后，如果该队列绑定了死信交换机，则消息会被死信交换机重新路由到死信队列;
        3. 消息成为死信的三种情况：
            

### 延迟队列
    延迟队列，即消息进入队列后不会立即被消费，只有到达指定时间后，才会被消费
    TTL + 死信队列实现延时队列

### 消息积压
    1. 消费者宕机积压
    2. 消费者消费能力不足积压
    3. 发送者流量太大
    
    解决方案：    
        上线更多的消费者，进行正常消费上线专门的队列消费服务，将消息先批量取出来，
        记录数据库，再慢慢处理

### 消息幂等性保障
    幂等性指一次和多次请求某一个资源，对于资源本身应该有相同的结果，也就是说，其任意多次执行对资源
    本身所产生的影响均与一次执行的影响相同
    在MQ中指，消费多条相同的消息，得到与消费该消息一次相同的结果
    
    解决方案：
        乐观锁机制

### 集群
    集群模式
    镜像模式

### haproxy
    
### 防止消息丢失
    
### TTL + 死信队列 + 延时队列实现定时操作
    
### 防止消息重复消费，处理消息积压
    