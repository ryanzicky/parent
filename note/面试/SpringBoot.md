### 1. Spring Boot自动配置原理是什么？

BFPP：BeanFactoryPostProcessor

BPP：BeanPostProcessor

BDRPP：BeanDefinitionRegistryPostProcessor

表达的总体思路是：总-分-总

1.   springboot自动装配是什么，解决了什么问题
2.   自动装配实现的原理：
     1.   当启动springboot应用程序的时候，会先创建SpringApplication的对象，在对象构造方法中会进行某些参数的初始化工作，最主要的是判断当前应用程序的类型以及初始化器和监听器，在这个过程中会加载整个应用程序中的spring.factories文件，将文件的内容放到缓存对象中，方便后续获取。
     2.   SpringApplication对象创建完成之后，开始执行run方法，来完成整个启动，启动过程中最主要的有两个方法，第一个叫做prepareContext，第二个叫做refreshContext，在这两个关键步骤中完成了自动装配的核心功能，前面的处理逻辑包含了上下文对象的创建，banner的打印，异常报告期的准备等各个准备工作，方便后续来进行调用。
     3.   在prepareContext方法中主要完成的是对上下文对象的初始化操作，包括了属性值的设置，比如环境对象，在整个过程中有一个非常重要的方法，叫做load，load主要完成一件事，将当前启动类作为一个beanDefinition注册到registry中，方便后续再进行BeanFactoryPostProcessor调用执行的时候，找到对应的主类，来完成@SpringBootApplication，@EnableAutoConfiguration等注解的解析工作。
     4.   在refreshContext方法中会进行整个容器的刷新过程，会调用到Spring中的refresh方法，refresh中有13个非常关键的方法，来完成整个Spring应用程序的启动，在自动装配过程中，会调用invokeBeanFactoryPostProcessor方法，在此方法中主要是对ConfigurationClassPostProcessor类的处理，这次是BFPP的子类也是BDRPP的子类，在调用的时候会先调用BDRPP中的postProcessBeanDefinitionRegistry方法，然后调用postProcessBeanFactory方法，在执行postProcessBeanDefinitionRegistry的时候会解析处理各种注解，包含@PropertySource，@ComponentScan，@ComponentScans，@Bean，@Import等注解，最主要的是@Import注解的解析。
     5.   在解析@Import注解的时候，会有一个getImports的方法，从主类开始递归解析注解，把所有包含@Import的注解都解析到，然后在processImport方法中对Import的类进行分类，此处主要识别的是AutoConfigurationImportSelector归属于ImportSelect的子类，在后续过程中会调用deferedImportSelectorHandler中的process方法，来完成EnableAutoConfiguration的加载。

### 2. Spring Boot 确定流程

1.   配置属性
2.   获取监听器，发布应用开始启动事件
3.   初始化输入参数
4.   配置环境，输出banner
5.   创建上下文
6.   预处理上下文
7.   刷新上下文
8.   再刷新上下文
9.   发布应用已经启动事件
10.   发布应用启动成功事件





