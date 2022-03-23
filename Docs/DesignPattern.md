# 设计模式



## 代理模式

**定义：**

给目标对象提供一个代理对象，并由代理对象控制对模板对象的引用。

在代理模式中，是需要代理对象和模板对象实现同一个接口，如果是不同接口，那就是适配器模式了。

![img](https://upload-images.jianshu.io/upload_images/944365-3e4cc2f9c34a64cc.png?imageMogr2/auto-orient/strip|imageView2/2)

**为什么要使用代理：**

在不改变目前对象方法的情况下对方法进行增强。





## 单例模式

**定义**：

单例（Singleton）模式的定义：指一个类只有一个实例，且该类能自行创建这个实例的一种模式。例如，Windows 中只能打开一个任务管理器，这样可以避免因打开多个任务管理器窗口而造成内存资源的浪费，或出现各个窗口显示内容的不一致等错误。

 在计算机系统中，还有 Windows 的回收站、操作系统中的文件系统、多线程中的线程池、显卡的驱动程序对象、打印机的后台处理服务、应用程序的日志对象、数据库的连接池、网站的计数器、Web 应用的配置对象、应用程序中的对话框、系统中的缓存等常常被设计成单例。

 单例模式有 3 个特点：

1. 单例类只有一个实例对象；
2. 该单例对象必须由单例类自行创建；
3. 单例类对外提供一个访问该单例的全局访问点；



**Spring 实现单例模式**



1. 饿汉式单例（在自己被加载时就将自己实例化，资源消耗大，但速度快）：

   私有构造器，不能被继承

```java
    Public class Singleton1{  
    	Private static final Singleton1 instance=new Singleton1();  
    	//私有的默认构造函数  
    	Public Singleton1(){}  
    	//静态工厂方法  
    	Public static Singleton1 getInstance(){  
        Return instance;  
    	}  
    }   
```

2. 懒汉式单例（实例并没有直接实例化，而是在静态工厂方法被调用的时候才进行的，而且对静态工厂方法使用了同步化，以处理多线程并发的环境）：

```java
Public class Singleton2{  
       Private static final Singleton2 instance=null;  
       //私有的默认构造函数  
       Public Singleton1(){}  
       //静态工厂方法  
       Public synchronized static Singleton2 getInstance(){  
         If(instance==null){  
           Instance=new Singleton2();  
         }  
         Return instance;  
       }  
    }
```

3. 单例注册表

   ```java
   Import java.util.HashMap;  
       Public class RegSingleton{  
          Static private HashMap registry=new HashMap();  
          //静态块，在类被加载时自动执行  
           Static{  
            RegSingleton rs=new RegSingleton();  
            Registry.put(rs.getClass().getName(),rs);  
          }  
       //受保护的默认构造函数，如果为继承关系，则可以调用，克服了单例类不能为继承的缺点  
       Protected RegSingleton(){}  
       //静态工厂方法，返回此类的唯一实例  
       public static RegSingleton getInstance(String name){  
           if(name==null){  
             name=” RegSingleton”;  
           }if(registry.get(name)==null){  
             try{  
                 registry.put(name,Class.forName(name).newInstance());  
              }Catch(Exception ex){ex.printStackTrace();}  
           }  
           Return (RegSingleton)registry.get(name);  
       }  
       }
   ```




**单例模式的应用场景：**

前面分析了单例模式的结构与特点，以下是它通常适用的场景的特点。

- 在应用场景中，某类只要求生成一个对象的时候，如一个班中的班长、每个人的身份证号等。
- 当对象需要被共享的场合。由于单例模式只允许创建一个对象，共享该对象可以节省内存，并加快对象访问速度。如 Web 中的配置对象、数据库的连接池等。
- 当某类需要频繁实例化，而创建的对象又频繁被销毁的时候，如多线程的线程池、网络连接池等。



## 模板模式

**意图：**定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。模板方法使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。

**主要解决：**一些方法通用，却在每一个子类都重新写了这一方法。

**何时使用：**有一些通用的方法。

**如何解决：**将这些通用算法抽象出来。

**关键代码：**在抽象类实现，其他步骤在子类实现。

**应用实例：** 1、在造房子的时候，地基、走线、水管都一样，只有在建筑的后期才有加壁橱加栅栏等差异。 2、西游记里面菩萨定好的 81 难，这就是一个顶层的逻辑骨架。 3、spring 中对 Hibernate 的支持，将一些已经定好的方法封装起来，比如开启事务、获取 Session、关闭 Session 等，程序员不重复写那些已经规范好的代码，直接丢一个实体就可以保存。

**优点：** 1、封装不变部分，扩展可变部分。 2、提取公共代码，便于维护。 3、行为由父类控制，子类实现。

**缺点：**每一个不同的实现都需要一个子类来实现，导致类的个数增加，使得系统更加庞大。

**使用场景：** 1、有多个子类共有的方法，且逻辑相同。 2、重要的、复杂的方法，可以考虑作为模板方法。

**注意事项：**为防止恶意操作，一般模板方法都加上 final 关键词。



```java
// 抽象类Game.java
public abstract class Game {
   abstract void initialize();
   abstract void startPlay();
   abstract void endPlay();
 
   //模板
   public final void play(){
      //初始化游戏
      initialize();
      //开始游戏
      startPlay();
      //结束游戏
      endPlay();
   }
}


//实体类Cricket.java
public class Cricket extends Game {
   @Override
   void endPlay() {
      System.out.println("Cricket Game Finished!");
   }
   @Override
   void initialize() {
      System.out.println("Cricket Game Initialized! Start playing.");
   }
   @Override
   void startPlay() {
      System.out.println("Cricket Game Started. Enjoy the game!");
   }
}

//实体类Football.java
public class Football extends Game {
   @Override
   void endPlay() {
      System.out.println("Football Game Finished!");
   }
   @Override
   void initialize() {
      System.out.println("Football Game Initialized! Start playing.");
   }
   @Override
   void startPlay() {
      System.out.println("Football Game Started. Enjoy the game!");
   }
}

//演示
public class TemplatePatternDemo {
   public static void main(String[] args) {
 
      Game game = new Cricket();
      game.play();
      System.out.println();
      game = new Football();
      game.play();      
   }
}


```

