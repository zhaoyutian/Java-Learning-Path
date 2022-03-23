# 问答篇



谈谈对Spring的理解

Spring本质是框架，是帮助程序员节约开发成本、优化存储空间、降低开发错误的脚手架。Spring诞生的背景就是互联网火爆、各行各业都在推动自动化、数字化。那么如何用少量的程序员，快速开发完成企业级的JAVA EE或Java Web项目，就是Spring要去解决的问题。Spring给出的答卷就是开源、轻量（可以通过AOP进行扩展）以及解耦（IoC依赖关系，控制反转，MVC三层架构模式减少代码耦合度）。只有开源才能提高社区活跃度，只有轻量才能提高灵活度降低侵入性，只有解耦才能减少开发时间，降低开发问题。聊到这，我们才引出Spring的核心，一是IoC，即控制反转，就是在解决bean的依赖关系，就是在解耦；二是AOP，即面向切面编程，则是提高了代码灵活度，至于MVC的三层架构，bean的管理、配置化的bean定义方式，也都是为了解耦或者节约开发成本的功能。

回到Spring核心：AOP和IoC。

AOP就是面向切面编程，它为开发人员提供了对某一个具体切面的前置、后置处理时刻提供了实现方案，比如日志、权限等。其背后的原理则是用到了设计模式中的代理模式，代理模式是对某一对象的增强，它的核心是在调用该对象方法时，不直接使用该对象，而是转由代理对象进行处理，然后我们可以在代理对象对调用前后进行修改，这样就在不改变对象的情况下增强了对象。而Spring AOP具体用的是动态代理的方式。代理的思路我们已经讲过，而动态则是利用了Java反射的特性，Spring使用的是JDK动态代理和CGLib两种，默认是JDK动态代理。

IoC就是控制反转，也被称为DI依赖注入。本质是由IoC容器控制bean的注册和初始化，把bean对象之间的依赖关系交给IoC容器处理。



Spring boot 启动流程

首先Spring boot的main方法中，已经引入了SpringBootApplication注解，此注解由SpringBootConfiguration、EnableAutoConfiguariton和ComponentScan三个注解组成。SpringBootConfiguration表示这是一个配置类，EnableAutoConfiguariton表示开启自动配置，这个自动配置也是Spring boot的非常实用的特性之一。ComponentScan表示扫描路径，为启动之后提供了扫描的位置，这个也可以自己配。以上是main class的注解部分，main方法主体其实就一句话，SpringApplication.run(this.class,args)。这个run就开启了整个Springboot的启动流程。



. new SpringApplication()

2. 置空ConfigurableApplicationContext

3. 构造SpringApplicationRunListeners并开启监听

4. 根据配置（application.yml）设置参数和环境，获取对应的ClassLoader

5. 构造ApplicationContext上下文容器

6. 准备上下文

    1. 设置上下文及reader和sacnner的环境
    2. 监听器加入上下文
    3. Bean工厂注册applicationArguments和springBootBanner单例
    4. 获取所有资源*
    5. load（装载）资源
        1. 创建BeanDefinitionLoader
            1. 创建AnnotatedBeanDefinitionReader
            2. 创建XmlBeanDefinitionReader
            3. 创建ClassPathBeanDefinitionScanner

7. 刷新上下文

    1. 准备刷新，获取环境及其参数信息并验证，包括profile、properties

    2. 准备beanFactory，包括环境、系统参数、系统环境

    3. postProcessBeanFactory

        1. addProcessor
        2. scan（如果有basePackages的话），搜集所有含有注解的class文件
        3. reader注册这些class文件

    4. invokeBeanFactoryPostProcessors 扫描bean并初始化

        1. 收集context中BeanDefinitionRegistryPostProcessor和BeanFactoryPostProcessor
        2. 收集BeanFactory中的BeanDefinitionRegistryPostProcessor
        3. 利用2中的BeanDefinitionRegistryPostProcessor扫描@Configuration/ @Bean/ @Component/ @ComponentScan/ @Import/ @ImportResource (其中@Service、@Controller等包含了@Import的注解也会在此被扫描到)

    5. registerBeanPostProcessors

    6. onRefresh 创建WebServer，默认是Tomcat

    7. 注册监听器

    8. finishBeanFactoryInitialization 完成bean工厂初始化，

       在beanDefinitionNames里的bean会被初始化，包括ConfigurationAnnotationProcessor、AutowiredAnnotationProcessor、RequiredAnnotationProcessor、CommonAnnotationprocessor以及被@Configuration、、@Controller、@Service（本质都是@Component）、@PostConstruct修饰过的方法或类

    9. 完成刷新上下文并发布事件

8. 监听器开启上下文监听

9. 开启Runner

    1. ApplicationRunner
    2. CommondRunner

   用户可以自定义Runner并实现接口ApplicationRunner或CommondRunner，再加注解@ConditionalOnClass可以在系统启动最后触发自定义操作



Spring 自动配置

首先需要在maven下的·pom.xml文件下引入autoconfig包，然后在Spring boot的main方法中加入@SpringBootApplication注解。此注解是一个组合注解，里面的@EnableAutoConfiguration注解，就是Spring开启自动配置的关键。它中包含了@Import注解，并导入了一个 AutoConfigurationImportSelector类。这个类中的getCandidateConfigurations方法就是获取候选的配置信息，它调用SpringFactoriesLoader的load方法，会帮我们加载META-INF/spring.factories下的配置项。当我们启动Spring boot的main方法时，这些配置就会被引入。





类加载顺序

我们写的java语言需要通过java虚拟机（JVM）编译成字节码才能真正被机器识别和执行。Java同时还支持运行时动态加载类，这给了java极大的灵活性，比如Spring的AOP就是根据动态代理完成的，IoC就是通过反射完成的，但这也可能带来一些安全隐患。为了消除这些隐患，JVM设定每一个加载器都有独立的命名空间，也就是不同加载器加载的java类，就算其他属性一模一样，也是不相同的，这样就避免了外部java类侵蚀正常类的可能性。那如何保证相同的类由相同的类加载器加载，这就需要双亲委派机制去管理。双亲委派机制顾名思义就是，加载器遇到类需要加载时，先委派给父类加载，父类遇到这个请求，也会委派给自己的父类加载，直到顶层为止，若父类找不到这个类，子类才会自己加载。在JDK8中，这样的层次一共分四层，启动类加载器、扩展类加载器、应用类加载器和用户自定义加载器。在JDK9之后，因为要适应模块化编程，对双亲委派机制有一定的修改和变化。在平台类加载器和应用程序类加载器收到请求后，在委派给父类加载器加载之前，先判断该类是否能归属到某一系统模块中，若能，则优先委派给负责此模块的加载器完成加载。