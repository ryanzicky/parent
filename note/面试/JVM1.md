### 1.  线程安全点

**线程安全点：safe point，JVM让线程停止。**

编译器认为无限循环，进行安全检测。

**线程安全点的检测发生在方法调用前。**

`-XX:-UseOnStackReplacement`：OSR；栈上替换

`-XX:+SafepointTimeout`

`-XX:SafepointTimeoutDelay=1000`

### 2. JVM内存模型，以及存放的内容

**运行时数据区：**

-   方法区（Method Area）：所有线程共享，线程不安全

-   堆（Heap）：所有线程共享，线程不安全

-   虚拟机栈（VM Stack）：线程独有

-   本地方法栈（Native Method Stack）：线程独有，C语言实现

-   程序计数器（Program Counter Register）：线程独有
    -   等价于IP寄存器（指令指针寄存器）：
    
    ES，EBS，

**汇编寄存器：**

**字节码文件（.class文件）：**

**类装载子系统：**

类加载器：

-   BootStrap类加载器：
-   Extension类加载器：
-   Application类加载器：
-   Customer自定义类加载器：

`loadClass，defineClass,findClass（默认抛出classNotFoundException）`

线程上下文类加载器，普通类加载器：线程上下文类加载器依赖于普通类加载器。

**父子双亲委派类加载器：**

1.  破坏双亲委派模型：
    1.  复写loadClass
    2.  用上下文类加载器
2.  自定义类加载器，满足双亲委派：
    1.  复写findClass
3.  热部署：
    1.  代理
    2.  tomcat热部署
    3.  Class.forName()
    4.  

连接：

-   验证：
-   准备：
-   解析：
-   初始化：

**Java执行引擎：**

1.  即时编译器（JIT Compier）

**编译原理：词法分析，语法分析，语义分析**

### 3. 堆内存划分的空间，如何回收这些内存对象，有哪些回收算法？

![堆内存空间划分](JVM1.assets/%E5%A0%86%E5%86%85%E5%AD%98%E7%A9%BA%E9%97%B4%E5%88%92%E5%88%86-1637897631462.png)

垃圾回收算法：标记清除，复制（多为新生代垃圾回收使用），标记整理。

### 4. 如何解决线上GC频繁问题

1.   查看监控，以了解出现问题的时间点以及当前FGC的频率（可对比正常情况看频率是否正常）
2.   了解该时间点之前有没有程序上线，基础组件升级等情况
3.   了解JVM的参数设置，包括：堆空间各个区域的大小设置，新生代和老年代分别采用了哪些垃圾收集器，然后分析JVM参数设置是否合理（jinfo）
4.   再对步骤1中列出的可能原因做排除法，其中元空间被打满，内存泄漏，代码显示地调用gc方法比较容易排查
5.   针对大对象或者长生命周期对象导致的FGC，可通过jmap-histo命令并结合dump堆内存文件作进一步分析，需要先定位到可疑对象
6.   通过可疑对象定位到具体代码再次分析，这时候要结合GC原来和JVM参数设置，弄清楚可疑对象是否满足了进入到老年代的条件才能下结论。

### 5. 描述class初始化过程

一个类初始化就是执行clinit()方法，过程如下：

-   父类初始化
-   static变量初始化/static块（按照文本顺序执行）

*Java Language Specification*中，类初始化详细过程如下（最重要的是类初始化是线程安全的）：

1.   每个类都有一个初始化锁LC，进程获取LC（如果没有获取到，就一直等待）
2.   如果C正在被其他线程初始化，释放LC并等待C初始化完成
3.   如果C正在被本线程初始化，即递归初始化，释放LC
4.   如果C已经被初始化了，释放LC
5.   如果C处于erronrous状态，释放LC并抛出异常NoClassDefFoundError
6.   否则，将C标记为正在被本线程初始化，释放LC；然后，初始化那些final且为基础类型的类成员变量
7.   初始化C的父类SC和各个接口SI_n（按照implements子句中的顺序来）；如果SC或SIn初始化过程中抛出异常，则获取LC，将C标记为erroneous，并通知所有线程，然后释放LC，然后再抛出同样的异常。
8.   从classLoader处获取assertion是否被打开
9.   接下来，按照文本顺序执行类变量初始化和静态代码块，或接口的字段初始化，把它们当作一个个单独的代码块
10.   如果执行正常，获取LC，标记C为已初始化，并通知所有线程，然后释放LC
11.   否则，如果抛出异常E，若不是Error，则以E为参数创建新的异常
12.   获取LC，将C标记为erroneous，通知所有等待的线程，释放LC，并抛出异常E。

### 6. 内存溢出的原因，如何排查线上问题

内存溢出的原因:

-   java.lang.OutOfMemoryError：......java heap space...... 堆栈溢出，代码问题的可能性很大
-   java.lang.OutOfMemoryError：GC over head limit exceeded 系统处于高频的GC状态，而且回收的效果依然不佳的情况，就会开始报这个错误，这种情况一般是产生了很多歌不可以被释放的对象，有可能是引用使用不当导致，或申请大对象导致，但是java heap space的内存溢出有可能提前不会报这个错误，也就是可能内存就直接不够导致，而不是高频GC
-   java.lang.OutOfMemoryError：PermGen space jdk1.7之前才会出现的问题，原因是系统代码非常多或引用的第三方包非常多，或到吗中使用了大量的常量，或通过intern注入常量，或者通过动态代码加载等方法，导致常量池的膨胀
-   java.lang.OutMemoryError：Direct buffer memory 直接内存不足，因为jvm垃圾回收不会回收掉直接内存这部分的内存，所以可能原因是直接或间接使用ByteBuffer中的allocateDirect方法的时候，而没有做clear
-   java.lang.StackOverflowError：-Xss设置得太小了
-   java.lang.OutOfMemoryError：unable to create new native thread 堆外内存不足，无法为线程分配内存区域
-   java.lang.OutOfMemoryError：request {} byte for {} out of swap 地址空间不够

### 7. jvm垃圾回收器，实际中如何选择？

![垃圾回收器](JVM1.assets/%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6%E5%99%A8.png)

解释几个名词：

1.   并行（Parallel）：多个垃圾收集线程并行工作，此时用户线程处于等待状态
2.   并发（Concurrent）：用户线程和垃圾收集线程同时执行
3.   吞吐量：运行用户代码时间/（运行用户代码时间+垃圾回收时间）

1.   **Serial收集器是最基本的，发展历史最悠久的收集器。**

     特点：单线程，简单高效（与其他收集器的单线程相比），对于限定单个CPU的环境来说，



### 8. Java类加载模型

1.   BootStrap ClassLoader（启动类加载器）
2.   Extension ClassLoader（扩展类加载器）
3.   Application ClassLoader（应用程序类加载器）
4.   User ClassLoader（自定义类加载器）

### 9.  



