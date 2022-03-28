### IO模型
    BIO & NIO & AIO
    BIO(Blocking IO):
        同步阻塞模型，一个客户端连接对应一个处理线程

        缺点:
            1. IO代码里read操作是阻塞操作，如果连接不做数据读写操作
                会导致线程阻塞，浪费资源
            2. 如果线程很多，会导致服务器线程太多，压力太大，比如C10K问题
        应用场景:
            BIO方式适用于连接数小且固定的架构，这种方式对服务器资源要求较高，
            但程序简单易理解
    NIO(Non Blocking IO):
        同步非阻塞，服务器实现模式为一个线程可以处理多个请求(连接),客户端发送的
        连接请求都会注册到多路复用器selector上，多路复用器轮询到连接有IO请求
        就进行处理，JDK1.4引入
        应用场景:
            NIO方式适用于连接数目较多且连接比较短(轻操作)架构，比如聊天服务器，
            弹幕系统，服务间通讯，编程比较复杂。
        
        NIO三大核心组件:  Channel(通道),Buffer(缓冲区),Selector(多路复用器)
        1. channel类似于流，每个channel对应一个buffer缓冲区，buffer底层就是个数组
        2. channel会注册到selector上,由selector根据channel读写事件的发生将其交由
            某个空闲的线程处理
        3. NIO的Buffer和channel都是既可以读也可以写

        NIO的底层在JDK1.4版本是用linux的内核函数select()或poll来实现，跟上面的NioServer
        代码类似，selector每次都会轮询所有的sockchannel看下哪个channel有读写事件，
        有的话就处理，没有就继续遍历，JDK1.5开始引入epoll基于事件响应机制优化NIO
        
    Selector.open() // 创建多路复用器
    socketChannel.register() // 将channel注册到多路复用器上
    selector.select() // 阻塞等待需要处理的事件发生

    NIO整个调用流程就是Java调用了操作系统的内核函数来创建Socket，获取到Socket的文件描述符，
    再创建一个Socket对象，对应操作系统的Epoll描述符，将获取到Socket连接的文件描述符的事件
    绑定到Socket对应的Epoll文件描述符上，进行事件的异步通知，这样就实现了使用一个线程，并且不需要
    太多的无效的遍历，将事件处理交给了操作系统内核(操作系统终端程序)，大大提高了效率。

    Epoll函数详解:
        1. int epoll_create(int size):
        创建一个epoll实例，并返回一个非负数做为文件描述符，用于对epoll接口的所有后续调用，参数size
        代表会容纳size个描述符，但size不是一个最大值，只是提示操作系统它的数量级，现在这个参数基本上
        弃用了
        2. int epoll_ctl(int epfd, int op, int fd, struct epoll_event *event)
        3. int epoll_wait(int epfd, struct epoll_event *event, int maxevents, int timeout)
        
    select：
        遍历，数组，每次调用都进行线性遍历，时间复杂度为O(n),有上限
    poll:
        遍历，链表，每次调用都进行线性遍历，时间复杂度为O(n),无上限
    epoll(jdk1.5以上):
        回调，哈希表，事件通知机制，每当有IO事件就绪，系统注册的回调函数
        就会被调用，时间复杂度为O(1)，无上限

    Redis就是典型的基于epoll的NIO线程模型(nginx也是),epoll实例收集所有事件(连接与读写事件),由一个服务端
    线程连接处理所有事件命令。

    AIO(NIO 2.0):
        异步非阻塞，由操作系统完成后回调通知服务端程序启动线程处理，一般适用于连接数较多且连接事件较长的应用
    
    BIO: 同步阻塞，编程简单，可靠性差，吞吐量低
    NIO: 同步非阻塞(多路复用),编程复杂,可靠性好,吞吐量高
    AIO: 异步非阻塞，编程复杂，可靠性好，吞吐量高
    

### Select Poll Epoll

### Netty线程模型
    1. Netty抽象出两组线程池BossGroup和WorkerGroup，BOSSGroup专门负责接收客户端的连接，
        WorkerGroup专门负责网路的读写
    2. BOSSGroup和WorkerGroup类型都是NioEventLoopGroup
    3. NioEventLootGroup相当于一个事件循环线程组，这个组中含有多个事件循环线程，每一个事件循环
        线程都是EventLoopGroup
    4. 每个NioEventLoop都有一个selector，用于监听注册在其上的socketChannel的网络通讯
    5. 每个Boss NioEventLoop线程内部循环执行的步骤有3步:
        a. 处理accept事件，与client建立连接，生成NioSocketChannel
        b. 将NioSocketChannel注册到某个Worker NioEventLoop上的Selector
        c. 处理任务队列的任务，即runAlltasks
    6. 每个worker NioEventLoop线程循环执行的步骤:
        a. 轮询注册到自己selector上的所有NioSocketChannel的read，write事件
        b. 处理I/O事件，即read，write事件，在对应NioSocketChannel处理业务
        c. runAllTasks处理任务队列TaskQueue的任务，一些耗时的业务处理一般可以放在TaskQueue中
            慢慢处理，这样不影响数据在pipeline中的流动处理
    7. 每个Worker NioEventLoop处理NioSocketChannel业务时，会使用pipeline，管道中维护了很多
        handler处理器用来处理channel中的数据

### Netty模块组件
    BootStrap，ServerBootStrap
        BootStrap意思是引导，一个Netty应用通常由一个BootStrap开始，主要作用是配置整个Netty程序，
        串联各个组件，Netty中BootStrap类是客户端程序的启动引导类，ServerBootStrap是服务端启动引导类
    Future，ChannelFuture
        Netty中所有的IO操作都是异步的，不能立刻得知消息是否被正确处理
        通过Future和ChannelFuture，注册一个监听，当操作执行成功或失败时监听会自动触发注册的监听事件
    Channel
        Netty网络通信的组件，能够用于执行网络I/O操作，Channel为用户提供:
        1. 当前网络连接的通道状态(例如是否打开?是否已经连接？)
        2. 网络连接的配置参数(例如接收缓冲区大小)
        3. 提供异步的网络I/O操作(如建立连接，读写，绑定端口)，异步调用意味着任何I/O调用都将立即返回，
            并且不保证在调用结束时所请求的I/O操作已完成
        4. 调用立即返回一个ChannelFuture实例，通过注册监听器到ChannelFuture上，可以I/O操作成功，
            失败或取消时通知调用方
        5. 支持关联I/O操作与对应的处理程序
    Selector
        Netty基于Selector对象实现I/O多路复用，通过Selector一个线程可以监听多个连接的Channel事件
        当向一个Selector中注册Channel后，Selector内部的机制就可以自动不断地查询(Select)这些注册的
        Channel是否有已就绪的I//O事件(例如可读，可写，网络连接完成等)，这样程序可以很简单地使用一个
        线程高效的管理多个Channel
    NioEventLoop
        NioEventLoop中维护了一个线程和任务队列，支持异步提交任务，线程启动时会调用NioEventLoop的run方法，
        执行I/O任务和非I/O任务
        I/O任务，即selectionKey中ready的事件，如accept，connect，read，write等，processSelectedKeys
        方法触发
        非I/O任务，添加到taskQueue中的任务，如register0，bind0等任务，有runAllTasks方法触发
    NioEventLoopGroup
        NioEventLoopGroup，主要管理eventLoop的生命周期，可以理解为一个线程池，内部维护了一个线程，
        每个线程(NioEventLoop)负责处理多个channel上的事件，而一个channel只对应于一个线程
    ChannelHandler
        ChannelHandler是一个接口，处理I/O事件或拦截I/O操作，并将其转发到其ChannelPipeline(业务处理链)
    ChannelHandlerContext
        保存Channel相关的上下文信息，同时关联一个ChannelHandler对象
    ChannelPipeline
        保存ChannelHandler的List，用于处理或拦截Channel的入站事件和出站操作
        ChannelPipeline实现了一种高级形式的拦截过滤器模式，使用可以完全控制事件的处理方式，以及Channel
        中各个的ChannelHandler如何相互交互
        在Netty中每个Channel都有且仅有一个ChannelPipeline与之对应，一个Channel包含了一个ChannelPipeline，
        而ChannelPipeline中又维护了一个由ChannelHandlerContext组成的双向链表，并且每个ChannelHandlerContext
        中又关联着一个ChannelHandler
        read事件(入站事件)和write事件(出站事件)在一个双线链表中，入站事件会从链表head往后传递到最后一个入站的
        handler，出站事件会从链表tail往前传递到最前一个出站header，两种类型的header互不干扰
    ByteBuf
        从结构上来说，ByteBuf由一串字节数组构成，数组中每个字节用来存放信息
        ByteBuf提供了两个索引，一个用于读取数据，一个用于写入数据，这两个索引通过在字节数组中移动，
        来定位需要读或者写信息的位置

### Netty编解码
    Netty涉及到编解码的组件有Channel，ChannelHandler，ChannelPipe等
    ChannelHandler
        ChannelHandler充当了处理入站和出站数据的应用程序逻辑容器，例如，实现ChannelInboundHandler接口
        (或ChannelInboundHandlerAdapter)，你就可以接收入站事件和数据，这些数据随后会被应用程序的业务
        逻辑处理，当要给客户端发送响应时，也可以从ChannelInboundHandler冲刷数据，业务逻辑通常写在一个或者
        多个ChannelInboundHandler中。ChannelOuntboundHandler原理一样，不过是用来处理出站数据的

    ChannelPipeline
        ChannelPipeline提供了ChannelHandler链的容器，以客户端应用程序为例，如果事件的运动方向是从客户端到
        服务端的，那么我们同城这些事件为出站，即客户端发送给服务端的数据会通过Pipeline中的一系列
        ChannelOutboundHandler(ChannelOutboundHanalder调用是从tail到head方向逐个调用每个Handler的逻辑)，
        并被这些handler处理，反之称为入站的，入站只调用Pipeline里的ChannelInboundHandler逻辑(ChannelInboundHandler
        调用是从head到tail方向逐个调用handler的逻辑)

    编码解码器
        入站消息会被解码，出站消息会被编码成字节

### Netty粘包拆包
    TCP是一个流协议，就是一个没有界限的一长串二进制数据，TCP作为传输层协议并不了解上层业务数据的具体含义，会根据TCO缓冲区
    的实际情况进行数据包的划分，所以在业务上认为是一个完整的包，可能会被拆分成多个包进行发送，也有可能把多个小的包封装成
    一个大的数据包发送，这就是所谓的TCP粘包和拆包问题。面向流的通信是无消息保护边界的
    
    解决方案:
        1. 消息定长度，传输的数据大小固定长度，例如每段长度固定为100字节，不够的空位补空格
        2. 在数据包尾部添加特殊分隔符
        3. 发送长度: 发送每条数据的时候，将数据长度一并发送

### Netty心跳检测机制
    所谓心跳，在TCP长连接中，客户端和服务端之间定期发送的一种特殊的数据包，通知对方自己还在线，以确保TCP连接的有效性，
    在Netty中，实现心跳机制的关键是IdleStateHandler

### Netty高并发高性能架构
    1. 主从Reactor线程模型
    2. NIO多路复用非阻塞
    3. 无锁串行化设计思想
    4. 支持高性能序列化协议
    5. 零拷贝(直接内存的使用)
    6. ByteBuf内存池设计
    7. 灵活的TCP参数配置能力
    8. 并发优化
    
    无锁串行化:
        高并发场景下，锁的竞争会导致性能的下降，为了尽可能的避免锁竞争带来的性能损耗，可以通过
        串行化设计，即消息的处理尽可能在同一个线程内完成，期间不进行线程切换，这样可以避免多线程
        竞争和同步锁。NIO的多路复用就是一种无锁串行化的设计(理解下Redis和Netty线程模型)。为了
        尽可能提升性能，Netty采用了串行无锁化设计，在IO线程内部进行串行化操作，避免多线程竞争
        导致的性能下降，表面上看，串行化设计似乎CPU利用率不高，并发程度不够，但是通过调整NIO线程池
        的线程参数，可以同时启动多个串行化线程并行运行，这种局部无锁化的串行线程设相比一个队列-多个
        工作线程模型性能更优

        Netty的NioEventLoop读取到消息之后，直接调用ChannelPipeline的fireChanelRead(Object msg)，
        只要用户不主动切换线程，一直会由NioEventLoop调用到用户的Handler，期间不进行线程切换，这种串行化
        处理方式避免了多线程操作导致的锁的竞争，从性能角度看是最优的

    直接内存:
        直接内存并不是虚拟机运行时数据区的一部分，也不是Java虚拟机规范中定义的内存区域，某些情况下这部分内存
        也会被频繁地使用，而且也可能导致OutOfMemoryError异常出现，Java里用DirectByteBuffer可以分配一块
        直接内存(堆外内存)。元空间对应的内存也叫做直接内存，它们对应的都是机器的物理内存

        直接内存申请较慢，访问效率高，在java虚拟机实现上，本地IO一般会直接操作直接内存(直接内存 -> 系统调用 -> 
        硬盘/网卡)，而非直接内存则需要二次考本(堆内存 -> 直接内存 -> 系统调用 -> 硬盘/网卡)

        使用直接内存的优缺点:
            优点:
                1. 不占用堆内存空间，减少了发生GC的可能
                2. java虚拟机实现上，本地IO会直接操作直接内存，而非直接内存则需要二次拷贝
            缺点:
                1. 初始化较慢
                2. 没有JVM直接帮助管理内存，容易发生内存溢出，为了避免一直没有FULL GC，最终导致直接内存
                    把物理内存耗完，我们可以指定直接内存的最大值，通过-XX: MaxDirectMemorySize来指定，
                    当达到阈值的时候，调用sysyem.gc来进行一次FULL GC,间接把那些没有被使用的直接内存
                    回收掉

    Netty零拷贝:
        Netty的接收和发送ByteBuf使用DIRECT BUFFERS，使用堆外直接内存进行Socket读写，不需要进行字节缓冲区
        的二次拷贝，如果使用传统的JVM堆内存(HEAP BUFFERS)进行Socket读写，JVM会将堆内存Buffer拷贝一份到直接
        内存中，然后才能写入Socket中，JVM堆内存的数据是不能直接写入Socket中的。相比于堆外直接内存，消息在发送
        过程中多了一次缓冲区的内存拷贝。

    ByteBuf内存池设计:
        随着JVM虚拟机和JIT即时编译技术的发展，对象的分配和回收是个非常轻量级的工作，但是对于缓冲区Buffer(相当于
        一个内存块)，情况却稍有不同，特别是对于堆外直接内存的分配和回收，是一件耗时的操作，为了尽量重用缓冲区，
        Netty提供了基于ByteBuf内存池的缓冲区重用机制，需要的时候直接从池子里获取ByteBuf使用即可，使用完毕之后
        就重新放回到池子里

    灵活的TCP参数配置能力:
        合理设置TCP在某些场景下对于性能的提升可以起到显著的效果，例如接收缓冲区SO_RCVBUF和发送缓冲区SO_SENDBUF。
        如果设置不当，对性能的影响非常大，通常建议值设置为128K或者256K
        
    并发优化:
        1. volatile的大量，正确使用
        2. CAS和原子类的广泛使用
        3. 线程安全容器的使用
        4. 通过读写锁提升并发性能
    
    ByteBuf扩容机制:
        Netty的ByteBuf需要动态扩容来满足需要，扩容过程:默认门限阈值为4MB(这个阈值是一个经验值，不同场景，可能取值
        不同)，当需要的容量等于门限阈值，使用阈值作为新的缓存去容量目标容量，如果大于阈值，采用每次步进4MB的方式进行
        内存扩张((需要扩容值/4MB) * 4MB)，扩张后需要和最大内存(maxCapacity)进行比较，大的话就用maxCapacity，
        否则使用扩容值目标容量，如果小于阈值，采用倍增的方式，以64(字节)作为基本数值，每次翻倍增长64 -> 128 -> 256。
        直到倍增后的结果大于等于需要的容量值

    Handler的生命周期回调接口调用顺序:
        handlerAdded -> channelRegistered -> channelActive -> channelRead -> channelReadComplete
        -> channelInactive -> channelUnRegistered -> handlerRemoved

        handlerAdded: 新建立的连接会按照初始化策略，把handler添加到该channel的pipeline里，也就是addLast执行完成
                        后的回调
        channelRegistered: 当该连接分配到具体的worker线程后，该回调会被调用
        channelActive: channel的准备工作已完成，所有的pipeline添加完成，并分配到具体的线程上，说明该channel准备就绪，
                        可以使用了
        channelRead: 客户端向服务端发送数据，每次都会回调该方法，表示有数据可读
        channelReadComlete: 服务端每次读完一次完整的数据之后，会调该方法，表示数据读取完毕
        channelInactive: 当连接断开时，该回调会被调用，说明这时底层的TCP连接已经断开了
        channelUnRegistered: 对应channelRegistered，当连接关闭后，释放绑定的worker线程
        handlerRemoved: 对应handlerAdded，将handler从该channel的pipeline移除后的回调方法
        
        