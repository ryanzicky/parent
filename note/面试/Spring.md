### 1. 如何理解Spring Boot中的Starter

使用Spring + SpringMvc使用，如果需要引入mybatis等框架，需要到xml中定义mybatis需要的bean

starter就是定义一个starter的jar包，写一个@Configuration配置类，将这些bean定义在里面，然后在starter包的META-INF/spring.factories中写入该配置类，springboot会按照约定来加载配置类

开发人员只需要将相应的starter包依赖进应用，进行相应的属性配置(使用默认配置时，不需要配置)，就可以直接进行代码开发，使用对应的功能

### 2. 如何实现一个IOC容器

1.  配置文件配置包扫描路径
2.  递归包扫描获取.class文件
3.  反射，确定需要交给IOC管理的类
4.  对需要注入的类进行依赖注入

-   配置文件中指定需要扫描的包路径
-   定义一些注解，分别表示访问控制层，业务服务层，数据持久层，依赖注入注解，获取配置文件注解
-   从配置文件中获取需要扫描的包路径，获取到当前路径下的文件信息及文件夹信息，我们将当前路径下所有以.class结尾的文件添加到一个Set集合中进行存储
-   遍历这个set集合，获取在类上有指定注解的类，并将其交给IOC容器，定义一个安全的Map用来存储这些对象
-   遍历这个IOC容器，获取到每一个类的实例，判断里面是否有依赖其他的类的实例，然后进行注入

### 3. Spring中的Bean是线程安全的吗

Spring本身并没有针对Bean做线程安全的处理，所以：

	1. 如果Bean是无状态的，那么Bean则是线程安全的
	2. 如果Bean是有状态的，那么Bean是线程不安全的

另外，Bean是不是线程安全，跟Bean的作用域没有关系，Bean的作用域只是表示Bean的生命周期范围，对于任何什么周期的Bean都是一个对象，这个对象是不是线程安全的，还是得看这个Bean对象本身

### 4. 对IOC的理解

容器概念，控制反转，依赖注入

-   ioc容器：实际上就是个map，里面存的是各种对象，在项目启动的时候会读取配置文件里面的bean节点，根据全限定名使用反射对象放到map里，扫毛到打上上述注解的类还是通过反射创建对象放到map里
-   控制反转：没用引入ioc容器之前，需要主动创建对象；引入ioc容器后，ioc容器会主动创建需要用到的对象
-   依赖注入：获得依赖对象的过程被反转了。控制被反转之后，获得依赖对象的过程由自身管理变为了由IOC容器主动注入，依赖注入是实现IOC的方法，就是由IOC容器在运行期间，动态地将某种依赖关系注入到对象之中

### 5. Spring大致流程

1.  Spring是一个快速开发的框架，Spring帮助来管理对象
2.  Spring的源码实现是每场优秀的，设计模式的应用，并发安全的实现，面向接口的设计等
3.  在创建Spring容器，也就是启动Spring时：
    1.  首先会进行扫描，扫描得到所有的BeanDefinition对象，并存在一个Map中
    2.  然后筛选出非懒加载的单例BeanDefinition进行创建Bean，对于多例Bean不需要在启动富哦成中去进行创建，对于多例Bean会在每次获取Bean时利用BeanDefinition去创建
    3.  利用BeanDefinition创建Bean就是Bean的创建生命周期，这期间包含了合并BeanDefinition，推断构造方法，实例化，属性填充，初始化前，初始化，初始化后等步骤，其中AOP就是发生在初始化后这一步骤中
    4.  单例Bean创建完了之后，Spring会发布一个容器启动事件
    5.  Spring启动结束
    6.  在源码中会更复杂，比如源码中会提供一些模版方法，让子类来实现，比如源码中海涉及到一些BeanFactoryPostProcessor和BeanPostProcessor的注册，Spring的扫描就是通过BeanFactoryPostProcessor来实现的，依赖注入就是通过BeanPostProcessor来实现的
    7.  在Spring启动过程中还会去处理@Import等注解

### 6. Spring Bean的生命周期

1.  解析类得到BeanDefinition
2.  如果有多个构造方法，则要推断构造方法
3.  确定好构造方法，进行实例化得到一个对象
4.  对对象中的加了@Autowired注解的属性进行属性填充
5.  回调Aware方法，比如BeanNameAware，BeanFactoryAware
6.  调用BeanPostProcessor的初始化前的方法
7.  调用初始化方法
8.  调用BeanPostProcessor的初始化方法，在这里会进行AOP
9.  如果当前创建的Bean是单例的则会把Bean放入单例池
10.  使用Bean
11.  Spring容器关闭时调用DisposableBean中destroy()方法

### 7. Spring用到了哪些设计模式

-   工厂模式：
    -   BeanFactory
    -   FactoryBean
-   适配器模式：AdvisorAdaptor接口，对Advisor进行了适配
-   访问者模式：PropertyAccessor接口，属性访问器，用来访问和设置某个对象的某个属性
-   装饰器模式：BeanWrapper
-   代理模式：AOP
-   观察者模式：时间监听机制
-   策略模式：InstantiationStrategy ---- 根据不同的情况进行实例化
-   模版模式：JdbcTemplate
-   委派模式：BeanDefinitionParserDelegate
-   责任链模式：BeanPostProcessor

### 8. AOP

AOP：将程序中的交叉业务逻辑（比如安全，日志，事务等），封装成一个切面，然后注入到目标对象（具体业务逻辑）中去。AOP可以对某个对象或某些对象的功能进行增强，比如对象中的方法进行增强，可以在执行某个方法之前额外的做一些事情，在某个方法执行之后额外的做一些事情。

### 9. Spring的事务机制

1.  Spring事务底层是基于数据库事务和AOP机制的
2.  首先对于使用了@Transactional注解的Bean，Spring会创建一个代理对象作为Bean
3.  当调用代理对象的方法时，会先判断该方法上是否加了@Transactional注解
4.  如果加了，那么则利用事务管理器创建一个数据库连接
5.  并且修改数据库连接的autocommit属性为false，禁止此连接的自动提交，这是实现Spring事务非常重要的一步
6.  然后执行当前方法，方法中会执行sql
7.  执行完当前方法后，如果没有出现异常就直接提交事务
8.  如果出现了异常，并且这个异常是需要回滚的就会混滚事务，否则仍然提交事务
9.  Spring事务的隔离级别对应的就是数据库的隔离级别
10.  Spring事务的传播机制是Spring事务自己实现的，也是Spring事务中最复杂的
11.  Spring事务的传播机制是基于数据库连接的，一个数据库连接一个事务，如果传播机制配置为需要新开一个事务，那么实际上就是先建立一个数据库连接，在此新数据库连接上执行sql

### 10. SpringBoot注解，及其实现

1.  @SpringBootApplication注解：这个注解标识了一个SpringBoot工程，它实际上是另外三个注解的组合，这三个注解是：
    1.  @SpringBootConfiguration：这个注解实际上就是一个@Configuration，标识启动类也是一个配置类
    2.  @EnableAutoConfiguration：向Spring容器中导入了一个Selector，用来加载ClassPath下SpringFactories中所定义的自动配置类，将这些自动加载为配置Bean
    3.  @ComponentScan：标识扫描路径，因为默认是没有配置实际扫描路径，所以SpringBoot扫描的路径是启动类所在的当前类目录
2.  @Bean注解：用来定义Bean，类似于XML中的<bean>标签，Spring在启动时，会对加了@Bean注解的方法进行解析，将方法的名字作为beanName，并通过执行方法得到bean对象
3.  @Controller，@Service，@ResponseBody，@Autowired

### 11. 什么时候@Transactional会实现

因为Spring的事务是基于代理来实现的，所以某个加了@Transactional的方法只有是被代理对象调用时，那么这个注解才会生效，所以如果是被代理对象来调用这个方法，那么@Transactional是不会生效的

同时如果某个方法是private的，那么@Transactional也会失效，因为底层cglib是基于父子类来实现的，子类是不能重载父类的private方法的，所以无法很好的利用代理，也会导致@Transactional失效

### 12. 框架源码

### 13. 如何实现AOP

利用动态代理技术实现AOP，比如JDK动态代理或Cglib动态代理，利用动态代理技术，可以针对某个类生成代理对象，当调用代理对象的某个方法时，可以任意控制方法的执行，比如可以先打印执行时间，再执行该方法，并且该方法执行完成后，再次执行打印时间。

项目中，比如事务，权限控制，方法执行时长日志都是通过AOP技术来实现的，凡是需要对某些方法做统一处理的都可以用AOP来实现，利用AOP可以做到业务无侵入

### 14. Spring如何处理循环依赖问题

循环依赖：多个对象之间存在循环的引用关系，在初始化过程当中就会出现死循环问题

1.  @Lazy注解：解决构造方法造成的循环依赖问题

2.  三级缓存

    ```java
    一级缓存：缓存最终的单例池结果：
    	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    二级缓存：缓存初始化的对象：
        private final Map<String, Object> earltSingletonObjects = new ConcurrentHashMap<>(16);
    三级缓存：
        缓存对象的ObjectFactory：
        private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
    ```

对于对象之间的普通引用，二级缓存会保存new出来的不完整对象，这样当单例吃中找到不依赖的属性时，就可以先从二级缓存中获取到不完整对象，完成对象创建，在后续的依赖注入过程中，将单例池中对象的引用关系调整完成。

三级缓存：如果引用的对象配置了AOP，那在单例池中最终就会需要注入动态代理对象，而不是原对象。而生成动态代理是要在对象初始化完成之后才开始的。于是Spring增加三级缓存，保存所有对象的动态代理配置信息，在发现有循环依赖时，将这个对象的动态地理信息获取出来，提前进行AOP，生成动态代理。

### 15. Spring 后置处理器的作用

Spring中的后置处理器分为BeanFactory后置处理器和Bean后置处理器，它们是Spring地城源码架构设计中非常重要的一种机制，可以进行扩展。BeanFactory后置处理器表示针对BeanFactory的处理器，Spring启动过程中，会先创建出BeanFactory实例，然后利用BeanFactory处理器来加工BeanFactory。比如，Spring的扫描就是基于BeanFactory后置处理器来实现的，而Bean后置处理器也类似，Spring在创建一个Bean的过程中，首先会实例化得到一个对象，然后再利用Bean后置处理器来对该实例对象进行加工，比如我们常说的依赖注入就是基于一个Bean后置处理器来实现的，通过该Bean后置处理器来给实例对象中加了@Autowired注解的属性自动赋值，还比如我们常说的AOP，也是利用一个Bean后置处理器来实现的，基于原实例对象，判断是否需要进行AOP，如果需要，那么就基于原实例对象进行动态代理，生成一个代理对象。

### 16. Spring如何处理事务

Spring当中支持编程式事务管理和声明式事务管理两种方式：

1.  编程式事务可以使用TransactionTemplate

2.  声明式事务：是Spring在AOP基础上提供的事务实现机制，他的最大优点就是不需要在业务代码中添加事务管理的代码，只需要在配置文件中做相关的事务规则声明就可以了。但是声明式事务只能针对方法级别，无法空值代码级别的事务管理。

    Spring中对事务定义了不同的传播级别：Progagation

    ```java
    PROPAGATION_REQUIRED: 默认传播行为，如果当前没有事务，将创建一个新事物，如果当前存在事务，就加入到事务中。
    PROPAGATION_SUPPORTS: 如果当前存在事务，就加入事务，如果当前不存在事务，就以非事务方式运行
    PROPAGATION_MANDATORY: 如果当前存在事务，就加入该事务，如果当前不存在事务，就抛出异常
    PROPAGATION_REQUIRES_NEW: 无论当前存不存在事务，都创建新事物进行执行
    PROPAGATION_NOT_SUPPORTED: 以非事务方式运行，如果当前存在事务，就将当前事务挂起
    PROPAGATION_NEVER: 以非事务方式运行，如果当前存在事务，就抛出异常
    PROPAGATION_NESTED: 如果当前存在事务，则在嵌套事务内执行，如果当前没有事务，则按REQUIRED属性执行
    ```

    Spring中事务的隔离级别：

    ```java
    ISOLATION_DEFAULT: 使用数据库默认的事务隔离级别
    ISOLATION_READ_UNCOMMITTED: 读未提交，允许事务在执行过程中，读取其他事务未提交的数据
    ISOLATION_READ_COMMITTED: 读已提交，允许事务在执行过程中，读取其他事务已经提交的数据
    ISOLATION_REPEATABLE_READ: 可重复读，在同一事务内，任意时刻的查询结果是一致的
    ISOLATION_SERIALIZABLE: 串行，所有事务依次执行
    ```

### 17. Spring中的Bean创建的声明周期

1.  Spring中一个Bean的创建分为以下几个步骤：
     1. 推断构造方法
     2. 实例化
     3. 填充属性，也就是依赖注入
     4. 处理Aware回调
     5. 初始化前，处理@POSTConstruct注解
     6. 初始化，处理InitialzingBean接口
     7. 初始化后，进行AOP

### 18. Spring中Bean是线程安全的吗？

Spring是一个轻量级控制反转（IOC）和面向切面（AOP）的容器框架

-   从大小与开销两方面而言Spring都是轻量级的
-   通过控制反转（IOC）的技术达到松耦合的目的
-   提供了面向切面编程的丰富支持，允许通过分离应用的业务逻辑与系统级服务进行内举行的开发
-   包含并管理引用对象（Bean）的配置和声明周期，这个意义上是一个容器
-   将简单的组件配置，组合成为复杂的应用，这个意义上是一个框架

### 19. Spring事务什么时候会失效？

Spring事务的原理是AOP，进行了切面增强，那么失效的根本原因是这个AOP不起作用，常见情况有如下几种：

1.  发生自调用，类里面使用this调用奔雷的方法（this通常省略），此时这个this对象不是代理类，而是UserService对象本身

    解决方法很简单，让那个this变成userService的代理类即可

2.  方法不是public的

    ```java
    @Transactional只能用于public的方法上，否则事务不会失效，如果要用在非public方法上，可以开启AspectJ代理模式。
    ```

3.  数据库不支持事务

4.  没有被Spring管理

5.  异常被吃掉，事务不会回滚（或者抛出的异常没有被定义，默认为RUntimeException）

### 20. Spring容器的启动流程

使用AnnotationConfigApplicationContext跟踪启动流程：

```java
this(); // 初始化reader和scanner
scan(basePackages); // 使用scanner组件扫描basePackage下的所有的对象，将配置类的BeanDefinition注册到容器中
refresh(); // 刷新容器
	prepareRefresh(); // 刷新前的预处理
	obtainFreshBeanFactory(); // 获取在容器初始化时创建BeanFactory
	prepareBeanFactory(); // BeanFactory的预处理工作，会向容器中添加一些组件
	postProcessBeanFactory(); // 子类重写该方法，可以实现在BeanFactory创建并预处理完成后做进一步的设置
	invokeBeanFactoryPostProcessors(); // 在BeanFactory初始化之后执行BeanFactory的后处理器
	registerBeanPostProcessors(); // 向容器中注册Bean的后处理器，他的主要作用就是干预Spring初始化Bean的流程，完成代理，自动注入，循环依赖等这些功能
    initMessageSource(); // 初始化messagesource组件，主要用于国际化
	initApplicationEventMulticaster(); // 初始化事件分发器
	onRefresh(); // 留给子容器，子类重写的方法，在容器刷新的时候可以自定义一些逻辑
	registerListeners(); // 注册监听器
	finishBeanFactoryInitialization(); // 完成BeanFactory的初始化，主要作用是初始化所有剩下的单例Bean
	finishRefresh(); // 完成整个容器的初始化，发布BeanFactory容器刷新完成的事件
```

### 21. Spring事务的实现方式和原理以及隔离级别

事务这个概念是数据库层面的

两种方式：编程式和声明式

在一个方法上加了@Transactional注解后，Spring会基于这个类生成一个代理对象，会将这个代理对象当作Bean，在使用这个代理对象的方法时，如果这个方法上存在@Transactional注解，那么代理逻辑会先把事务的自动提交设置为false，然后再去执行原来的业务逻辑方法，如果执行业务逻辑方法没有出现异常，那么代理逻辑中就会将事务进行提交，如果执行业务逻辑出现了异常，那么则将事务进行回滚。

针对哪些异常回滚事务是可以配置的，可以利用@Transactional注解中的rollbackFor属性进行配置，默认情况下会对RuntimeException和Error进行回滚

Sprign事务隔离级别就是数据库的隔离级别：外加一个默认级别

-   Read unCommitted：未提交读

-   Read Commited：提交读，不可重复读

-   Repeatable Read：可重复读

-   Serializable：可串行化

    ```
    数据库的配置隔离级别是Read Commited，而Spring配置的隔离级别是Repeatable Read，请问这时隔离级别是以哪个为准？
    以Spring配置的为准，如果Spring设置的隔离级别数据库不支持，取决于数据库
    ```

### 22. Spring中Bean的创建过程

Spring框架中的Bean，经过四个阶段：实例化->属性赋值->初始化->销毁

1. 实例化：new xxxx(); // 两个时机：1. 当客户端向容器申请一个Bean时；2. 当容器在初始化一个Bean时发现还需要依赖另一个Bean。BeanDefinition对象保存。**到底是new一个对象还是创建一个动态代理**

2. 设置对象属性(依赖注入)：Spring通过BeanDefinition找到对象依赖的其他对象，并将这些对象赋予当前对象

3. 处理Aware接口：Spring会检测对象是否实现了xxxAware接口，如果实现了，就会调用对应的方法。

    BeanNameAware，BeanClassLoaderAware，BeanFactoryAware，ApplicationContextAware

4. BeanPostProcessor前置处理：调用BeanPostProcessor的postProcessBeforeInitialization方法

5. InitializingBean：Spring检测对象如果实现了这个接口，就会执行它的afterPropertiesSet()方法，定制初始化逻辑

6. init-method：<bean init-method=xxx>如果Spring发现Bean配置了这个属性，就会调用它的配置方法，执行初始化逻辑，@PostConstruct

7. BeanPostProcessor后置处理：调用BeanPostProcessor的postProcessAfterInitialization方法

    >   到这里，这个Bean的创建过程就完成了，Bean就可以正常使用了

8. DisposableBean：当Bean实现了这个接口，在对象销毁前就会调用destroy()方法

9. destroy-method：<bean destroy-method=xxx> @PostDestroy

### 23. Spring设计模式，应用场景

-   简单工厂：由一个工厂类根据传入的参数，动态决定应该创建哪一个产品类

    ```
    Spring中的BeanFactory就是简单工厂模式的体现，根据传入一个唯一的标识来获得Bean对象，但是否是在传入参数后创建还是传入参数钱创建这个要根据具体情况而定。
    ```

    

-   工厂方法：

    ```
    实现了FactoryBean接口的Bean是一类factory的bean。其特点是，spring会在使用getBean()调用获得该bean时，会自动调用该bean的getObject()方法，所以返回的不是factory这个bean，而是bean.getObject()方法的返回值
    ```

-   单例模式：保证一个类仅有一个实例，并提供一个访问它的全局访问点

    ```
    spring对实例的实现：spring中的单例模式完成了后半句，即提供了全局的访问点BeanFactory。但没有从构造器级别去控制单例，这是因为spring管理的是任意的java对象。
    ```

-   适配器模式：

    ```
    Spring定义了一个适配接口，使得每一中Controller有一种对应的适配器实现类，让适配器代替controller执行相应的方法，这样在扩展Controller时，只需要增加一个适配器类就完成了SpringMVC的扩展了
    ```

-   装饰器模式：动态地给一个对象添加一些额外的职责。就增加功能来说，Decorator模式相比生成子类更为灵活。

    ```
    Spring中用到的包装器模式在类名上有两种表现：一种是类名中含有wrapper，另一种是类名中含有Decorator
    ```

-   动态代理：

    ```
    切面在应用运行的时刻被织入，一般情况下，在织入切面时，AOP容器会为目标对象动态的创建一个代理对象，SpringAOP就是以这种方式织入切面的。
    织入：把切面应用到目标对象并创建新的代理对象的过程。
    ```

### 24. Spring框架中的Bean是线程安全的吗？如果线程不安全，要如何处理？

Spring容器本身没有提供Bean的线程安全策略，因此，也可以说Spring容器中的Bean不是线程安全的。

要如何处理线程完全问题，就要分情况分析。

Spring中的作用域：

1.  singleton
2.  prototype：为每个Bean请求创建一个实例；
3.  request：为每个request请求创建一个实例，请求完成后失效
4.  session：与request是类似的
5.  global-session：全局作用域

对于线程安全问题：

 1.    对于prototype作用域，每次都是生成一个新的对象，所以不存在线程安全问题

 2.    对于singleton作用域：默认就是线程不安全的，但是对于开发中大部分的Bean，其实是无状态的，不需要保证线程安全。所以在平常的MVC开发中，是不会有线程安全问题的。

       >   无状态表示这个实例没有属性对象，不能保存数据，是不变的类。比如：controller，service，dao
       >
       >   有状态表示这个实例是有属性对象的，可以保存数据的，是线程不安全的。比如：pojo

       但是如果要保证线程安全，可以将Bean的作用域改为prototype，比如像Model View

       *另外还可以使用ThreadLocal来解决线程安全问题，ThreadLocal为每个线程保存一个副本变量，每个线程只操作自己的副本变量。*

### 25. Spring中的单例Bean是线程安全的吗

Spring中的Bean默认是单例模式的，框架并没有对Bean进行多线程的封装处理

如果Bean是有状态的，那就需要开发人员自己来进行线程安全的保证，最简单的方法就是改变Bean的作用域，把“singleton”改为“prototype”，这样每次请求Bean就相当于是new Bean()这样就可以保证线程安全了

-   有状态就是有数据存储功能。
-   无状态就是不会保存数据 controller，service和dao层本身并不是线程安全的，只是如果只是调用里面的方法，而且多线程调用一个实例的方法，会在内存中复制变量，这是自己的线程的工作内存，是安全的。。

Dao会操作数据库Connection，Connection是带有状态的，比如说数据库事务，Spring的事务管理器使用ThreadLocal为不同线程维护了一套独立的connection副本，保证线程之间不会互相影响（Spring是如何保证事务获取同一个Connection的）

不要在Bean中声明任何有状态的实例变量或类变量，如果必须如此，那么就使用ThreadLocal把变量变为线程私有的，如果Bean的实例变量或类变量需要在多个线程之间共享，那么就只能使用synchronized，lock，CAS等这些实现线程同步的方法了。

### 26. Spring MVC的主要组件

Handler：处理器，直接应对这MVC中的C就是Controller层。@RequestMapping标注的地方都可以看成是一个Handler，只要可以处理请求就是Handler。

1.  HandlerMapping：

    initHandlerMappings(context)，处理器映射器，根据用户请求的资源uri来查找Handler的，在SpringMVC中会有很多请求，每个请求都需要一个Handler处理，具体接收到一个请求之后使用哪个Handler进行，这就是HandlerMapping需要做的事。

2.  HandlerAdapter：

    initHandlerAdapters(context)：适配器。因为SpringMVC中的Handler可以是任意形式的，只要能处理请求就ok，但是Servlet需要的处理方法的结构却是固定的，都是以request和response为参数的。如何让固定的Servlet处理方法调用灵活的Handler来进行处理呢？这就是HandlerAdapter要做的事情。

    Handler是用来干活的工具；HandlerMapping用于根据需要干的活找到相应的工具；HandlerAdapter是使用工具干活的人。

3.  HandlerExceptionResolver：

    initHandlerExceptionResolvers(context)，根据异常设置ModelAndView，之后交给render方法进行渲染。

4.  ViewResolver：

    initViewResolvers(context)，ViewResolver用来将String类型的视图名和Locale解析为View类型的视图。

5.  RequestToViewNameTranslator：

    initRequestToViewNameTranslator(context)，从request中获取ViewName

6.  LocaleResolver：

    intiLocaleResolver(context)，国际化：一是ViewResolver视图解析的时候，二是用到国际化资源或者主题的时候

7.  ThemeResolver：

    initThemeResolver(context)，解析主题，SpringMVC中一个主题对应一个properties文件

8.  MultipartResolver：

    initMultipartResolver(context)，处理上传请求

9.  FlashMapManager：

    initFlashMapManager(context)，用来管理FlashMap的。

### 27. SpringMVC工作流程

1.  用户发送请求至前端控制器DispatcherServlet
2.  DispatcherServlet收到请求调用HandlerMapping处理映射器
3.  处理映射器找到具体的处理器(可以根据xml配置，注解进行查找)，生成处理器以及处理拦截器（如果有则生成）一并返回给DispatcherServlet
4.  DispatcherServlet调用HandlerAdapter处理适配器
5.  HandlerAdapter经过适配器调用具体的处理器（Controller，也叫后端控制器）
6.  Controller执行完成返回ModelAndView
7.  HandlerAdapter将controller执行结果ModleAndVIew返回给DispatcherServlet
8.  DispatcherServlet将ModelAndVIew传给ViewResolver视图解析器
9.  ViewReslover解析后返回具体View
10.  DispatcherServlet根据View进行渲染视图（即将模型数据填充值视图中）
11.  DispatcherServlet响应用户

### 28. SpringMVC中的控制器是不是单例模式？如果是，如何保证线程安全？

控制器是单例模式

单例模式下就会有线程安全问题

Spring中保证线程安全的问题

1.  将scope设置成非singleton。prototype，request。
2.  最好的方式是将控制器设计成无状态模式。在控制器中，不要携带数据，但是可以引用无状态的Service和Dao。

### 29. Spring Boot自动配置原理

@Import+@Configuration+Spring spi

自动配置类由各个starter提供，使用@Configuration+@Bean定义配置类，放到META-INF/spring.factories下使用Spring SPI扫描META-INF/spring.factories下的配置类

使用@Import导入自动配置类

![Spring Boot自动配置](Spring.assets/Spring%20Boot%E8%87%AA%E5%8A%A8%E9%85%8D%E7%BD%AE.png)

### 30. Mybatis与Hibernate区别

面向对象开发，面向表结构开发

1.  开发速度：

    Mybatis简单

    Hibernate较难

2.  开发工作量：

    Mybatis需要手动编写SQL，以及ResultMap。Hibernate有良好的映射机制，开发者无需关心SQL的生成与结果映射，可以更关注业务流程。

3.  sql优化：

    Hibernate的查询会将表中的所有字段查询出来，这一点有性能消耗。

    Mybatis的sql是手动编写的，可以按需指定查询的字段。

4.  对象管理：

    Hibernate是完整的对象/关系映射解决方案，提供了对象状态管理的功能，使开发者不再需要理会底层数据库系统的细节。

5.  缓存对比：

    相同点：

    ​	都可以实现自己的缓存或使用第三方缓存方案，创建适配器来完全覆盖缓存行为

    不同点：

    ​	Hibernate对查询对象有着良好的管理机制，用户无需关心SQL

    ​	Mybatis，使用二级缓存比较容易出错

    ​	Hibernate功能强大，数据库无关性好，O/R映射能力强，学习成本搞。

### 31. SpringBoot配置文件的加载顺序

优先级从高到低，高优先级的配置覆盖低优先级的配置，所有配置会行程互补配置。

1.  命令行参数，所有的配置都可以在命令行上进行指定
2.  Java系统属性（System.getProperties()）;
3.  操作系统环境变量；
4.  jar包外部的application-{profile}.properties或application.yml(带spring.profile)配置文件
5.  jar包内部的application-{profile}.properties或application.yml(带spring.profile)配置文件 再加载不带profile的
6.  jar包外部的application.properties或application.yml(不带spring.profile)配置文件
7.  jar包内部的application.properties或application.yml(不带spring.profile)配置文件
8.  @Configuration注解类上的PropertySource

### 32. Spring，SpringMVC和SpringBoot的区别

-   Spring是一个IOC容器，用来管理Bean，使用依赖注入实现控制反转，可以很方便的整合各种框架。提供AOP机制弥补OOP的代码重复问题，更方便将不同类不同方法中的共同处理抽取成切面，自动注入给方法执行，比如日志，异常等。
-   SpringMVC是Spring对web框架的一个解决方案，提供了一个总的前端控制器Servlet，用来接收请求，然后定义了一套路由策略（url到handle的映射）及适配器执行Handler，将handler结果使用视图解析技术生成视图展现给前端。
-   SpringBoot是Spring提供的一个快速开发工具包，让开发更方便，更快速的开发Spring+SpringMVC应用，简化了配置（约定了默认配置），整合了一系列的解决方案（starter机制），redis，mongodb，es，可以开箱即用。

### 33. SpringBoot中常用注解及其底层实现

1.  @SpringBootApplication注解：这个注解标识了一个SpringBoot工程，它实际上是另外三个注解的组合：
     	1. @SpringBootConfiguration：这个注解实际就是一个@Configuration，标识启动类也是一个配置类
     	2. @EnableAutoConfiguration：向Spring容器中导入一个Selector，用来加载ClassPath下spring.factories中所定义的字自动配置类，将这些自动加载为配置Bean
     	3. @ComponentScan：标识扫描路径，默认启动类所在目录
2.  @Bean注解：用来定义Bean，类似于xml中的<bean>标签，Spring在启动时，会对加了@Bean注解的方法进行解析，将方法的名字作为BeanName，并通过执行方法得到Bean对象
3.  @Controller，@Service，@ResponseBody，@Autowired

### 34. SpringBoot是如何启动Tomcat的

1.  首先sb在启动的时候会先创建一个Spring容器
2.  在创建Spring容器过程中，会利用@ConditionalOnClass技术来判断当前classPath中是否存在Tomcat依赖，如果存在则会生成一个启动Tomcat的Bean
3.  Spring容器创建完成之后，就会获取启动Tomcat的Bean，并创建Tomcat对象，并绑定端口等，然后启动tomcat

### 35. Mybatis中#{}和$P{}的区别

1.  #{}是预编译处理，是占位符，${}是字符串替换，是拼接符
2.  Mybatis在处理#{}时，会将sql中的#{}替换为？号，调用PreparedStatement来赋值
3.  Mybatis在处理${}时，就是把${}替换成变量的值，调用Statement来赋值
4.  使用#{}可以有效的防止SQL注入，提高系统安全性

### 36. ApplicationContext和BeanFactory的区别

BeanFactory是Spring中非常核心的组件，表示Bean工厂，可以生成Bean，维护Bean，而ApplicationContext继承了BeanFactory，所以ApplicationContext拥有BeanFactory所有的特点，也是一个Bean工厂，但是ApplicationContext除开继承了BeanFactory之外，还继承了诸如EnvironmentCapable，MessageSource，ApplicationEventPublisher等接口，从而ApplicationContext还有获取系统环境变量，国际化，时间发布等功能，这是BeanFactory所不具备的。

### 37. Mybatis插件运行原理，如何编写一个插件

Mybatis只支持针对ParameterHander，ResultSetHandler，StatementHandler，Executor这4种接口的插件，Mybatis使用JDK动态代理，为需要拦截的接口生成代理对象以实现接口方法拦截功能，每当执行这4种接口对象的方法时，就会进入拦截方法，具体就是InvocationHandler的invoke()方法，拦截哪些指定需要拦截的方法。

编写插件：实现Mybatis的Interceptor接口并复写interceptor()方法，然后在给插件编写注解，指定要拦截哪一个接口方法即可，在配置文件中配置编写的插件。

### 38. Mybatis的优缺点

优点：

1.  基于Sql语句编程，相当灵活，不会对应用程序或数据库的现有设计造成任何影响，SQL写在XML里，解除sql与程序代码的耦合，便于统一管理；提供xml标签，支持编写动态sql语句，并可重用。
2.  与JDBC相比，减少了50%以上的代码量，消除了jdbc大量冗余的代码，不需要手动开关连接；
3.  很好的与各种数据库兼容（因为Mybatis使用jdbc来连接数据库，所以只要jdbc支持的数据库Mybatis都支持）。
4.  能够与Spring很好的集成
5.  提供映射标签，支持对象与数据库的orm字段关系映射，提供对象关系映射标签，支持关系组件维护

缺点：

1.  sql语句的编写工作量较大，尤其当字段多，关联表多时，需要自己写sql。
2.  sql语句依赖于数据库，导致数据库移植性差，不能随意更换数据库。

### 39. BeanFactory和ApplicationContext区别

1.  ApplicationContext是BeanFactory的子接口

2.  ApplicationContext提供了更完整的功能

    1.  继承MessageSource，因此支持国际化
    2.  统一的资源文件访问方式
    3.  提供在监听器中注册Bean的事件
    4.  同时加载多个配置文件
    5.  载入多个（有继承关系）上下文，使得每一个上下文都专注于一个特定的层次，比如应用的web层

    -   BeanFactory采用的是延迟加载形式来注入Bean的，即只有在使用到某个Bean时（调用getBean()），才对该Bean进行加载实例化。这样，我们就不能发现一些存在的Spring的配置问题，如果Bean的某一个属性没有注入，BeanFactory加载后，直至第一次使用调用getBean()方法才会抛出异常。
    -   ApplicationContext，它是在容器启动时，一次性创建了所有的Bean。这样，在容器启动时，我们就可以发现Spring中存在的配置错误，这样有利于检查所依赖属性是否注入。ApplicationContext启动后预载入所有的单实例Bean，通过预载入单实例Bean，确保当你需要的时候，你就不用等待，因为它们已经创建好了。
    -   相对于基本的BeanFactory，ApplicationContext唯一的不足是占用内存空间，当应用程序配置Bean较多时，程序启动较慢。
    -   BeanFactory通常以编程的方式被创建，ApplicationContext还能以声明的方式创建，如使用ContextLoader。
    -   BeanFactory和ApplicationContext都支持BeanPostProcessor，BeanFactoryPostProcessor的使用，但两者之间的区别是：BeanFactory需要手动注册，而ApplicationContext则是自动注册。

















