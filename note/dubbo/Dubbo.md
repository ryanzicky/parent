### RPC协议(远程方法调用)
    1. 传输方式
    2. 数据格式
    
    协议：
        rpc over tcp
        rpc over http

    method
    http ------> 协议
    http body method

    httpclient + tomcat

### dubbo
    1. 负载均衡
    2. 集群容错
    3. 服务降级
    4. 本地存根
    5. 本地伪装
    6. callBack
    7. 异步调用
    8. 泛化调用
    9. 管理台

### Dubbo的可扩展SPI源码解析
    1. 基本流程
    2. AOP
    3. 依赖注入
    4. AdaptiveExtension
    5. ActivateExtension

### 流程
    ExtensionLoader ---------- 扩展类加载器
        1. type ----------接口
        2. objectFactory -------------- 实现类的对象 --------------Spring容器

        方法
        1. getExtension(name)
        2. createExtension(name) -----> http 创建HttpProtcol对象
            1. name -----—> 实现类 (1. 扫描接口文件内容，Map) map.get(name)----impl
            2. 实例化 --------- CarFilter
            3. 依赖注入
            4. AOP -------- new CarWrapper(carFilter) -------- 新Car实例对象  ------- cachedWrapperClasses 是一个set
            5. return instance
        3. loadExtensionClasses() --------- return Map

### dubbo与spring整合
    1. 解析配置文件 ------- XXConfig Bean
    2. @Service注解的实现流程 ----- bean, ServiceBean --- ref ServiceBean.export()
    3. @Reference注解的实现流程 ------ 接口 ReferenceBean.get() ------- 代理对象()

### dubbo服务导出
    1. 服务注册到注册中心里去
        服务URL: http://192.168.1.112:80/
                com.luban.DemoService?timeout=3000&version=1.0.1&application=dubbo-demo-provider-application
        URL ---------> Zookeeper
        
        注册中心
        流程 导出
            1. 注册  RegistryProtocol.export(URL) registry://192.168.1.112:2181/RegistryService?registry=
                    zookeeper&xxx=http://192.168.1.112:80/com.luban.DemoService?timeout=3000&version=1.0.1
                    &application=dubbo-demo-provider-application
        
        1. 构造URL:
            1. 构造服务的参数
                
        2. 注册URL:
            
    2. 启动Tomcat,nettyServer


### Spring Cloud和Dubbo的区别
    底层协议: SpringCloud基于http协议(传输层)，dubbo基于tcp协议(应用层)，决定了dubbo的性能相对比较好
    注册中心: SpringCloud使用的eureka，dubbo推荐使用zookeeper
    模型定义: dubbo将一个接口定义为一个服务，SpringCloud则是将一个应用定义为一个服务
    SpringCloud是一个生态，而Dubbo是SpringCloud生态中关于服务调用一种解决方案(服务治理)

### Dubbo服务引入
    1. 生成一个代理对象(服务接口的一个代理)
    2. 代理逻辑
        {
            mock
            所有服务提供者
            路由
            负载均衡
            集群容错
            发送Invocation对象
        }


    1. Invoke(配置)
    2. 注册中心读数据
    3. DubboInvole HttpInvoke

    StaticDirectory: 静态

    RegistryDiretory: 动态服务目录
        serviceKey
        queryMap
        registry: 注册中心
        protocol: 消费者配置协议
        invokers: 当前这个服务所有的服务提供者
        ConsumerConfigurationListener:
        
        
    
