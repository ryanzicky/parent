### Kafka
    Kafka是一个分布式，支持分区的(partition),多副本的(replica),基于zookeeper协议的分布式
    消息系统，它的最大特性是可以实时的处理大量数据以满足各种需求场景：比如基于hadoop的批处理系统，
    低延迟的实时系统，Storm/Spark流式处理引擎，web/nginx日志，访问日志，消息服务等，用scala
    语言编写

    Kafka基本概念：
        Broker: 
            消息中间件处理节点，一个Kafka节点就是一个Broker，一个或者多个Broker可以组成一个集群
        Topic:
            Kafka根据Topic对基本消息进行归类，发布到Kafka集群的每条消息都需要制定一个Topic
        Producer:
            生产者，向Broker发送消息的客户端
        Consumer:
            消费者，从Broker读取消息的客户端
        ConsumerGroup:
            每个Consumer属于一个特定的ConsumerGroup，一条消息可以被多个不同的ConsumerGroup消费，
            但是一个ConsumerGroup中只能有一个Consumer消费该消息
        Partition:
            物理上的概念，一个Topic可以分为多个Partiotion，每个Partiotion内部是有序的
    从一个较高的层面来看，Producer通过网络发送消息到Kafka集群，然后Consumer来进行消费

    服务端(Brokers)和客户端(Producer，Consumer)之间通信通过TCP协议来完成

### 使用场景
    1. 日志收集:
        一个公司可以用Kafka收集各种服务的log，通过Kafka以统一接口服务的方式开放给各种consumer，
        例如hadoop，Hbase，Solr等
    2. 消息系统:
        解耦生产者和消费者，缓存消息等
    3. 用户活动跟踪:
        Kafka经常用来记录web用户或者app用户的各种活动，如浏览网页，搜索，点击等活动，这些活动信息被
        各个服务器分布到kafka的topic中，然后订阅者通过订阅这些topic来做实时的监控分析，或者装载到
        hadoop，数据仓库中做离线分析和挖掘
    4. 运营指标:
        kafka也经常用来记录运营监控数，包括收集各种分布式应用的数据，生产各种操作的集中反馈，比如
        报警和报告

### 消费模式
    1. 单播消费
        一条消息只能被某一个消费者消费的模式，类似queue模式，只需让所有消费者在一个消费者组里即可
    2. 多播消费
        一条消息能被多个消费者消费的模式，类似publish-subscribe模式，针对kafka同一条消息只能
        被同一个消费组下的某一个消费者消费的特性，要实现多播只要保证这些消费者属于不同的消费组即可。
    
    消费者从Broker poll消息

### 主题Topic和消息日志Log
    可以理解Topic是一个类别的名称，同类消息发送到同一个Topic下面，对于每一个Topic，下面可以用
    多个分区(Partition)日志文件
    Partition是一个有序的message序列，这些message按顺序添加到一个叫做commit log的文件中，
    每个Partition中的message的offset可能是相同的
    Kafka一般不会删除消息，不管这些消息有没有被消费，只会根据配置的日志保留时间(log.retention.hours)
    确认消息多久被删除，默认保留最近一周的日志消息，kafka的性能与保留的消息数据量大小没有关系，因此保存
    大量的数据消息日志信息不会有什么影响
    每个Consumer是基于自己在CommitLog中的消费进度(offset)来进行工作的，在Kafka中，消费offset由
    Consumer自己来维护，一般情况下我们按照顺序逐条消费CommitLog中的消息，当然我们可以通过指定offset
    来重复消费某些消息，或者跳过某些消息
    这意味着kafka中的consumer对集群的影响是非常小的，添加一个或者减少一个consumer，对于集群或者其他
    consumer来说，都是没有影响的，因为每个consumer维护各自的消费offset

### 角色作用
    1. Leader节点负责给定Partition的所有读写请求
    2. Replicas表示某个Partition在哪几个Broker上存在备份，不管这个节点是不是“leader”，
        甚至这个节点挂了，也会列出
    3. isr是replicas的一个子集，它只列出当前还活着的，并且已同步备份了该Partition的节点

### 可以这样来理解Topic，Partition和Broker
    一个Topic，代表逻辑上的一个业务数据集，比如按数据库里不同表的数据操作消息区分放入不同的Topic，
    订单相关操作消息放入订单Topic，用户相关操作消息放入用户Topic，对于大型网站来说，后端数据都是
    海量的，订单消息很可能是非常巨量的，比如有几百个G甚至达到TB级别，如果把这么多数据都放在一台
    机器上肯定会有容量限制问题，那么就可以在Topic内部划分多个Partition来分片存储数据，不同的Partition
    可以定位与不同的机器上，每台机器上都运行一个Kafka的Broker进程

    *为什么要对Topic下数据进行分区存储？*
    1. CommitLog文件会受到所在机器的文件系统大小的限制，分区之后可以将不同的分区放在不同的机器上，
    相当于对数据做了分布式存储，理论上一个Topic可以处理任意数量的数据
    2. 为了提高并行度
    
### Kafka集群
    log的Partitions分布在Kafka集群中不同的Broker上，每个Broker可以请求备份其他Broker上Partition
    上的数据，Kafka集群支持配置一个Partition备份的数量
    针对每个Patition，都有一个Broker起到"leader"的作用，0个或多个其他的Broker作为"followers"的作用
    Leader处理所有的针对这个Partition的读写请求，而followers被动复制leader的结果，不提供读写(主要是为了
    保证多副本数据与消费的一致性)。如果这个leader失效了，其中的一个follower将会自动的变成新的leader

    Producers
        生产者将消息发送到topic中去，同时负责选择将message发送到topic的哪一个Partition中，通过round-robin
        做简单的负载均衡，也可以根据消息中的某一个关键字来进行区分，通常第二种方式使用得更多

    Consumer
        传统的消息传递模式有2种: 队列(queue)和(publish-subscribe)
        1. queue模式: 多个consumer从服务器中读取数据，消息只会到底一个consumer
        2. publish-subscribe模式: 消息会被广播给所有的consumer

        Kafka基于这2种模式提供了一种consumer的抽象概念: consumer group
        1. queue模式: 所有的consumer都位于同一个consumer group下
        2. publish-subscribe模式: 所有的consumer都有着自己唯一的consumer group

        通常一个topic会有几个consumer group，每个consumer group都是一个逻辑上的订阅者(logical subscribe)
        每个consumer group 由多个consumer instance组成，从而达到可扩展和容灾的功能

    消费顺序:
        一个Partition同一时刻在一个consumer group中只能有一个consumer instance在消费，从而保证消费顺序
        consumer group中的consumer instance的数量不能比一个topic中的partition的数量多，否则，多出来的
        consumer 消费不到消息

        kafka只在partition的范围内保证消息消费的局部有序，不能在同一个topic中的多个partition中保证总的消费有序
        如果有在总体上保证消费有序的需求，那么我们可以通过将topic的partition数量设置为1，将consumer group中的
        consumer instance数量也设置为1，但是这样会影响性能，所有kafka的顺序消费很少用
    
### Kafka发送消费核心参数详解

### Kafka核心总控器原理剖析

### Kafka副本选举leader机制详解

### Kafka消费Rebalance机制详解

### Kafka日志存储及索引原理详解

### Kakfa原理
    Kafka核心总控制器Controller
    在Kafka集群中会有一个或者多个Broker，其中有一个Broker会被选举为控制器(Kafka Controller),它负责管理整个集群
    中所有分区和副本的状态
    1. 当某个分区的Leader副本出现故障时，由控制器负责为该分区选举新的Leader副本
    2. 当检测到某个分区的ISR集合发生变化时，由控制器负责通知所有Broker更新其元数据信息
    3. 当使用Kafka-topics.sh脚本为某个topic增加分区数量时，同样还是由控制器负责让新分区被其他节点感知到

    Controller选举机制
    在Kafak集群启动的时候，会自动选举一台Broker作为Controller来管理整个集群，选举的过程是集群中每个Broker都会尝试
    在Zookeeper上创建一个/Controller临时节点，zookeeper会保证有且仅有一个broker能创建成功，这个broker就会
    成为集群的总控制器controller
    当这个controller角色的broker宕机了，此时zookeeper临时节点会消失，集群里其他broker会一直监听这个临时节点，
    发现临时节点消失了，就竞争再次创建临时节点，就是我们上面说的选举机制，zookeeper又会保证只有一个broker成为新的
    controller
    具备控制器身份的broker需要比其他普通的broker多一份职责，具体细节如下:
    1. 监听broker相关的变化，为Zookeeper中的/brokers/ids/节点添加BrokerChangeListener，用来处理broker增减的变化
    2. 监听topic相关的变化，为zookeeper中的/brokers/topics/节点添加TopicChangeListener，用来处理topic增减的变化
        为zookeeper中的/admin/delete_topics节点添加TopicDeletionListener，用来处理删除topic的动作
    3. 从zookeeper中读取当前所有与topic，partition以及broker有关的信息并进行相应的管理，对于所有topic所对应的Zookeeper
        中的/brokers/topics/[topic]节点添加PartitionModificationsListener，用来监听topic中的分区分配变化
    4. 更新集群的元数据信息，同步到其他普通的broker节点中

### Partition副本选举leader机制
    controller感知到分区leader所在的broker挂了(controller监听了很多zk节点可以感知到broker存活)，controller会从
    ISR列表里挑第一个broker作为leader(第一个broker最先放进ISR列表，可能是同步数据最多的副本)
    副本进入ISR列表有两个条件:
        1. 副本节点不能产生分区，必须能与zookeeper保持会话以及跟leader副本网络连通
        2. 副本能复制leader上的所有写操作，并且不能落后太多

### 消费者消费消息的offset记录机制
    每个consumer会定期将自己消费分区的offset提交给kafka内部topic:_consumer_offsets，提交过去的时候，key是
    consumerGroup+topic+分区号，value就是当前offset的值，kafka会定期清理topic里的消息，最后就保留最新的
    那条数据
    因为_consumer_offsets可能会接收高并发的请求，kafka默认给其分配50个分区，这样可以通过加机器的方式抗大并发

### 消费者Rebalance机制
    rebalance就是说如果消费组里的消费者数量有变化或消费的分区数有变化，kafka会重新分配消费者消费分区的关系，
    比如consumer group中某个消费者挂了，此时会自动把分配给求他的分区交给其它的消费者，如果他又重启了，那么又会把
    一些分区重新交换给他
    注意: rebalance只针对subscribe这种不指定分区消费的情况，如果通过assign这种消费方式指定了分区，kafka不会
    进行rabalance
    如下情况可能会触发消费者rebalance
        1. 消费组里的consumer增加或减少了
        2. 动态给topic增加了分区
        3. 消费组订阅了更多的topic
    rebalance过程中，消费者无法从kafka消费消息，这对kafka的TPS会有影响，如果kafka集群内节点较多，
    比如数百个，那重平衡可能会耗时极多，所以应尽量避免在系统高峰期的重平衡发生

### Rebalance过程如下
    第一阶段：
    1. 选择组协调器
    组协调器GroupCoordinator: 每个consumer group都会选择一个broker作为自己的租协调器coordinator，
    负责监控这个消费组里的所有消费者的心跳，以及判断是否宕机，然后开启消费者rabalance
    consumer group中的每个consumer启动时会向kafka集群中的某个节点发送findCoordinatorRequest请求来查找
    对应的组协调器GroupCoordinator,并跟其建立网络连接
    2. 组协调器选择方式:
    通过如下方式可以选出consumer消费的offset要提交到_consumer_offset的哪个分区，这个分区leader对应的broker
    就是这个consumer group的coordinator
    公式: hash(consumer group id) % _consumer_offsets主题的分区数
    第二阶段:
    1. 加入消费组join_group
    在成功找到消费组所对应的GroupCoordinator之后就进入加入消费组的阶段，在此阶段的消费者会向
    GroupCoordinator发送JoinGroupRequest请求，并处理响应，然后GroupCoordinator从一个consumer group
    中选择第一个加入group的consumer作为leader(消费组协调器)，把consumer group情况发送给这个leader，接着
    这个leader会负责制定分区方案
    第三阶段(sync group):
    consumer leader通过给GroupCoordinator发送SyncGroupRequest，接着GroupCoordinator就把分区方案下发
    给各个consumer，他们会根据指定分区的leader broker进行网络连接以及消息消费
    
    消费者Rebalance分区分配策略:
    主要有三种rebalance策略: range,round-robin,sticky
    kafka提供了消费者客户端参数partition.assignment.strategy来设置消费者与订阅主题之间的分区分配策略，
    默认情况为range分配策略
    假设一个主题有10个分区(0-9)，现在有三个consumer消费者:
    range策略就是按照分区序号排序，假设n=分区数/消费者数量=3，m=分区数%消费者数量=1。那么前n个消费者每个分配
    n + 1个分区，后面的(消费者数量-m)个消费者每个分配n个分区
    比如分区0-3给一个consumer，分区4-6给一个consumer，分区7-9给一个consumer
    round-robin策略就是轮询分配，比如分区0，3，6，9给一个consume，分区1，4，7给一个consumer，分区2，5，8
    给一个consumer
    sticky策略初始时分配策略与round-robin类似，就是在rebalance的时候，需要保证如下两个原则
    1. 分区的分配要尽可能均匀
    2. 分区的分配尽可能与上次分配的保持相同
    当两者发生冲突时，第一个目标优先于第二个目标，这样可以最大程度维持原来的分区分配的策略
    比如对于第一种range情况的分配，如果第三个consumer挂了，那么重新用sticky策略分配的结果如下:
    consumer1除了原有的0-3，会再分一个7
    consumer2除了原有的4-6，会再分配8和9

### pubducer发布消息机制剖析
    1. 写入方式
    producer采用push模式将消息发布到broker，每条消息都被append到partition中，属于顺序写磁盘(
    顺序写磁盘效率比随机写内存要高，保证kafka吞吐率)
    2. 消息路由
    producer发送消息到broker时，会根据分区算法选择将其存储到哪一个partition，其路由机制为:
        1. 指定了partition，则直接使用
        2. 未指定partition但指定了key，通过对key的value进行hash选出一个partition
        3. partition和key都未指定，使用轮询选出一个partition
    
    3. 写入流程:
        1. producer先从zookeeper的"/brokers/.../state"节点找到该partition的leader
        2. producer将消息发送给该leader
        3. leader将消息写入本地log
        4. followers从leader pull消息，写入本地log后向leader发送ack
        5. leader收到所有ISR中的replica的ack后，增加HW(high watermark,最后commit的offser)
            并向producer发送ack
    
    HW与LEO详解
    HW俗称高水位，HighWatermark的缩写，取一个partition对应的ISR中最小的LEO(log-end-offset)作为HW，
    consumer最多只能消费到HW所在的位置，另外每个replica都有HW，leader和follower各自负责更新自己的HW的状态，
    对于leader新写入的消息，consumer不能立刻消费，leader会等待该消息被所有ISR中的replicas同步后更新HW，此时
    消息才能被consumer消费，这样保证了如果leader所在的broker失效，该消息任然可以从新选举的leader中获取，
    对于来自broker的读取请求，没有HW的限制
    
### 日志分段存储
    Kafka一个分区的消息数据对应存储在一个文件夹下，以topic名称+分区号命名，消息在分区内是分段(segment)存储，
    每个段的消息都存储在不一样的log文件里，这种特性方便old segment file快速被删除，kafka规定了一个段位的log
    文件最大为1G，做这个限制的目的是为了方便把log文件加载到内存去操作：
        · 部分消息的offser索引文件，kafka每次往分区发4K(可配置)消息就会记录一条当前消息的offset到index文件
        · 如果要定位消息的offset会现在这个文件里快速定位，再去log文件里找具体消息
        00000000000000000000.index
        · 消息存储文件，主要存offset和消息体
        00000000000000000000.log
        · 消息的发送时间索引文件，kafka每次往分区发4K(可配置)消息就会记录一条当前消息的发送时间戳与对应的offset
        到timeindex文件
        · 如果需要按照时间来定位消息的offset，会先在这个文件里查找
        00000000000000000000.timeindex

        # 以第一行偏移量命名文件
        00000000000005367851.index
        00000000000005367851.log
        00000000000005367851.timeindex

        00000000000009936472.index
        00000000000009936472.log
        00000000000009936472.timeindex
    这个99365472之类的数字，就是代表了这个日志段文件里包含的其实Offset，也就说明这个分区至少都写入了
    近1000万条数据了
    Kafka Broker有一个参数，log.segment.bytes 限定了每个日志段文件的大小，最大就是1G
    一个日志段文件满了，就自动开一个新的日志段文件来写入，避免单个文件过大，影响文件的读写性能，
    这个过程叫log rolling，正在被写入的那个日志段文件，叫做active log segment

### JVM参数设置
    Kafka是scala语言开发的，运行在JVM上，需要对JVM参数进行合理设置
    大内存情况一般使用G1垃圾收集器，因为年轻代内存比较大，用G1可以设置GC最大停顿时间，
    不至于minor gc就花费太长时间，当然，因为像kafka，rocketMQ，es这些中间件，
    写数据到磁盘会用到系统的page cache，所以JVM内存不宜分配过大，需要给操作系统的
    缓存流出几个G

### 线上问题及优化
    1. 消息丢失情况:
        消息发送端:
            1. acks = 0: 表示producer不需要等待任何broker确认收到消息的回复，就可以继续发送下一条消息，
                性能最高，但是最容易丢失消息。大数据统计场景，对性能要求高，对数据丢失不敏感的情况可以使用这种
            2. acks = 1: 至少要等待leader已经成功将数据写入本地log，但是不需要等待所有follower是否成功写入，
                就可以继续发送下一条消息，这种情况下，如果follower没有成功备份数据，而此时leader挂掉，则消息会丢失
            3. acks = -1或all: 这意味着leader需要等待所有备份(min.insync.replicas配置的备份个数)都成功写入日志，
                这种策略会保证只要有一个备份存活就不会丢失数据，这是最强的数据保证，一般除非是金融级别，或跟钱打交道的
                场景才会使用这种配置，当然如果min.insync.replicas配置的是1则也可能丢失消息，跟acks=1情况类似
        
        消息消费端:
            如果消息这边配置的是自动提交，万一消费到数据还没处理完，就自动提交offset了，但是此时consumer宕机了，
            未处理完的数据丢失了，下次也消费不到了
    
    2. 消息重复消费:
        消息发送端:  
            发送消息如果配置了重试机制，比如网络抖动时间过长导致发送端发送超时，实际broker可能已经接收到消息，
            但发送方会重新发送消息
        消息消费端:
            如果消费端配置的是自动提交，则刚拉取了一批数据处理了一部分，但没来得及提交，服务挂了，下次重启又会拉取
            相同的一批数据重复处理
            一般消费端要做幂等处理
    
    3. 消息乱序:
        如果发送端配置了重试机制，Kafka不会等之前那条消息完全发送成功才去发送下一条消息，这样就可能出现，发送了
        1，2，3条消息，第一条发送超时了，后面两条发送成功了，再重试发送第1条消息，这时消息在broker端的顺序就是
        2，3，1了。
        所以，是否一定要配置重试要根据业务情况而定，也可以用同步发送的模式去发消息，当然acks不能设置为0，这样也
        能保证消息从发送端到消费端全链路有序
        Kafka保证全链路消息顺序消费，需要从发送端开始，将所有有序消息发送到同一个分区，然后用一个消费者去消费，
        但是这种性能比较低，可以在消费者接收到消息后将需要保证顺序消费的几条消费放到内内列(可以多搞几个),一个内存
        队列去开启一个线程顺序处理消息。
        
    4. 消息积压:
        1. 线上有时因为发送方发送消息速度过快，或者消费方消费速度过慢，可能会导致broker积压大量未消费消息
            此种情况如果积压了上百万条未消费消息需要紧急处理，可以设置消费端程序，让其将收到的消息快速转到
            其他topic(可以设置很多个分区)，然后再启动多个消费者同时消费新主题的不同分区。
        2. 由于消息数据格式变动或消费者程序有bug，导致消费者一直消费不成功，也可能导致broker积压大量未消费
            消息。此种情况可以将这些消费不成功的消息转发到其他队列里去(类似死信队列),后面再慢慢分析死信队列
            里的消息处理问题

    5. 延时队列:
        延时队存储的对象是延时消息，所有的"延时消息"是指消息被发送之后，并不想让消费者立刻获取，而是等待特性的时间后，
        消费者才能获取这个消息进行消费，延时队列的使用场景有很多，比如:
        1. 在订单系统中，一个用户下单之后通常有30分钟时间进行支付，如果30分钟之内没有支付成功，那么这个订单将
        进行异常处理，这时就可以使用延时队列来处理这些订单了
        2. 订单完成1小时候通知用户来进行评价
        
        实现思路:
            发送延时消息时先把消息按照不同的延迟时间发送到指定的队列中(topic_1s,topic_5s,topic_10s,...topic_2h,
            这个一般不能支持任意时间段的延时)，然后通过定时器进行轮询来消费这些topic，查看消息是否过期，如果到期就把
            这个消息发送到具体业务处理的topic中，队列中消息越靠前的到期时间就越早，具体来说就是定时器再一次消费过程中，
            对消息的发送时间做判断，看下是否延迟到对应时间了，如果到了就转发，如果没到这一次定时任务就可以提前结束了

    6. 消息回溯:
        如果某段时间对已消费消息计算的结果有问题，可能是程序员bug导致的计算错误，当程序bug修复后，这时可能需要对之前
        已消费的消息重新消费，可以指定从多久之前的消息回溯消费，这种可以用consumer的offsetsForTimes，seek等方法
        指定从某个offset偏移的消息开始消费    
    
    7. 分区数越多吞吐量越高吗
        压测确定分区数
        
    
    
