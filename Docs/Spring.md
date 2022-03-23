# Spring 基础知识

## Spring框架

由7个模块组成：

![image-20200712103347545](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20200712103347545.png)

- **核心容器** ：核心容器提供 Spring 框架的基本功能。核心容器的主要组件是 `BeanFactory`，它是工厂模式的实现。 `BeanFactory` 使用 *控制反转* （IOC） 模式将应用程序的配置和依赖性规范与实际的应用程序代码分开。
- **Spring 上下文** ：Spring 上下文是一个配置文件，向 Spring 框架提供上下文信息。Spring 上下文包括企业服务，例如 JNDI、EJB、电子邮件、国际化、校验和调度功能。
- **Spring AOP** ：通过配置管理特性，Spring AOP 模块直接将面向切面的编程功能集成到了  Spring 框架中。所以，可以很容易地使 Spring 框架管理的任何对象支持 AOP。Spring AOP 模块为基于 Spring  的应用程序中的对象提供了事务管理服务。通过使用 Spring AOP，不用依赖 EJB 组件，就可以将声明性事务管理集成到应用程序中。
- **Spring DAO** ：JDBC DAO  抽象层提供了有意义的异常层次结构，可用该结构来管理异常处理和不同数据库供应商抛出的错误消息。异常层次结构简化了错误处理，并且极大地降低了需要编写的异常代码数量（例如打开和关闭连接）。Spring DAO 的面向 JDBC 的异常遵从通用的 DAO 异常层次结构。
- **Spring ORM** ：Spring 框架插入了若干个 ORM 框架，从而提供了 ORM （对象关系映射）对象关系工具，其中包括 JDO、Hibernate 和 iBatis SQL Map。所有这些都遵从 Spring 的通用事务和 DAO 异常层次结构。
- **Spring Web 模块** ：Web 上下文模块建立在应用程序上下文模块之上，为基于 Web 的应用程序提供了上下文。所以，Spring 框架支持与 Jakarta Struts 的集成。Web 模块还简化了处理多部分请求以及将请求参数绑定到域对象的工作。
- **Spring MVC 框架** ：MVC 框架是一个全功能的构建 Web 应用程序的 MVC 实现。通过策略接口，MVC 框架变成为高度可配置的，MVC 容纳了大量视图技术，其中包括 JSP、Velocity、Tiles、iText 和 POI。



### Spring AOP

AOP 即 Aspect Oriented Program 面向切面编程

目的：AOP能够将那些与业务无关，**却为业务模块所共同调用的逻辑或责任（例如事务处理、日志管理、权限控制等）封装起来**，便于**减少系统的重复代码**，**降低模块间的耦合度**，并**有利于未来的可拓展性和可维护性**。

概念：

切入点（Pointcut）
 在哪些类，哪些方法上切入（**where**）

通知（Advice）
 在方法执行的什么实际（**when:**方法前/方法后/方法前后）做什么（**what:**增强的功能）

切面（Aspect）
 切面 = 切入点 + 通知，通俗点就是：**在什么时机，什么地方，做什么增强！**

织入（Weaving）
 把切面加入到对象，并创建出代理对象的过程。（由 Spring 来完成）

例子：

1. POJO下建立LandLord类

```java
package pojo;

import org.springframework.stereotype.Component;

@Component("landlord")
public class Landlord {

    public void service() {
        // 仅仅只是实现了核心的业务功能
        System.out.println("签合同");
        System.out.println("收房租");
    }
}
```

2. 在aspect下新建中间商Broker类

```java
package aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
class Broker {

    @Before("execution(* pojo.Landlord.service())")
    public void before(){
        System.out.println("带租客看房");
        System.out.println("谈价格");
    }

    @After("execution(* pojo.Landlord.service())")
    public void after(){
        System.out.println("交钥匙");
    }
}
```

3. 在applicationContext.xml中配置自动注入，并告诉Spring LoC容器去哪里扫描这两个bean

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <context:component-scan base-package="aspect" />
    <context:component-scan base-package="pojo" />

    <aop:aspectj-autoproxy/>
</beans>
```

4. 编写测试及测试

```java
package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pojo.Landlord;

public class TestSpring {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        Landlord landlord = (Landlord) context.getBean("landlord", Landlord.class);
        landlord.service();

    }
}
```

测试结果：

![img](https://upload-images.jianshu.io/upload_images/7896890-a7dc802dcfd2f1a2.png?imageMogr2/auto-orient/strip|imageView2/2/w/537)



自己的理解：

1. 关键是before和after这两个注解，这两个注解是AOP 的核心。

2. 实现的重点
   1. @Aspect注解给（Broker）中间商，并定义切面
   2. 对（LandLord）包租婆和（Broker）中间商实现compentScan扫描，这里有两步，一个是分别加入@Component注解，一个是在bean里声明component-scan base-package。



注解：

`@Before` 前置通知，在连接点方法前调用 

 `@Around` 环绕通知，它将覆盖原有方法，但是允许你通过反射调用原有方法，集成了前置通知和后置通知，它保留了连接点原有的方法的功能

`@After` 后置通知，在连接点方法后调用  

`@AfterReturning` 返回通知，在连接点方法执行并正常返回后调用，要求连接点方法在执行过程中没有发生异常 

`@AfterThrowing` 异常通知，当连接点方法异常时调用



## Spring Bean Scope 作用域



| 作用域         | 描述                                                         |
| -------------- | ------------------------------------------------------------ |
| singleton      | 在spring IoC容器仅存在一个Bean实例，Bean以**单例**方式存在，默认值 |
| prototype      | 每次从容器中调用Bean时，**都返回一个新的实例**，即每次调用getBean()时，相当于执行newXxxBean() |
| request        | 每次HTTP请求都会创建一个新的Bean，该作用域仅适用于WebApplicationContext环境 |
| session        | 同一个HTTP Session共享一个Bean，不同Session使用不同的Bean，仅适用于WebApplicationContext环境 |
| global-session | 一般用于Portlet应用环境，该运用域仅适用于WebApplicationContext环境 |



```java
package com.tutorialspoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class MainApp {
   public static void main(String[] args) {
      ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
      HelloWorld objA = (HelloWorld) context.getBean("helloWorld");
      objA.setMessage("I'm object A");
      objA.getMessage();
      HelloWorld objB = (HelloWorld) context.getBean("helloWorld");
      objB.getMessage();
   }
}
```



singleton: 

```xml
<!-- A bean definition with singleton scope -->
<bean id="..." class="..." scope="singleton">
    <!-- collaborators and configuration for this bean go here -->
</bean>
```

```
Your Message : I'm object A
Your Message : I'm object A
```



prototype:

```xml
<!-- A bean definition with singleton scope -->
<bean id="..." class="..." scope="prototype">
   <!-- collaborators and configuration for this bean go here -->
</bean>
```

```
Your Message : I'm object A
Your Message : null
```





## Spring Bean 生命周期

理解 Spring bean 的生命周期很容易。当一个 bean 被实例化时，它可能需要执行一些初始化使它转换成可用状态。同样，当 bean 不再需要，并且从容器中移除时，可能需要做一些清除工作。

Bean的生命周期可以表达为：**Bean的定义——Bean的初始化——Bean的使用——Bean的销毁**



![这里写图片描述](https://img-blog.csdn.net/20180908211606260?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Byb2dyYW1tZXJfYXQ=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)



定义： Beans.xml

初始化：

1. **afterPropertiesSet() **方法

   ```java
   public class ExampleBean implements InitializingBean {
      public void afterPropertiesSet() {
         // do some initialization work
      }
   }
   ```



2. **init-method** 属性

```xml
<bean id="exampleBean" 
         class="examples.ExampleBean" init-method="init"/>
```



```java
public class ExampleBean {
   public void init() {
      // do some initialization work
   }
}
```



Note: 

afterPropertiesSet() 方法在init-method方法之前生效。



Bean生命时候被创建？

容器启动之后，并不会马上实例化相应的bean定义。但容器有BeanDefinition，来保存实例化阶段所需要的信息。只有当请求方法通过BeanFactory的getBean()方法来请求某个对象实例（Instantiation ）时，才有可能触发Bean实例化的活动。



BeanFacotory：对象实例化默认采用延迟初始化。当对象A被请求需要第一次实例化，它所依赖的对象B却没被实例化，这时候容器会先实例化对象B，这种情况是容器内部调用getBean()，对于对象B的实例化是隐式(hidden)的。

ApplicationContext：上下文容器启动后会实例化所有单例bean（bean的作用域是singleton）定义。所以当你得到ApplicationContext类型的容器引用时，容器内所有对象已经全部实例化完成。



之所以说getBean()方法是有可能触发Bean实例化阶段的活动，是因为只有当对应某个bean定义的getBean()方法第一次被调用时，不管是显式的还是隐式的，Bean实例化阶段的活动才会被触发，第二次被调用则会直接返回容器缓存的第一次实例化完的对象实例（prototype类型bean除外）。当getBean()方法内部发现该bean定义之前还没有被实例化之后，会通过createBean()方法来进行具体的对象实例化。





## Spring Bean后置处理器

Bean后置处理器允许在调用**初始化方法前后**对Bean进行额外的处理。

**BeanPostProcessor** 接口定义回调方法，你可以实现该方法来提供自己的实例化逻辑，依赖解析逻辑等。你也可以在 Spring 容器通过插入一个或多个 BeanPostProcessor 的实现来完成实例化，配置和初始化一个bean之后实现一些自定义逻辑回调方法。

你可以配置多个 BeanPostProcessor 接口，通过设置 BeanPostProcessor 实现的 **Ordered** 接口提供的 **order** 属性来控制这些 BeanPostProcessor 接口的执行顺序。

BeanPostProcessor 可以对 bean（或对象）实例进行操作，这意味着 Spring IoC 容器实例化一个 bean 实例，然后 BeanPostProcessor 接口进行它们的工作。 

**ApplicationContext** 会自动检测由 **BeanPostProcessor** 接口的实现定义的 bean，注册这些 bean 为后置处理器，然后通过在容器中创建 bean，在适当的时候调用它。



## Spring Bean 定义继承

bean 定义可以包含很多的配置信息，包括构造函数的参数，属性值，容器的具体信息例如初始化方法，静态工厂方法名，等等。

子 bean 的定义继承父定义的配置数据。子定义可以根据需要重写一些值，或者添加其他值。

Spring Bean 定义的继承与 Java 类的继承无关，但是继承的概念是一样的。你可以定义一个父 bean 的定义作为模板和其他子 bean 就可以从父 bean 中继承所需的配置。

当你使用基于 XML 的配置元数据时，通过使用父属性，指定父 bean 作为该属性的值来表明子 bean 的定义。

具体方法： parent=“beanId”

```xml
   <bean id="helloIndia" class="com.tutorialspoint.HelloIndia" parent="helloWorld">
      <property name="message1" value="Hello India!"/>
      <property name="message3" value="Namaste India!"/>
   </bean>
```

### Bean 定义模板

将abstract = "true"，不必填写class属性。

```xml
   <bean id="beanTeamplate" abstract="true">
      <property name="message1" value="Hello World!"/>
      <property name="message2" value="Hello Second World!"/>
      <property name="message3" value="Namaste India!"/>
   </bean>

   <bean id="helloIndia" class="com.tutorialspoint.HelloIndia" parent="beanTeamplate">
      <property name="message1" value="Hello India!"/>
      <property name="message3" value="Namaste India!"/>
   </bean>
```



## Spring 依赖注入 Dependency Injection



Spring框架的核心功能之一就是通过依赖注入的方式来管理Bean之间的依赖关系。依赖注入（或有时称为布线）有助于把这些类粘合在一起，同时保持他们独立。

```java
public class TextEditor {
   private SpellChecker spellChecker;
   public TextEditor(SpellChecker spellChecker) {
      this.spellChecker = spellChecker;
   }
}
```

在这里，TextEditor 不应该担心 SpellChecker 的实现。SpellChecker 将会独立实现，并且在 TextEditor 实例化的时候将提供给 TextEditor，整个过程是由 Spring 框架的控制。在这里，我们已经从 TextEditor 中删除了全面控制，并且把它保存到其他地方（即 XML 配置文件），且依赖关系（即 SpellChecker 类）通过**类构造函数**被注入到 TextEditor 类中



### 基于构造函数的依赖注入 Constructor-based dependency injection

当容器调用带有多个参数的构造函数类时，实现基于构造函数的 DI，每个代表在其他类中的一个依赖关系。

下面的例子显示了一个类 TextEditor，只能用构造函数注入来实现依赖注入。

```java
package com.tutorialspoint;
public class TextEditor {
   private SpellChecker spellChecker;
   public TextEditor(SpellChecker spellChecker) {
      System.out.println("Inside TextEditor constructor." );
      this.spellChecker = spellChecker;
   }
   public void spellCheck() {
      spellChecker.checkSpelling();
   }
}
```

Beans.xml文件的内容：

```xml
   <!-- Definition for textEditor bean -->
   <bean id="textEditor" class="com.tutorialspoint.TextEditor">
      <constructor-arg ref="spellChecker"/>
   </bean>

   <!-- Definition for spellChecker bean -->
   <bean id="spellChecker" class="com.tutorialspoint.SpellChecker">
   </bean>
```



#### 构造函数参数解析:

如果存在不止一个参数时，当把参数传递给构造函数时，可能会存在歧义。要解决这个问题，那么构造函数的参数在 bean 定义中的顺序就是把这些参数提供给适当的构造函数的顺序就可以了。

用index来显式确定参数顺序（不必须）

```xml
<beans>

   <bean id="exampleBean" class="examples.ExampleBean">
      <constructor-arg index="0" value="2001"/>
      <constructor-arg index="1" value="Zara"/>
   </bean>

</beans>
```



### Spring基于设值函数的依赖注入 Setter-based dependency injection

当容器调用一个无参的构造函数或一个无参的静态 factory 方法来初始化你的 bean 后，通过容器在你的 bean 上调用设值函数，基于设值函数的 DI 就完成了。

```JAVA
package com.tutorialspoint;
public class TextEditor {
   private SpellChecker spellChecker;
   // a setter method to inject the dependency.
   public void setSpellChecker(SpellChecker spellChecker) {
      System.out.println("Inside setSpellChecker." );
      this.spellChecker = spellChecker;
   }
   // a getter method to return spellChecker
   public SpellChecker getSpellChecker() {
      return spellChecker;
   }
   public void spellCheck() {
      spellChecker.checkSpelling();
   }
}
```

```xml
   <bean id="textEditor" class="com.tutorialspoint.TextEditor">
      <property name="spellChecker" ref="spellChecker"/>
   </bean>

   <!-- Definition for spellChecker bean -->
   <bean id="spellChecker" class="com.tutorialspoint.SpellChecker">
   </bean>
```

你应该注意定义在基于构造函数注入和基于设值函数注入中的 Beans.xml
文件的区别。唯一的区别就是在基于**构造函数**注入中，我们使用的是**〈bean〉标签中的〈constructor-arg〉元素**，而在基于**设值**函数的注入中，我们使用的是**〈bean〉标签中的〈property〉元素**。

#### 使用 p-namespace 实现 XML 配置

```xml
  <bean id="john-classic" class="com.example.Person">
      <property name="name" value="John Doe"/>
      <property name="spouse" ref="jane"/>
   </bean>

   <bean name="jane" class="com.example.Person">
      <property name="name" value="John Doe"/>
   </bean>
```

用p:name写法代替 <property name="name" value="John Doe"/>

```xml
   <bean id="john-classic" class="com.example.Person"
      p:name="John Doe"
      p:spouse-ref="jane"/>
   </bean>

   <bean name="jane" class="com.example.Person"
      p:name="John Doe"/>
   </bean>
```

### 内部Beans

```xml
   <bean id="outerBean" class="...">
      <property name="target">
         <bean id="innerBean" class="..."/>
      </property>
   </bean>
```

### 注入集合

现在如果你想传递多个值，如 Java Collection 类型 List、Set、Map 和 Properties，应该怎么做呢。为了处理这种情况，Spring 提供了四种类型的集合的配置元素，如下所示： 

| 元素    | 描述                                                         |
| ------- | ------------------------------------------------------------ |
| <list>  | 它有助于连线，如注入一列值，**允许重复**。                   |
| <set>   | 它有助于连线一组值，但**不能重复**。                         |
| <map>   | 它可以用来注入名称-值对的集合，其中名称和值可以是**任何类型**。 |
| <props> | 它可以用来注入名称-值对的集合，其中名称和值都是**字符串类型**。 |

##### 注入Bean的引用

```xml
   <!-- Bean Definition to handle references and values -->
   <bean id="..." class="...">

      <!-- Passing bean reference  for java.util.List -->
      <property name="addressList">
         <list>
            <ref bean="address1"/>
            <ref bean="address2"/>
            <value>Pakistan</value>
         </list>
      </property>

      <!-- Passing bean reference  for java.util.Set -->
      <property name="addressSet">
         <set>
            <ref bean="address1"/>
            <ref bean="address2"/>
            <value>Pakistan</value>
         </set>
      </property>

      <!-- Passing bean reference  for java.util.Map -->
      <property name="addressMap">
         <map>
            <entry key="one" value="INDIA"/>
            <entry key ="two" value-ref="address1"/>
            <entry key ="three" value-ref="address2"/>
         </map>
      </property>

   </bean>
```

##### 注入null和空字符串

空：

```xml
<bean id="..." class="exampleBean">
   <property name="email" value=""/>
</bean>
```

null：

```xml
<bean id="..." class="exampleBean">
   <property name="email"><null/></property>
</bean>
```



## Spring Bean 自动装配



Spring 容器可以在不使用`<constructor-arg>`和`<property>` 元素的情况下**自动装配**相互协作的 bean 之间的关系，这有助于减少编写一个大的基于 Spring 的应用程序的 XML 配置的数量。

下列自动装配模式，它们可用于指示 Spring 容器为来使用自动装配进行依赖注入。你可以使用`<bean>`元素的 **autowire** 属性为一个 bean 定义指定自动装配模式。

| 模式                                                         | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| no                                                           | 这是默认的设置，它意味着没有自动装配，你应该使用显式的bean引用来连线。你不用为了连线做特殊的事。在依赖注入章节你已经看到这个了。 |
| [byName](https://www.w3cschool.cn/wkspring/fwdz1mmb.html)    | 由属性名自动装配。Spring 容器看到在 XML 配置文件中 bean 的自动装配的属性设置为 byName。然后尝试匹配，并且将它的属性与在配置文件中被定义为相同名称的 beans 的属性进行连接。 |
| [byType](https://www.w3cschool.cn/wkspring/8dhy1mmd.html)    | 由属性数据类型自动装配。Spring 容器看到在 XML 配置文件中 bean 的自动装配的属性设置为 byType。然后如果它的**类型**匹配配置文件中的一个确切的 bean 名称，它将尝试匹配和连接属性的类型。如果存在不止一个这样的 bean，则一个致命的异常将会被抛出。 |
| [constructor](https://www.w3cschool.cn/wkspring/jtlb1mmf.html) | 类似于 byType，但该类型适用于构造函数参数类型。如果在容器中没有一个构造函数参数类型的 bean，则一个致命错误将会发生。 |
| autodetect                                                   | Spring首先尝试通过 constructor 使用自动装配来连接，如果它不执行，Spring 尝试通过 byType 来自动装配。 |



#### 自动装配‘byName'

例如，在配置文件中，如果一个 bean 定义设置为自动装配 *byName*，并且它包含 *spellChecker* 属性（即，它有一个 *setSpellChecker(...)* 方法），那么 Spring 就会查找定义名为 *spellChecker* 的    bean，并且用它来设置这个属性。你仍然可以使用 <property> 标签连接其余的属性。下面的例子将说明这个概念。

正常情况下Beans.xml:

```xml
   <!-- Definition for textEditor bean -->
   <bean id="textEditor" class="com.tutorialspoint.TextEditor">
       <property name="spellChecker" ref="spellChecker" />
       <property name="name" value="Generic Text Editor" />
   </bean>

   <!-- Definition for spellChecker bean -->
   <bean id="spellChecker" class="com.tutorialspoint.SpellChecker">
   </bean>
```

 ByName自动装配：

```xml
   <!-- Definition for textEditor bean -->
   <bean id="textEditor" class="com.tutorialspoint.TextEditor" 
      autowire="byName">
      <property name="name" value="Generic Text Editor" />
   </bean>

   <!-- Definition for spellChecker bean -->
   <bean id="spellChecker" class="com.tutorialspoint.SpellChecker">
   </bean>
```



#### 自动装配'byType'

例如，在配置文件中，如果一个 bean 定义设置为自动装配 *byType*，并且它包含 *SpellChecker* 类型的 *spellChecker* 属性，那么 Spring 就会查找定义名为 *SpellChecker* 的 bean，并且用它来设置这个属性。你仍然可以使用 <property> 标签连接其余属性。下面的例子将说明这个概念，你会发现和上面的例子没有什么区别，除了 XML 配置文件已经被改变。

```xml
   <!-- Definition for textEditor bean -->
   <bean id="textEditor" class="com.tutorialspoint.TextEditor" 
      autowire="byType">
      <property name="name" value="Generic Text Editor" />
   </bean>

   <!-- Definition for spellChecker bean -->
   <bean id="SpellChecker" class="com.tutorialspoint.SpellChecker">
   </bean>
```



#### 构造器函数自动装配



正常beans.xml文件：

```xml
   <!-- Definition for textEditor bean -->
   <bean id="textEditor" class="com.tutorialspoint.TextEditor">
      <constructor-arg  ref="spellChecker" />
      <constructor-arg  value="Generic Text Editor"/>
   </bean>

   <!-- Definition for spellChecker bean -->
   <bean id="spellChecker" class="com.tutorialspoint.SpellChecker">
   </bean>
```

构造器函数自动装配：

```xml
   <!-- Definition for textEditor bean -->
   <bean id="textEditor" class="com.tutorialspoint.TextEditor" 
      autowire="constructor">
      <constructor-arg value="Generic Text Editor"/>
   </bean>

   <!-- Definition for spellChecker bean -->
   <bean id="SpellChecker" class="com.tutorialspoint.SpellChecker">
   </bean>
```





## Spring 注解

注解连线在默认情况下在 Spring 容器中不打开。因此，在可以使用基于注解的连线之前，我们将需要在我们的 Spring 配置文件中启用它。所以如果你想在 Spring 应用程序中使用的任何注解，可以考虑到下面的配置文件。

重点是： 

```XML
<context:annotation-config/>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-3.0.xsd">

   <context:annotation-config/>
   <!-- bean definitions go here -->

</beans>
```



### @Reuqired 注释

**@Required** 注释应用于 bean 属性的 setter 方法，它表明受影响的 bean 
属性在配置时必须放在 XML 配置文件中，否则容器就会抛出一个 BeanInitializationException 
异常。下面显示的是一个使用 @Required 注释的示例。

```java
package com.tutorialspoint;
import org.springframework.beans.factory.annotation.Required;
public class Student {
   private Integer age;
   private String name;
   @Required
   public void setAge(Integer age) {
      this.age = age;
   }
   public Integer getAge() {
      return age;
   }
   @Required
   public void setName(String name) {
      this.name = name;
   }
   public String getName() {
      return name;
   }
}
```

### @Autowired 注释

@Autowired 注释可以在 setter 方法中被用于自动连接 bean，就像 @Autowired 注释，容器，一个属性或者任意命名的可能带有多个参数的方法。你可以在 XML 文件中的 setter 方法中使用 **@Autowired** 注释来除去  元素。当 Spring遇到一个在 setter 方法中使用的  @Autowired 注释，它会在方法中视图执行 **byType** 自动连接。

#### Set方法中加@Autowired注释：

```java
package com.tutorialspoint;
import org.springframework.beans.factory.annotation.Autowired;
public class TextEditor {
   private SpellChecker spellChecker;
   @Autowired
   public void setSpellChecker( SpellChecker spellChecker ){
      this.spellChecker = spellChecker;
   }
   public SpellChecker getSpellChecker( ) {
      return spellChecker;
   }
   public void spellCheck() {
      spellChecker.checkSpelling();
   }
}
```

#### 属性中加@Autowired注释:

```java
package com.tutorialspoint;
import org.springframework.beans.factory.annotation.Autowired;
public class TextEditor {
   @Autowired
   private SpellChecker spellChecker;
   public TextEditor() {
      System.out.println("Inside TextEditor constructor." );
   }  
   public SpellChecker getSpellChecker( ){
      return spellChecker;
   }  
   public void spellCheck(){
      spellChecker.checkSpelling();
   }
}
```

####  构造函数中加@Autowired注释：

```java
package com.tutorialspoint;
import org.springframework.beans.factory.annotation.Autowired;
public class TextEditor {
   private SpellChecker spellChecker;
   @Autowired
   public TextEditor(SpellChecker spellChecker){
      System.out.println("Inside TextEditor constructor." );
      this.spellChecker = spellChecker;
   }
   public void spellCheck(){
      spellChecker.checkSpelling();
   }
}
```



### @Qualifier注释

可能会有这样一种情况，当你创建多个具有相同类型的 bean 时，并且想要用一个属性只为它们其中的一个进行装配，在这种情况下，你可以使用 **@Qualifier** 注释和 **@Autowired** 注释通过指定哪一个真正的 bean 将会被装配来消除混乱。下面显示的是使用 @Qualifier 注释的一个示例。

```java
package com.tutorialspoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
public class Profile {
   @Autowired
   @Qualifier("student1")
   private Student student;
   public Profile(){
      System.out.println("Inside Profile constructor." );
   }
   public void printAge() {
      System.out.println("Age : " + student.getAge() );
   }
   public void printName() {
      System.out.println("Name : " + student.getName() );
   }
}
```



```xml
   <!-- Definition for profile bean -->
   <bean id="profile" class="com.tutorialspoint.Profile">
   </bean>

   <!-- Definition for student1 bean -->
   <bean id="student1" class="com.tutorialspoint.Student">
      <property name="name"  value="Zara" />
      <property name="age"  value="11"/>
   </bean>

   <!-- Definition for student2 bean -->
   <bean id="student2" class="com.tutorialspoint.Student">
      <property name="name"  value="Nuha" />
      <property name="age"  value="2"/>
   </bean>
```

输出结果：

```
Inside Profile constructor.
Age : 11
Name : Zara
```

### JSR-250注释

#### @PostConstruct和@PreDestroy注释

为了定义一个 bean 的安装和卸载，我们使用 **init-method** 和/或 **destroy-method** 参数简单的声明一下 。init-method 属性指定了一个方法，该方法在 bean 的实例化阶段会立即被调用。同样地，destroy-method 指定了一个方法，该方法只在一个 bean 从容器中删除之前被调用。

你可以使用 **@PostConstruct** 注释作为初始化回调函数的一个替代。**@PreDestroy** 注释作为销毁回调函数的一个替代，其解释如下示例所示。

```java
package com.tutorialspoint;
import javax.annotation.*;
public class HelloWorld {
   private String message;
   public void setMessage(String message){
      this.message  = message;
   }
   public String getMessage(){
      System.out.println("Your Message : " + message);
      return message;
   }
   @PostConstruct
   public void init(){
      System.out.println("Bean is going through init.");
   }
   @PreDestroy
   public void destroy(){
      System.out.println("Bean will destroy now.");
   }
}
```

```xml
   <bean id="helloWorld" 
       class="com.tutorialspoint.HelloWorld"
       init-method="init" destroy-method="destroy">
       <property name="message" value="Hello World!"/>
   </bean>
```



#### @Resource 注释

你可以在字段中或者 setter 方法中使用 **@Resource** 注释，它和在 Java EE 5 中的运作是一样的。@Resource 注释使用一个 ‘name’ 属性，该属性以一个 bean 名称的形式被注入。你可以说，它遵循 **by-name** 自动连接语义，如下面的示例所示

```java
package com.tutorialspoint;
import javax.annotation.Resource;
public class TextEditor {
   private SpellChecker spellChecker;
   @Resource(name= "spellChecker")
   public void setSpellChecker( SpellChecker spellChecker ){
      this.spellChecker = spellChecker;
   }
   public SpellChecker getSpellChecker(){
      return spellChecker;
   }
   public void spellCheck(){
      spellChecker.checkSpelling();
   }
}
```



### Spring基于Java的配置



#### @Configuration 和 @Bean 注解

带有 **@Configuration** 的注解类表示这个类可以使用 Spring IoC 容器作为 bean 定义的来源。**@Bean** 注解告诉 Spring，一个带有 @Bean 的注解方法将返回一个对象，该对象应该被注册为在 Spring 应用程序上下文中的 bean。最简单可行的 @Configuration 类如下所示：

```java
package com.tutorialspoint;
import org.springframework.context.annotation.*;
@Configuration
public class HelloWorldConfig {
   @Bean 
   public HelloWorld helloWorld(){
      return new HelloWorld();
   }
}
```

上面代码等同于下面的XML配置

```xml
<beans>
   <bean id="helloWorld" class="com.tutorialspoint.HelloWorld" />
</beans>
```

在这里，带有 @Bean 注解的方法名称作为 bean 的 ID，它创建并返回实际的 bean。你的配置类可以声明多个 @Bean。一旦定义了配置类，你就可以使用 *AnnotationConfigApplicationContext* 来加载并把他们提供给 Spring 容器，如下所示：

```java
public static void main(String[] args) {
   ApplicationContext ctx = 
   new AnnotationConfigApplicationContext(HelloWorldConfig.class); 
   HelloWorld helloWorld = ctx.getBean(HelloWorld.class);
   helloWorld.setMessage("Hello World!");
   helloWorld.getMessage();
}
```



#### **@import**注解

```java
@Configuration
@Import(ConfigA.class)
public class ConfigB {
   @Bean
   public B a() {
      return new A(); 
   }
}
```

现在，当实例化上下文时，不需要同时指定 ConfigA.class 和 ConfigB.class，只有 ConfigB 类需要提供，如下所示：

```java
public static void main(String[] args) {
   ApplicationContext ctx = 
   new AnnotationConfigApplicationContext(ConfigB.class);
   // now both beans A and B will be available...
   A a = ctx.getBean(A.class);
   B b = ctx.getBean(B.class);
}
```



#### 生命周期回调

@Bean 注解支持指定任意的初始化和销毁的回调方法，就像在 bean 元素中 Spring 的 XML 的初始化方法和销毁方法的属性：

```java
public class Foo {
   public void init() {
      // initialization logic
   }
   public void cleanup() {
      // destruction logic
   }
}

@Configuration
public class AppConfig {
   @Bean(initMethod = "init", destroyMethod = "cleanup" )
   public Foo foo() {
      return new Foo();
   }
}
```

指定 Bean 的范围：

默认范围是单实例，但是你可以重写带有 @Scope 注解的该方法，如下所示：

```java
@Configuration
public class AppConfig {
   @Bean
   @Scope("prototype")
   public Foo foo() {
      return new Foo();
   }
}
```



### Spring中的时间处理

你已经看到了在所有章节中 Spring 的核心是 **ApplicationContext**，它负责管理 beans 的完整生命周期。当加载 beans 时，ApplicationContext 发布某些类型的事件。例如，当上下文启动时，ContextStartedEvent 发布，当上下文停止时，ContextStoppedEvent 发布。

通过 ApplicationEvent 类和 ApplicationListener 接口来提供在  ApplicationContext 中处理事件。如果一个 bean 实现 ApplicationListener，那么每次  ApplicationEvent 被发布到 ApplicationContext 上，那个 bean 会被通知。

| 序号 | Spring 内置事件 & 描述                                       |
| ---- | :----------------------------------------------------------- |
| 1    | **ContextRefreshedEvent：** ApplicationContext 被初始化或刷新时，该事件被发布。这也可以在 ConfigurableApplicationContext 接口中使用 refresh() 方法来发生。 |
| 2    | **ContextStartedEvent**：当使用 ConfigurableApplicationContext 接口中的 start() 方法启动 ApplicationContext 时，该事件被发布。你可以调查你的数据库，或者你可以在接受到这个事件后重启任何停止的应用程序。 |
| 3    | **ContextStoppedEvent：** 当使用 ConfigurableApplicationContext 接口中的 stop() 方法停止 ApplicationContext 时，发布这个事件。你可以在接受到这个事件后做必要的清理的工作。 |
| 4    | **ContextClosedEvent：** 当使用 ConfigurableApplicationContext 接口中的 close() 方法关闭 ApplicationContext 时，该事件被发布。一个已关闭的上下文到达生命周期末端；它不能被刷新或重启。 |
| 5    | **RequestHandledEvent：**这是一个 web-specific 事件，告诉所有 bean HTTP 请求已经被服务。 |



为了监听上下文事件，一个 bean 应该实现只有一个方法 **onApplicationEvent()** 的 ApplicationListener 接口。因此，我们写一个例子来看看事件是如何传播的，以及如何可以用代码来执行基于某些事件所需的任务。

让我们在恰当的位置使用 Eclipse IDE，然后按照下面的步骤来创建一个 Spring 应用程序：



CSEventHandler.java文件的内容：

```java
package com.tutorialspoint;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
public class CStartEventHandler 
   implements ApplicationListener<ContextStartedEvent>{
   public void onApplicationEvent(ContextStartedEvent event) {
      System.out.println("ContextStartedEvent Received");
   }
}
```

CStopEventHandler.java文件的内容：

```java
package com.tutorialspoint;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
public class CStopEventHandler 
   implements ApplicationListener<ContextStoppedEvent>{
   public void onApplicationEvent(ContextStoppedEvent event) {
      System.out.println("ContextStoppedEvent Received");
   }
}
```

在Beans.xml中配置：

```xml
   <bean id="helloWorld" class="com.tutorialspoint.HelloWorld">
      <property name="message" value="Hello World!"/>
   </bean>

   <bean id="cStartEventHandler" 
         class="com.tutorialspoint.CStartEventHandler"/>

   <bean id="cStopEventHandler" 
         class="com.tutorialspoint.CStopEventHandler"/>
```



## Spring框架的AOP Aspect Oriented Programming

Spring 框架的一个关键组件是**面向方面的编程**(AOP)框架。面向方面的编程需要把程序逻辑分解成不同的部分称为所谓的关注点。跨一个应用程序的多个点的功能被称为**横切关注点**，这些横切关注点在概念上独立于应用程序的业务逻辑。有各种各样的常见的很好的方面的例子，如日志记录、审计、声明式事务、安全性和缓存等。

Spring AOP 模块提供拦截器来拦截一个应用程序，例如，当执行一个方法时，你可以在方法执行之前或之后添加额外的功能。

| 项            | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| Aspect        | 一个模块具有一组提供横切需求的 APIs。例如，一个日志模块为了记录日志将被 AOP 方面调用。应用程序可以拥有任意数量的方面，这取决于需求。 |
| Join point    | 在你的应用程序中它代表一个点，你可以在插件 AOP 方面。你也能说，它是在实际的应用程序中，其中一个操作将使用 Spring AOP 框架。 |
| Advice        | 这是实际行动之前或之后执行的方法。这是在程序执行期间通过 Spring AOP 框架实际被调用的代码。 |
| Pointcut      | 这是一组一个或多个连接点，通知应该被执行。你可以使用表达式或模式指定切入点正如我们将在 AOP 的例子中看到的。 |
| Introduction  | 引用允许你添加新方法或属性到现有的类中。                     |
| Target object | 被一个或者多个方面所通知的对象，这个对象永远是一个被代理对象。也称为被通知对象。 |
| Weaving       | Weaving 把方面连接到其它的应用程序类型或者对象上，并创建一个被通知的对象。这些可以在编译时，类加载时和运行时完成。 |





mysql 显示 男女而不是12

如何控制事务

redis击穿 redis存储情况 断电怎么办

异步 同步 标签 前端

接口 和mq

线程

多个数据库如何储存

kafaka

当前时间

jquery



# SQL UNION 和 UNION ALL 