### 常用MQ比较
    Kafka：
        优点：吞吐量大，性能非常好，集群高可用
        缺点：会丢数据，功能比较单一
        使用场景：日志分析，大数据采集
    
    RabbitMQ:
        优点：消息可靠性高，功能全面
        缺点：吞吐量低，消息积累会影响性能，erlang语言不好定制
        使用场景：小规模场景

    RocketMQ:
        优点：高吞吐，高性能，高可用，功能全面
        缺点：官方文档比较简单，客户端只支持java
        使用场景：几乎全场景

### 消息样例
    1. 消息样例
    2. 

### 生产者发送消息
    1. 同步发送
    2. 异步发送
    3. 单向发送

### 消费者消费消息
    1. 一种是消费者主动去Broker上拉去消息
    2. 由Broker收到消息后，主动推送给消费者

### 顺序消费
    MessageQueue 局部有序

### 延迟消息

### 批量消息
    
### 过滤消息

### 事务消息


### 基本概念
    1. 消息模型(Message Model)
        RocketMQ 主要由 Producer,Broker,Consumer 三部分组成，其中Producer负责
        生产消息，Consumer 负责消费消息，Broker负责存储消息。Broker在实际部署过程中
        对应一台服务器，每个Broker可以存储多个Topic的消息，每个Topic的消息可以分片
        存储于不同的Broker，Message Queue用于存储消息的物理地址，每个Topic中的消息
        地址存储于多个Mesaage Queue中，ConsumerGroup 由多个Consumer实例构成
    
    2. 消息生产者(Producer)
        负责生产消息，一般由业务系统负责生产消息，一个消息生产者会把业务应用系统里产生的消息
        发送到broker服务器，RocketMQ提供多种发送方式，同步发送，异步发送，顺序发送，单向发送，
        同步和异步方式均需要Broker返回确认信息，单向发送不需要
        
        生产者中，会把同一类Producer组成一个集合，叫做生产者组，这类Producer发送同一类消息
        且发送逻辑一致，如果发送的是事务消息且原始生产者在发送之后崩溃，则Broker服务器会联系
        同一生产者组的其他生产者实例以提交或回溯消费。

    3. 消息消费者(Consumer)
        负责消费消息，一般是后台系统负责异步消费，一个消费者会从Broker服务器拉取消息，并将
        其提供给应用程序，从用户应用的角度而言提供了两种消费形式: 拉取式消费，推动式消费
            1. 拉取式消费的应用通常主动调用Consumer的拉消息方法从Broker服务器拉消息，主动权
            由应用控制，一旦获取了批量消息，应用就会启动消费过程
            2. 推动式消费模式下Broker收到数据后会主动推送给消费端，该消费模式一般实时性较高
        消费者同样会把同一类Consumer组成一个集合，叫做消费者组，这类Consumer通常消费同一类
        消息且消费逻辑一致，消费者组使得在消息消费方面，实现负载均衡和容错的目标变得非常容易，
        要注意的是，消费者组的消费者实例必须订阅完全相同的Topic，RocketMQ支持两种消费模式：
        集群消费(Clustering) 和广播消费(Broadcasting)：
            1. 集群模式下，相同Consumer Group 的每个Consumer实例平均分摊消息
            2. 广播模式下，相同Consumer Group 的每个Consumer实例都接收全量的消息

    4. 主题(Topic)
        表示一类消息的集合，每个主题包含若干条消息，每条消息只能属于一个主题，是RocketMQ进行
        消息订阅的基本单位
        同一个Topic下的数据，会分片保存到不同的Broker上，而每一个分片单位，就叫做MessageQueue，
        MessageQueue是生产者发送消息与消费者消费消息的最小单位

    5. 代理服务器(Broker Server)
        消息中转角色，负责存储消息，转发消息。代理服务器在RocketMQ系统中负责接收从生产者发送来的
        消息并存储，同时为消费者的拉取请求做准备。代理服务器也存储消息相关的元数据，包括消费者组，
        消费进度偏移和主题和队列消息等。

        Broker Server 是RocketMQ真正的业务核心，包含了多个重要的子模块:
            1. Remoting Module: 整个Broker的实体，负责处理来自clients端的请求
            2. Client Manager: 负责管理客户端(Producer / Consumer)和维护Consumer的Topic订阅信息
            3. Store Service: 提供方便简单的API接口处理消息存储到物理硬盘和查询功能
            4. HA Service: 高可用服务，提供Mater Broker 和 Slave Brokr之间的数据同步功能
            5. Index Service: 根据特定的Message key投递到Broker的消息进行索引服务，以提供消息的快速查询
        而Broker Server要保证高可用需要搭建主从集群架构，RocketMQ中有两种Broker架构模式:
            1. 普通集群
            这种集群模式下会给每个节点分配一个固定的解决，master负责响应客户端的请求，并存储消息，slave则只负责对
            master的消息进行同步保存，并响应部分客户端的读请求，消息同步方式分为同步同步和异步同步
            这种集群模式下各个节点的角色无法进行切换，也就是说，master节点挂了，这一组Broker就不可用了
            
            2. Dledger高可用集群
            Dledger是RocketMQ自4.5版本引入的实现高可用集群的一项技术，这个模式下的集群会随机选出一个节点作为master，
            而当master节点挂了后，会从slave中自动选出一个节点升级成为master。
            Dledger技术做的事情: 
                1. 接管Broker的CommitLog消息存储
                2. 从集群中选举出master节点
                3. 完成master节点往salve节点的消息同步

    6. 名字服务(Name Server)
        名称服务冲淡消息提供者，Broker Server会在启动时向所有的NameServer注册自己的服务信息，
        并且后续通过心跳请求的方式保证这个服务信息的实时性，生产者或消费者能够通过名字服务查找
        各主题对应的Broker IP列表，多个NameServer实例组成集群，但相互独立，没有信息交换

    7. 消息(Message)

### 消息存储
    1. 何时存储消息
        分布式队列因为有高可靠性的要求，所以数据要进行持久化存储
        1. MQ收到一条消息后，需要向生产者返回一个ack响应，并将消息存储起来
        2. MQ PUSH 一条消息给消费者后，等待消费者的ack响应，需要将消息标记为已消费。
            如果没有标记为消费，MQ会不断的尝试网消费者推送这条消息
        3. MQ需要定期删除一些过期的消息，这样才能保证服务一直高可用

    2. 消息存储介质
        RocketMQ采用类似于Kafka的文件存储机制，即直接用磁盘文件来保存消息

        2.1 磁盘保存文件慢吗
            RocketMQ的消息用顺序写，保证了消息存储的速度
        2.2 零拷贝技术加速文件读写
            Linux操作系统分为（用户态）和（内核态），文件操作，网络操作需要涉及这两种心态的切换，
            免不了进行数据复制
            一台服务器，把本机磁盘文件的内容发送到客户端，一般分为两个步骤：
            1. read: 读取本地文件内容
            2. write: 将读取的内容通过网络发送出去
            这两个看似简单的操作，实际进行了4次数据复制，分别是:
                1. 从磁盘复制数据到内核态内存
                2. 从内核态内存复制到用户态内存
                3. 然后从用户态内存复制到网络驱动的内核态内存
                4. 最后是从网络驱动的内核态内存复制到网卡中进行传输
            而通过使用mmap的方式，可以省去向用户态的内存复制，提高速度。这种机制在Java中是通过NIO包中的
            MappedByteBufer实现的。RocketMQ充分利用了上述特性，也就是所谓的“零拷贝”技术，提高
            消息存盘和网络发送的速度。

    3. 消息存储结构
        RocketMQ消息的存储分为三个部分:
        1. CommitLog: 存储消息的元数据，所有消息都会顺序存入到CommitLog文件中，CommitLog由
            多个文件组成，每个文件固定大小1G，以第一条消息的偏移量为文件名
        2. ConsumerQueue: 存储消息在CommitLog的索引，一个MessageQueue一个文件，记录当前
            MessageQueue被那些消费者组消费到了哪一条CommitLog
        3. IndexFile: 为了消息查询提供了一种通过key或时间区间来查询消息的方法，这种通过IndexFile
            来查找消息的方法不影响发送与消费消息的主流程

    4. 刷盘机制
        RocketMQ为了提高性能，尽量保证磁盘的顺序写，消息在写入磁盘时，有两种写磁盘的方式
        同步是刷盘和异步输盘
        同步刷盘：
            在返回写成功状态时，消息已经被写入磁盘。具体流程是，消息写入内存的PAGECACHE后，
            立刻通知刷盘线程刷盘，然后等待刷盘完成，刷盘线程执行完成后唤醒等待的线程，返回消息写
            成功的状态。
        异步刷盘：
            在返回写成功状态时，消息可能只是被写入了内存的PAGECACHE，写操作的返回快，吞吐量大；
            当内存里的消息量积累到一定程度时，统一触发写磁盘动作，快速写入。

    5. 消息主从复制
        如果Broker以一个集群的方式部署，会有一个master节点和多个slave节点，消息需要从Master复制到
        Slave上，而消息复制的方式分为同步复制和异步复制
            1. 同步复制：
                同步复制是等master和slave都写入消息成功后才反馈给客户端写入成功的状态
            2. 异步复制：
                异步复制需要master写入消息成功，就反馈给客户端写入成功的状态，然后再异步
                的将消息复制给slave节点
    
    6.1. Producer负载均衡
        Producer发送消息时，默认会轮询目标Topic下的所有MessageQueue，并采用递增取模的方式往不同的
        MessageQueue上发送消息，以达到让消息平均落在不同的queue上的目的，而由于MessageQueue是分布
        在不同的Broker上的，所以消息也会发送到不同的broker上
        也可以制动一个MessageQueueSelector，通过这个对象将消息发送到自己指定的MessageQueue上，
        这样可以保证消息局部有序
        
    6.2. Consumer负载均衡
        Consumer也是以MessageQueue为单位来进行负载均衡的，分为集群模式和广播模式
        1. 集群模式
        2. 广播模式
        
    7. 消息重试
        广播模式，不存在消息重试机制，即消息消费失败后，不会重新进行发送，而只是继续消费新的消息
        而对于普通的消息，当消费者消费消息失败后，可以通过设置返回状态达到消息重试的结果
        1. 如何让消息进行重试
            1. 返回Action.ReconsumeLater - 推荐
            2. 返回null
            3. 抛出异常
        重试的消息会进入一个RetryComsumerGroup队列中
        RocketMQ默认允许每条消息最多重试16次
        如果消息重试16次后任然失败，消息将不再投递，转为进入死信队列
        一条消息无论重试多少次，这些重试消息的MessageId始终都是一样的

    8. 死信队列
        1. 一个死信队列对应一个ConsumerGroup，而不是对应某个消费者实例
        2. 如果一个ConsumerGroup没有产生死信队列，RocketMQ就不会为其创建相应的死信队列
        3. 一个死信队列包含了这个ConsumerGroup里的所有死信信息，而不区分该消息属于哪个Topic
        4. 死信队列中的消息不会再给消费者正常消费
        5. 死信队列的有效期跟正常消息相同，默认3天，对应broker.conf中的fileReservedTime属性，
            超过这个最长时间的消息都会被删除，而不管消息是否消费国
        
    9. 消息幂等
        1. 幂等的概念
        2. 消息幂等的必要性
        3. 处理方式
            
### Dledger 选举leader流程
    Dledger使用Raft协议进行leader选举
    初始化三个状态：Leader Follower Candidate
    以三个节点的集群为例，选举流程：
        1. 集群启动时，三个节点都是follower，发起投票，三个节点都会给自己投票，这样一轮投票下来，三个节点的term都是1，
            是一样的，这样是选举不出leader的
        2. 当一轮投票选举不出leader后，三个节点会进入随机休眠，例如A休眠1秒，B休眠3秒，C休眠2秒
        3. 1秒后，A节点醒来，会把自己的term加一票，投为2，然后2秒后，C节点醒来，发现A的term已经是2，比自己的1大，
            就会承认A是leader，把自己的term也更新为2，实际上这个时候，A已经获得了集群中的多数票，2票，A就会被选举为leader。
            这样，一般经过很短的几轮选举，就会选举出一个leader来
        4. 到3秒时，B节点会醒来，他也同样会承认A的term最大，他是leader，自己的term也会更新为2，这样集群中的所有Candidate
            就都确认成了leader和follower
        5. 然后在一个任期内，A会不断发心跳给另外两个节点，当A挂了后，另外的节点没有收到A的心跳，就会都转化成Candidate状态，
            重新发起选举

    Dledger还会采用Raft协议进行多副本的消息同步：
        简单来说，数据同步会通过两个阶段，一个是uncommitted阶段，一个commited阶段
        Leader Broker上的Dledger收到一条数据后，会标记为uncommitted状态，然后他通过自己的DledgerServer组件
        把这个uncommitted数据发给follower broker的DledgerServer组件
        接着Follower Broker的DledgerServer收到uncommitted消息之后，必须返回给一个ack给Leader Broker的Dledger，
        然后如果Leader Broker收到半数的Follower Broker返回的ack之后，就会把消息标记为commited状态
        在接下来，Leader Broker上的dledgerServer会发送commited消息给followe broker的dledgerServer，让他们把
        消息也标记为commited状态。这样，就基于raft协议完成了两阶段的数据同步

### 全链路消息零丢失
    1. half消息写入失败
    2. 订单系统写数据库失败
    3. half消息写入成功后RocketMQ挂了
        保留订单状态，等MQ系统恢复后再更新状态
    4. 下单成功后图和优雅的等待支付成功
        1. 不用事务消息
            启动一个定时任务，每隔一段时间扫描订单表，比对未支付的订单下单时间，将超过时间的订单回收
            使用RocketMQ的延迟消息机制，往MQ发一个延迟1分钟的消息，消费到这个消息后去检查订单的支付状态
            如果订单已支付，就往下游发送下单通知，如果没有支付，就再发一个延迟1分钟的消息
        2. 使用事务消息
            用事务消息的状态回查机制替代定时任务，再下单时给Broker返回一个unknown的未知状态，
            在状态回查的方法中区查询订单的支付状态
    5. 事务消息机制的作用
        RocketMQ的事务消息机制，保证了整个事务消息的一半，他保证的是订单系统下单和发消息这两个事务的一致性，
        对下游服务的事务没有保证，是分布式事务的一个很好的降级方案

    RocketMQ消息零丢失总结：
        1. 生产者使用事务消息机制
        2. Broker配置同步刷盘+Dledger主从架构
        3. 消费者不要使用异步消费
        4. 整个MQ挂了之后准备降级方案
    降低了系统的处理性能以及吞吐量。
    采用定时对账，补偿的机制来提高消息的可靠性，如果消费者不需要进行消息存盘，使用异步消费的机制带来性能提升
    
    
### RocketMQ配置同步刷盘+Dledger主从架构保证MQ自身不会丢消息
    1. 同步刷盘
    2. Dledger的文件同步
        在Dledger集群中，Dledger会通过两阶段提交的方式保证文件在主从之间成功同步
        数据同步会通过两个阶段，一个uncommitted阶段，一个commited阶段
        基于raft的两阶段数据同步
    
### RocketMQ特有的问题，NameServer挂了如何保证消息不丢失
    NameServer在RocketMQ中，是扮演一个路由中心的角色，提供到Broker的路由功能
    Kafka使用zookeeper和一个作为Controller的Broler一起来提供路由服务
    
    NameServer挂了后，生产者和消费者都无法工作
    消息不丢失：
        将订单信息缓存在Redis，文件或者内存中，然后起一个线程定时的扫描这些失败的订单消息，
        尝试往RocketMQ发送，这样等RocketMQ的服务恢复之后，就能第一时间把消息发送出去。
        服务降级
    
    
### 使用RocketMQ保证消息顺序性
    1. 为什么要保证消息有序
        消息消费不能乱序场景
    2. 如何保证消息有序
        MQ的顺序问题分为全局有序和局部有序
        1. 全局有序：整个MQ系统的所有消息严格按照队列先入先出顺序进行消费
        2. 局部有序：只保证一部分关键消息的消费顺序
        
    通常情况下，发送者发送消息时，会通过MessageQueue轮的方式保证消息尽量均匀分不到所有的MessageQueue上，
    而消费者也就同样需要从多个MessageQueue上消费消息，而MessageQueue是RocketMQ存储消息的最小单元，
    它们之间的消息都是互相隔离的，这种情况下，无法保证消息全局有序

    局部有序，只需要将有序的一组消息都存入同一个MessageQueue里，这样MessageQueue的FIFO设计可以保证这一组消息的有序
    保证Topic全局消息有序的方式，就是将Topic配置成只有一个MessageQueue队列，这样天生就能保证消息全局有序了

### 使用RocketMQ如何快速处理积压消息
    1. 如何确定RocketMQ有大量的消息积压
    2. 如何处理大量积压的消息
        如果Topic下的MessageQueue配置是足够多的，那么每个Consumer实际上会分配多个MessageQueue来进行消费
        这个时候，可以简单通过增加Consumer的服务节点数量来加快消息的消费，等积压消息消费完了，再恢复成正常情况。
        极限的情况就是吧Consumer的节点个数设置成跟MessageQueue的个数相同，这样增加Consumer的服务节点没用

        如果Topic的MessageQueue配置不够多，可以通过增加Topic的方式，配置足够多的MessageQueue，然后把
        所有消费者节点的目标Topic转向新的Topic，并紧急上线一组新的消费者，只负责消费旧Topic中的消息，并
        存储到新的Topic中，这个速度是可以很快的，然后在新的Topic上，就可以通过增加消费者个数来提高消费速度了
        之后在根据情况恢复成正常情况
    