# Java 基础


## java注解的实现原理

注解本质是一个继承了Annotation 的特殊接口，其具体实现类是Java 运行时生成的动态代理类。而我们通过反射获取注解时，返回的是Java 运行时生成的动态代理对象$Proxy1。通过代理对象调用自定义注解（接口）的方法，会最终调用AnnotationInvocationHandler 的invoke 方法。该方法会从memberValues 这个Map 中索引出对应的值。而memberValues 的来源是Java 常量池。


## equal() 和 hashCode()


equal()相等的两个对象他们的hashCode()肯定相等，也就是用equal()对比是绝对可靠的。

hashCode()相等的两个对象他们的equal()不一定相等，也就是hashCode()不是绝对可靠的。

所有对于需要大量并且快速的对比的话如果都用equal()去做显然效率太低，所以解决方式是，每当需要对比的时候，首先**用hashCode()去对比**，如果**hashCode()不一样**，则表示这两个对象肯定**不相等**（也就是不必再用equal()去再对比了）,如果hashCode()相同，此时**再对比他们的equal()**，如果equal()也相同，则表示这两个对象是真的相同了，这样既能大大提高了效率也保证了对比的绝对正确性！

**哈希码（HashCode）介绍**

哈希码产生的依据：哈希码并不是完全唯一的，它是一种算法，让同一个类的对象按照自己不同的特征尽量的有不同的哈希码，但不表示不同的对象哈希码完全不同。也有相同的情况，看程序员如何写哈希码的算法。

**重写equal()和HashCode()方法的应用**

项目要求用户的名字和年龄都相同则视为同一个User object。

equal()方法：先判断==，再判断是否为null，然后看类型，最后看name和age。

```java
 MUIUUIIQ=		q113   @Override  
    public boolean equals(Object obj) {  
        if (this == obj)  
            return true;  
        if (obj == null)  
            return false;  
        if (getClass() != obj.getClass())  
            return false;  
        Student other = (Student) obj;  
        if (age != other.age)  
            return false;  
        if (name == null) {  
            if (other.name != null)  
                return false;  
        } else if (!name.equals(other.name))  
            return false;  
        return true;  
    }  
```

HashCode()方法：把age和name都当做运算的参数。

```java
    @Override  
    public int hashCode() {  
        final int prime = 31;  
        int result = 1;  
        result = prime * result + age;  
        result = prime * result + ((name == null) ? 0 : name.hashCode());  
        System.out.println("hashCode : "+ result);  
        return result;  
    }  
```

Q：为什么要**重写HashCode()**方法？

1. 提高效率：元素放入集合的流程中，先判断这个要加入的元素是否和集合中任意一个元素的hashcode值相等，不等，则直接加入此元素。若相等，则再看equal方法是否相等，若不等，则加入此元素。

2. 避免歧义，出现equal不等，hashcode却相等的情况。


## 定义

**HTTP协议**

HTTP（超文本传输协议，HyperText Transfer Protocol)是互联网上应用最为广泛的一种网络协议。

HTTP 1.0

HTTP 协议老的标准是HTTP/1.0，为了提高系统的效率，HTTP 1.0规定浏览器与服务器只保持短暂的连接，浏览器的每次请求都需要与服务器建立一个TCP连接，服务器完成请求处理后立即断开TCP连接，服务器不跟踪每个客户也不记录过去的请求。

同时，带宽和延迟也是影响一个网络请求的重要因素。在网络基础建设已经使得带宽得到极大的提升的当下，大部分时候都是延迟在于响应速度。基于此会发现，http1.0被抱怨最多的就是**连接无法复用**，和**head of line blocking**这两个问题。



HTTP 1.1

为了克服HTTP 1.0的这个缺陷，HTTP 1.1支持持久连接（HTTP/1.1的默认模式使用带流水线的持久连接），在一个TCP连接上可以传送多个HTTP请求和响应，减少了建立和关闭连接的消耗和延迟



**接口**

就是提供一种统一的”协议”，而接口中的属性也属于“协议”中的成员。它们是公共的，静态的，最终的常量。相当于全局常量。抽象类是不“完全”的类，相当于是接口和具体类的一个中间层。即满足接口的抽象，也满足具体的实现。

**接口可用的修饰符**：

public final abstract

不能用private、static、synchronized、native访问修饰符修饰。



**String类能被继承吗？**

答：不能。因为String类被final修饰。



**能否从一个static方法内部发出对非static方法的调用？**

答：不能。**非static方法是要与对象关联在一起的**，**必须创建一个对象后，才可以在该对象上进行方法调用，而static方法调用时不需要创建对象，可以直接调用**。也就是说，当一个static方法被调用时，可能还没有创建任何实例对象，如果从一个static方法中发出对非static方法的调用，那个非static方法是关联到哪个对象上的呢？这个逻辑无法成立，所以，一个static方法内部发出对非static方法的调用。



**一个“.java”源文件中是否可以包括多个类（不是内部类）？有什么限制？**

  答：可以有多个类，但只能有一个public类，并且public的类名必须与文件名相一致。



**枚举类是原始数据类型吗？**

答：不是



**拷贝数组的方法**：

1. System.arraycopy（）
2. Arrays.copyOf()
3. clone()
4. foreach()

其中**System.arraycopy()效率最高**，因为它是由本地方法区用c或c++实现的。

Arrays.copyOf()底层也是用System.arraycopy()，只不过在赋值length的时候加了一个判断Math.min(original.length, newLength))



**加载驱动方法**

1. Class.forName(“com.microsoft.sqlserver.jdbc.SQLServerDriver”);
2. DriverManager.registerDriver(new com.mysql.jdbc.Driver());
3. System.setProperty(“jdbc.drivers”, “com.mysql.jdbc.Driver”);

注意： DriverManager.getConnection方法返回一个Connection对象，这是加载驱动之后才能进行的



**在Java中，对于不再使用的内存资源，如调用完成的方法，“垃圾回收器”会自动将其释放。**

答：错误。方法调用时，会创建栈帧在栈中，调用完是程序自动出栈释放，而不是gc释放。垃圾回收器是主要是针对堆区



**位运算**

​     <<   左移，左移几位其实就是该数据乘以2的几次方（可完成2的次幂运算）

​     \>>   右移，右移几位就是除以2的几次幂

​     \>>>  无符号右移，数据右移时，高位出现空位，无论原高位是什么，空位都用0补。

​     &    与运算  例3&6即将3和6都用二进制数表示，然后每一位做与运算（都为1则得1，其余都得0）

​     |    或运算  例3|6即将3和6都用二进制数表示，然后每一位做或运算（有一个为1则为1）

​     ^    异或运算  相同为0，相反为1 （一个数异或同一个数两次结果还是这个数）

​     ~    反码  取反（除了符号位以外）



**Integer与int区别：**

Integer（-128~127）

1、Integer是int的包装类，int则是java的一种基本数据类型 
2、Integer变量必须实例化后才能使用，而int变量不需要 
3、Integer实际是对象的引用，当new一个Integer时，实际上是生成一个指针指向此对象；而int则是直接存储数据值 
4、Integer的默认值是null，int的默认值是0

Note：

List<Integer>

```java
Integer i1 = 128; 
Integer i2 = 128; 
System.out.print((i1 == i2)+".");
		  
String i3 = "100";
String i4 = "1" + new String("00"); 
System.out.print((i3== i4)+"."); 

Integer i5 = 100; 
Integer i6 = 100; 
System.out.print(i5 == i6);
```

输出：

```java
false.false.true
```

注：

java对于-128到127之间的数，会进行缓存，Integer i = 127时，会将127进行缓存，下次再写Integer j = 127时，就会直接从缓存中取，就不会new了

当Integer储存的值大于等于128， 有IntegerCache类制动装箱

```java
public static Integer valueOf(int i){
    assert IntegerCache.high >= 127;
    if (i >= IntegerCache.low && i <= IntegerCache.high){
        return IntegerCache.cache[i + (-IntegerCache.low)];
    }
    return new Integer(i);
}
```





### 容器

容器分为三大类： Iterator,Collection,Map

![img](https://images0.cnblogs.com/i/617995/201404/161353012916056.png)



### List

是一个**有序、可重复、可为null**的集合。

**ArrayList**: 基于**动态数组**实现的非线程安全的集合

当底层数组满的情况下还在继续添加的元素时，ArrayList则会执行扩容机制扩大其数组长度。ArrayList查询速度非常快，因此在实际开发中被广泛使用。唯一不足的是插入和删除元素较慢，同时它并不是线程安全的。

ArrayList的**初始容量是10**，也可以在**构造的时候设置initialCapacity**。

```java
DEFAULT_CAPACITY = 10
// 构造具有指定初始容量的空列表
ArrayList(int initalCapacity)
```



ArrayList get() set() function 先rangeCheck(index)判断数组下标是否越界，再通过index直接拿取/设置element。add方法则先用ensureCapacityInternal进行扩容判断，再通过elementData[size++] = e进行赋值。remove方法则是删除index位置上的元素，并用System.arraycopy方法把之后的元素向前移，最后elementData[--size] = null保证GC work。

ArrayList 扩容原理：

每次add的时候会先调用ensureCapacityInternal方法，先判断是否需要扩容

```java
// 判断当前ArrayList是否需要进行扩容
private void ensureCapacityInternal(int minCapacity) {
   ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
}


// 判断当前ArrayList是否需要进行扩容
private void ensureExplicitCapacity(int minCapacity) {
    //快速报错机制
    modCount++;
 
    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

private static int calculateCapacity(Object[] elementData, int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    return minCapacity;
}
```


扩容一般是之前的1.5倍，如果增加的量大于1.5倍的容量，会增加到增加量的容量，然后用copyof方法把元素拷贝到新数组里。

```java
    private void grow(int minCapacity) {
        //之前的容量
        int oldCapacity = elementData.length;
        //新的容量为之前的容量 1.5倍, >> 1 是位运算，代表向右移一位，相当于除以2
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        //如果新的容量小于 要扩容的容量，新的容量等于要扩容的容量
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        //如果已经大于了最大的容量，那么已经到了最大的大小, MAX_ARRAY_SIZE = 0x7ffffff7         
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```



contains方法的实现：

for循环数组下标，进行遍历，判断o.equals(elementData[i])

注意，indexOf方法会先判断输入o是否为null，如果为null，则用==代替equals方法。

```java
   /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this list is to be tested
     * @return <tt>true</tt> if this list contains the specified element
     */

public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
/**
 * Returns the index of the first occurrence of the specified element
 * in this list, or -1 if this list does not contain the element.
 * More formally, returns the lowest index <tt>i</tt> such that
 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
 * or -1 if there is no such index.
 */
public int indexOf(Object o) {
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}
```


MAX_ARRAY_SIZE（阈值）是2的31次-9，即Integer.MAX_VALUE-8。

注：为什么是-8，某些 JVM 在数组中保留一些标题字，避免内存溢出。

ArrayList实现了RandomAccess，支持快速随机访问，indexedBinarySearch，否则只能执行iteratorBinarySearch方法。

ArrayList通过for遍历比通过iterator遍历要稍快，LinkedList通过iterator遍历比通过for遍历要快。



ArraList 和 LinkedList的区别

ArrayList: 数组，连续一块内存空间，方便寻址，但删除插入慢，因为要发生数据迁移。

LinkedList：双向链表，不是连续的内存空间，查找慢，但删除插入快，因为只需要修改前后节点的指针。



**LinkedList**:  基于**双向链表**实现非线程安全的集合

他是一个链表结构，不能像数组一样随即访问，必须是每个元素一次遍历谁知道找到元素为止。其结构的特殊性导致它查询数据慢。

双向链表

双向链表(双链表)是链表的一种。和单链表一样，双链表也是由节点组成，它的每个数据结点中都有两个指针，分别指向直接后继和直接前驱。所以，从双向链表中的任意一个结点开始，都可以很方便地访问它的前驱结点和后继结点。一般我们都构造双向循环链表。

![img](https://images0.cnblogs.com/blog/497634/201402/231247423393589.jpg)

add方法默认添加到表尾

```java
public boolean add(E e) {
    linkLast(e);
    return true;
}

    /**
     * Links e as last element.
     */
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

```





### Set

一个**不包含重复元素**的Collection。Set内部实现是基于Map的，所以Set取值时不保证数据和存入的时候顺序一致，不允许重复值。Set有三个较为常用的实现类，HashSet、TreeSet、LinkedHashSet

如果你需要的是一个快速的集合，建议你使用HashSet;

如果你需要的是一个排序集合，请选择TreeSet;

如果你需要一套能够存储插入顺序的集合,请使用LinkedHashSet。



**HashSet**

底层是**数组+单链表+红黑树**的数据结构，HashSet底层实现依赖于HashMap。



```java
 // HashSet 真实的存储元素结构
 private transient HashMap<E,Object> map;
 // 作为各个存储在 HashMap 元素的键值对中的 Value
 private static final Object PRESENT = new Object();
 //空参数构造方法 调用 HashMap 的空构造参数，还有一些带参构造方法，也都实例化
 HashMap  public HashSet() { 
 	map = new HashMap<>();
 } 
 //添加元素
 public boolean add(E e) {
 	return map.put(e, PRESENT)==null;
 } 
 //HashMap中的方法
 public V put(K key, V value) {
 	return putVal(hash(key), key, value, false, true);
 }
 final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    Node<K, V>[] tab;
     Node<K, V> p;
     int n, i;
    //如果哈希表为空，调用resize()创建一个哈希表，并用变量n记录哈希表长度
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    /**
      * 如果指定参数hash在表中没有对应的桶，即为没有碰撞
      * Hash函数，(n - 1) & hash 计算key将被放置的槽位
      * (n - 1) & hash 本质上是hash % n，位运算更快
      */
     if ((p = tab[i = (n - 1) & hash]) == null)
         //直接将键值对插入到map中即可
         tab[i] = newNode(hash, key, value, null);
     else {// 桶中已经存在元素
         Node<K, V> e;
         K k;
         // 比较桶中第一个元素(数组中的结点)的hash值相等，key相等
         if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
         	// 将第一个元素赋值给e，用e来记录
             e = p;
         	// 当前桶中无该键值对，且桶是红黑树结构，按照红黑树结构插入
         else if (p instanceof TreeNode)
             e = ((TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
         	// 当前桶中无该键值对，且桶是链表结构，按照链表结构插入到尾部
         else {
             for (int binCount = 0; ; ++binCount) {
         	 	// 遍历到链表尾部
             	if ((e = p.next) == null) {
         			p.next = newNode(hash, key, value, null);
         			// 检查链表长度是否达到阈值，达到将该槽位节点组织形式转为红黑树
         			if (binCount >= TREEIFY_THRESHOLD - 1)// -1 for 1st
                        treeifyBin(tab, hash);
                     break; 
         		} 
         		// 链表节点的<key, value>与put操作<key, value>相同时，不做重复操作，跳出循环
         		if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                 p = e;
         	}
         }
         // 找到或新建一个key和hashCode与插入元素相等的键值对，进行put操作
         if (e != null) {// existing mapping for key
         // 记录e的value
             V oldValue = e.value;
         	/**
             * onlyIfAbsent为false或旧值为null时，允许替换旧值
             * 否则无需替换
             */
             if (!onlyIfAbsent || oldValue == null)
                 e.value = value;
             // 访问后回调
             afterNodeAccess(e);
             // 返回旧值
             return oldValue;
          }
       }
       // 更新结构化修改信息
       ++modCount;
       // 键值对数目超过阈值时，进行rehash
       if (++size > threshold) resize();
          // 插入后回调 afterNodeInsertion(evict);
          return null;
	}

```



**TreeSet**

TreeSet实际上是基于TreeMap的NavigableSet实现。而且TreeSet是非线程安全的。TreeSet判断两个对象不相等的方式是两个对象通过equals方法返回false，或者通过CompareTo方法比较没有返回，TreeSet不支持快速随机遍历，只能通过迭代器进行遍历。

**LinkedHashSet**

LinkedHashSet是介于HashSet 和 TreeSet之间，内部是一个双向链表结构，LinkedHashSet集合同样是根据元素的hashCode值来决定元素的存储位置，但是它同时使用**链表**维护元素的**次序**。这样使得元素看起 来像是以**插入顺序保存**的，也就是说，当遍历该集合时候，LinkedHashSet将会以元素的添加顺序访问集合的元素。



**EnumSet**

枚举 set 中所有键都必须来自单个枚举类型，该枚举类型在创建 set 时显式或隐式地指定。

有iterator方法返回迭代器an齐自然顺序遍历元素（该顺序是生命枚举常量的顺序）。

不允许使用null元素，不同步



### Map

将键映射到值的对象。一个映射不能包含重复的键；每个键最多只能映射到一个值。



**HashMap**

基于哈希表的Map接口的实现。

具体实现:

hashMap是由**数组+链表**组成的。

重要常量：

```java
// 初始大小是16 1 << 4是位运算，表示1向右移4位
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

// 最大容量是2的30次方
static final int MAXIMUM_CAPACITY = 1 << 30;

//负载因子默认是0.75
static final float DEFAULT_LOAD_FACTOR = 0.75f;

//当hash冲突超过8时，将链表（list）转化为数（treeNode）
static final int TREEIFY_THRESHOLD = 8;
```



HashMap的组成因子：Node，包含key，value，hash以及next值

```java
    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
```


构造hashmap

如果有initialCapacity参数，则构造一个**等于或略大于cap的2**的次方的size

```java
static final int tableSizeFor(int cap) {
    // 防止现在就是2的幂次，如果不减，经过下面的算法会把最高位后面的都置位1，再加1则相当于将当前的数值乘2
    int n = cap - 1;
    // 高位右移1位，保障高位和第二高位都为11
    n |= n >>> 1;
    // 高两位右移2位，保障高4位都为1
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

注意点

1. 为什么要 int n = cap -1，再最后又返回 n+1：

   因为要防止输入为2的整数幂的数值，输出为cap的2倍

2. 为什么要位运算5次：

   因为int是32位，-2^31 到 2^31 - 1，所以需要位运算5次：

```ruby
01 00000 00000 00000 00000 00000 00000 (n)   
01 10000 00000 00000 00000 00000 00000 (n |= n >>> 1)    
01 11100 00000 00000 00000 00000 00000 (n |= n >>> 2)    
01 11111 11000 00000 00000 00000 00000 (n |= n >>> 4)    
01 11111 11111 11111 00000 00000 00000 (n |= n >>> 8)    
01 11111 11111 11111 11111 11111 11111 (n |= n >>> 16) 
```



结果如下：

```java
public static void main(String[] args) {
    System.out.println(tableSizeFor(1));
    System.out.println(tableSizeFor(5));
    System.out.println(tableSizeFor(25));
    System.out.println(tableSizeFor(125));
    System.out.println(tableSizeFor(625));
}

输出：
1
8
32
128
1024
```

为什么size要是2的整数幂



get()方法：

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}
```



1. 先计算key的hash值

   ```java
   static final int hash(Object key) {
       int h;
       return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
   }
   ```

   计算key值时，增加了(h = key.hashCode()) ^ (h >>> 16)，即**异或h的高16位**。这是是为了让**hash值更加随机**。由于和（length-1）运算，length 绝大多数情况小于2的16次方。所以始终是hashcode 的低16位（甚至更低）参与运算。要是高16位也参与运算，会让得到的下标更加散列。

   补充知识：

   当length=8时    下标运算结果取决于哈希值的低三位

   当length=16时  下标运算结果取决于哈希值的低四位

   当length=32时  下标运算结果取决于哈希值的低五位

   当length=2的N次方， 下标运算结果取决于哈希值的低N位


2. 然后根据hash值拿node：

   ```java
   final Node<K,V> getNode(int hash, Object key) {
       Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
       if ((tab = table) != null && (n = tab.length) > 0 &&
           (first = tab[(n - 1) & hash]) != null) {
           if (first.hash == hash && // always check first node
               ((k = first.key) == key || (key != null && key.equals(k))))
               return first;
           if ((e = first.next) != null) {
               if (first instanceof TreeNode)
                   return ((TreeNode<K,V>)first).getTreeNode(hash, key);
               do {
                   if (e.hash == hash &&
                       ((k = e.key) == key || (key != null && key.equals(k))))
                       return e;
               } while ((e = e.next) != null);
           }
       }
       return null;
   }
   ```

   注意：

   first = tab[(n - 1) & hash] 这段代码是用来计算 key 在 tab 中索引位置。

   - **&运算速度快，至少比%取模运算块**
   - **能保证 索引值 肯定在 capacity 中，不会超出数组长度**
   - **(n - 1) & hash，当n为2次幂时，会满足一个公式：(n - 1) & hash = hash % n**



   所以，table size必须是2的整数幂。

   ​

   如果table位空则返回空；否则拿到hash表的第一个node，如果第一个是此node，返回这个node；否则判断是链表还是数结构，按照链表或数再遍历或数查找拿到node。

```java
/**
* 这个方法是TreeNode类的一个实例方法，调用该方法的也就是一个TreeNode对象，
* 该对象就是树上的某个节点，以该节点作为根节点，查找其所有子孙节点，
* 看看哪个节点能够匹配上给定的键对象
* h k的hash值
* k 要查找的对象
* kc k的Class对象，该Class应该是实现了Comparable<K>的，否则应该是null，参见：
*/
final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
    TreeNode<K,V> p = this; // 把当前对象赋给p，表示当前节点
    do { // 循环
        int ph, dir; K pk; // 定义当前节点的hash值、方向（左右）、当前节点的键对象
        TreeNode<K,V> pl = p.left, pr = p.right, q; // 获取当前节点的左孩子、右孩子。定义一个对象q用来存储并返回找到的对象
        if ((ph = p.hash) > h) // 如果当前节点的hash值大于k得hash值h，那么后续就应该让k和左孩子节点进行下一轮比较
            p = pl; // p指向左孩子，紧接着就是下一轮循环了
        else if (ph < h) // 如果当前节点的hash值小于k得hash值h，那么后续就应该让k和右孩子节点进行下一轮比较
            p = pr; // p指向右孩子，紧接着就是下一轮循环了
        else if ((pk = p.key) == k || (k != null && k.equals(pk))) // 如果h和当前节点的hash值相同，并且当前节点的键对象pk和k相等（地址相同或者equals相同）
            return p; // 返回当前节点
 
 
        // 执行到这里说明 hash比对相同，但是pk和k不相等
 
        else if (pl == null) // 如果左孩子为空
            p = pr; // p指向右孩子，紧接着就是下一轮循环了
        else if (pr == null)
            p = pl; // p指向左孩子，紧接着就是下一轮循环了
 
        // 如果左右孩子都不为空，那么需要再进行一轮对比来确定到底该往哪个方向去深入对比
        // 这一轮的对比主要是想通过comparable方法来比较pk和k的大小     
        else if ((kc != null ||
                    (kc = comparableClassFor(k)) != null) &&
                    (dir = compareComparables(kc, k, pk)) != 0)
            p = (dir < 0) ? pl : pr; // dir小于0，p指向右孩子，否则指向右孩子。紧接着就是下一轮循环了
 
        // 执行到这里说明无法通过comparable比较  或者 比较之后还是相等
        // 从右孩子节点递归循环查找，如果找到了匹配的则返回    
        else if ((q = pr.find(h, k, kc)) != null) 
            return q;
        else // 如果从右孩子节点递归查找后仍未找到，那么从左孩子节点进行下一轮循环
            p = pl;
    } while (p != null); 
    return null; // 为找到匹配的节点返回null
}

```



3. 如果key存在，则返回value，否则返回null



put()方法：

1. 首先判断哈希表是否位空，为空则新建一个hash表（默认是16的size）

2. 用key计算出的hash值，查询该hash值下的地址是否有值，若无则创建一个新的数据到此位置

3. 若该hash值下有值，比较两个hash值是否相同，若相同，说明此key已经存在，只需替换value即可

4. 若hash值不一样，则说明hash冲突，需要判断是红黑树（treenode）还是链表（list）

   1. 若是链表，则iterator迭代，直到key相同，则替换value，若迭代完了还是没有这个key，则需要再添加节点。添加完再检查此链表**是否大于等于8**，若是，则转为红黑数（treeifyBin方法）
   2. 若是红黑树，则调用putTreeVal方法进行操作

5. 判断是否要扩容，defaul值是**16**。


```java
public V put(K key, V value) {
   return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        //声明了一个局部变量 tab,局部变量 Node 类型的数据 p,int 类型 n,i
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //首先将当前 hashmap 中的 table(哈希表)赋值给当前的局部变量 tab,然后判断tab 是不是空或者长度是不是 0,实际上就是判断当前 hashmap 中的哈希表是不是空或者长度等于 0
        if ((tab = table) == null || (n = tab.length) == 0)
        //如果是空的或者长度等于0,代表现在还没哈希表,所以需要创建新的哈希表,默认就是创建了一个长度为 16 的哈希表
            n = (tab = resize()).length;
        //将当前哈希表中与要插入的数据位置对应的数据取出来,(n - 1) & hash])就是找当前要插入的数据应该在哈希表中的位置,如果没找到,代表哈希表中当前的位置是空的,否则就代表找到数据了, 并赋值给变量 p
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);//创建一个新的数据,这个数据没有下一条,并将数据放到当前这个位置
        else {//代表要插入的数据所在的位置是有内容的
        //声明了一个节点 e, 一个 key k
            Node<K,V> e; K k;
            if (p.hash == hash && //如果当前位置上的那个数据的 hash 和我们要插入的 hash 是一样,代表没有放错位置
            //如果当前这个数据的 key 和我们要放的 key 是一样的,实际操作应该是就替换值
                ((k = p.key) == key || (key != null && key.equals(k))))
                //将当前的节点赋值给局部变量 e
                e = p;
            else if (p instanceof TreeNode)//如果当前节点的 key 和要插入的 key 不一样,然后要判断当前节点是不是一个红黑色类型的节点
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);//如果是就创建一个新的树节点,并把数据放进去
            else {
                //如果不是树节点,代表当前是一个链表,那么就遍历链表
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {//如果当前节点的下一个是空的,就代表没有后面的数据了
                        p.next = newNode(hash, key, value, null);//创建一个新的节点数据并放到当前遍历的节点的后面
                        if (binCount >= TREEIFY_THRESHOLD - 1) // 重新计算当前链表的长度是不是超出了限制，TREEIFY_THRESHOLD默认是8
                            treeifyBin(tab, hash);//超出了之后就将当前链表转换为树,注意转换树的时候,如果当前数组的长度小于MIN_TREEIFY_CAPACITY(默认 64),会触发扩容,我个人感觉可能是因为觉得一个节点下面的数据都超过8 了,说明 hash寻址重复的厉害(比如数组长度为 16 ,hash 值刚好是 0或者 16 的倍数,导致都去同一个位置),需要重新扩容重新 hash
                        break;
                    }
                    //如果当前遍历到的数据和要插入的数据的 key 是一样,和上面之前的一样,赋值给变量 e,下面替换内容
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { //如果当前的节点不等于空,
                V oldValue = e.value;//将当前节点的值赋值给 oldvalue
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value; //将当前要插入的 value 替换当前的节点里面值
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;//增加长度
        if (++size > threshold)
            resize();//如果当前的 hash表的长度已经超过了当前 hash 需要扩容的长度, 重新扩容,条件是 haspmap 中存放的数据超过了临界值(经过测试),而不是数组中被使用的下标
        afterNodeInsertion(evict);
        return null;
    }
```



treeifbin方法

```java
final void treeifyBin(Node<K,V>[] tab, int hash) {
        int n, index; Node<K,V> e;
        logger.info("n = tab.length:" + tab.length);
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY) //小于最小默认树结构容量MIN_TREEIFY_CAPACITY（默认是64）时进行扩容
        {
            resize();
        }
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            TreeNode<K,V> hd = null, tl = null;
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }
```

treeif方法

```java
		final void treeify(Node<K,V>[] tab) {
            // root节点
            TreeNode<K,V> root = null;
            // 遍历TreeNode链
            for (TreeNode<K,V> x = this, next; x != null; x = next) {
                // next　下一个节点
                next = (TreeNode<K,V>)x.next;
                // 设置左右节点为空
                x.left = x.right = null;
                // 第一次进入循环　root　== null，确定头结点，为黑色
                if (root == null) {
                    // 将根节点的父节点设置位空
                    x.parent = null;
                    // 将根节点设置为黑色
                    x.red = false;
                    //将x 设置为根节点
                    root = x;
                }
                // 后面进入循环走的逻辑，x 指向树中的某个节点。 此处为非根节点
                else {
                    //　获取当前循环节点key
                    K k = x.key;
                    // 获取当前节点 hash
                    int h = x.hash;
                    Class<?> kc = null;
                    // 从根节点开始验证，遍历所有节点跟当前节点 x 比较，调整位置，有点像冒泡排序
                    for (TreeNode<K,V> p = root;;) {
                        // 循环查找当前节点插入的位置并添加节点
                        int dir, ph;
                        // 每个节点的 key
                        K pk = p.key;
                        // hashMap元素的hash值用来表示红黑树中节点数值大小
                        if ((ph = p.hash) > h)
                            // 当前节点值小于根节点，dir = -1 沿左路径查找
                            dir = -1;
                        else if (ph < h)
                            // 当前节点值大于根节点, dir = 1 沿右路径查找
                            dir = 1;
                        // 如果存在比较对象，则根据比较对象定义的comparable进行比较
            			// 比较之后返回查询节点路径（左或右）
                        else if ((kc == null &&
                                  (kc = comparableClassFor(k)) == null) ||
                                 (dir = compareComparables(kc, k, pk)) == 0)
                            // 当前节点的值等于根节点值。
                            // 如果当前节点实现Comparable接口，调用compareTo比较大小并赋值dir
                            // 如果当前节点没有实现Comparable接口，compareTo结果等于0，则调用tieBreakOrder继续比较大小
                            // tieBreakOrder本质是通过比较k与pk的hashcode
                            dir = tieBreakOrder(k, pk);
						// 当前“根节点”赋值给xp
                        TreeNode<K,V> xp = p;
                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
                            // 如果当前节点小于根节点且左子节点为空 或者  当前节点大于根节点且右子节点为空，直接添加子节点
                            // 将px设置为ｘ的父节点
                            x.parent = xp;
                            if (dir <= 0)
                                xp.left = x;
                            else
                                xp.right = x;
                            // 平衡红黑树，将二叉树转换位红黑树－正式转换红黑树
                            root = balanceInsertion(root, x);
                            // 跳出循环，继续向红黑树添加下一个元素
                            break;
                        }
                    }
                }
            }
            // 确保红黑树根节点是数组中该index的第一个节点
            moveRootToFront(tab, root);
        }
```


resize方法：

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}

```

迭代时间与HashMap实例的”容量“（桶的数量）及其大小（键-值映射关系数）成比例，初始容量和加载因子影响其性嫩。初始容量指的是哈希表在创建时的容量。加载因子是哈希表在其容量自动增加之前可以达到多满的尺度。当哈希表中的条目超过加载因子与当前容量的成绩，则对该哈希表进行rehash操作（重建内部数据结构），将其扩大大约两倍的桶数。

加载因子过大会增加查询成本，但减少了空间开销。初始容量过大会浪费空间，过小则增加很少条目就需要rehash。

**可以存储null键和null值**

初始size为**16**，扩容：newsize = oldsize*2，size一定为2的n次幂

ConcurrentHashMap是使用了锁分段技术来保证线程安全的。



**HashTable**

同步的， 线程安全

无论key还是value都**不能为null**

初始size为**11**，扩容：newsize = olesize*2+1



线程安全的集合：



1. Vector

Vector和ArrayList类似，是长度可变的数组，与ArrayList不同的是，Vector是线程安全的，它给几乎所有的public方法都加上了synchronized关键字。由于加锁导致性能降低，在不需要并发访问同一对象时，这种强制性的同步机制就显得多余，所以现在Vector已被弃用

2. HashTable

HashTable和HashMap类似，不同点是HashTable是线程安全的，它给几乎所有public方法都加上了synchronized关键字，还有一个不同点是HashTable的K，V都不能是null，但HashMap可以，它现在也因为性能原因被弃用了。



3. 用collection包装方法

```java
List<E> synArrayList = Collections.synchronizedList(new ArrayList<E>());
Set<E> synHashSet = Collections.synchronizedSet(new HashSet<E>());
Map<K,V> synHashMap = Collections.synchronizedMap(new HashMap<K,V>());

```



4. ConcurrentHashMap

ConcurrentHashMap取消了segment分段锁，而采用CAS和synchronized来保证并发安全。数据结构跟HashMap1.8的结构一样，**数组+链表/红黑二叉树**。
 synchronized只锁定当前链表或红黑二叉树的首节点，这样只要hash不冲突，就不会产生并发，效率又提升N倍。

JDK1.8的ConcurrentHashMap的结构图如下：

![img](https://upload-images.jianshu.io/upload_images/2843224-59ca97f3ae25b043.png?imageMogr2/auto-orient/strip|imageView2/2)



琐

### 多线程 Mutil-Thread

https://www.cnblogs.com/snow-flower/p/6114765.html

线程生命周期

![1588242816337](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\1588242816337.png)



线程定义：

一个程序内部的顺序控制流。

线程与进程的区别：

1. 每个进程都有独立的代码和数据空间，进程间的切换会有较大的开销

2. 线程可以看成是轻量级的进程，同一类线程共享代码和数据空间，没个线程有动力的允许栈和程序技术器，线程切换的开销小。

3. 多线程：在同一应用程序中有多个顺序流同时执行。
4. 多进程：在操作系统中能同时允许多个任务或程序。



创建线程和启动

1. 继承Thread类创建线程类
   1. 定义一个继承Thread类的子类，并重写该类的run()方法
   2. 创建Thread子类的实例（对象）
   3. 调用该线程对象的start()方法启动线程

```java
 class SomeThead extends Thraad   { 
    public void run()   { 
     //do something here  
    }  
 } 
 
public static void main(String[] args){
 SomeThread oneThread = new SomeThread();   
 // 步骤3：启动线程：   
 oneThread.start(); 
}
```

2. 实现Runnable接口创建线程类

```java
class SomeRunnable implements Runnable   { 
  public void run()   { 
  //do something here  
  }  
} 
Runnable oneRunnable = new SomeRunnable();   
Thread oneThread = new Thread(oneRunnable);   
oneThread.start(); 
```

3. 通过Callable和Futuer创建线程

```java
public interface Callable   { 
  V call() throws Exception;  
 } 
  // 步骤1：创建实现Callable接口的类SomeCallable(略);   
  // 步骤2：创建一个类对象： 
  Callable oneCallable = new SomeCallable(); 
  // 步骤3：由Callable创建一个FutureTask对象：   
  FutureTask oneTask = new FutureTask(oneCallable); 
  // 注释： FutureTask是一个包装器，它通过接受Callable来创建，它同时实现了 Future和Runnable接口。 
  // 步骤4：由FutureTask创建一个Thread对象：   
  Thread oneThread = new Thread(oneTask);   
  // 步骤5：启动线程：  
  oneThread.start(); 
```



### 线程管理

#### 线程睡眠 sleep

sleep是静态方法，最好不要用Thread的实例对象调用它，因为它睡眠的始终是当前正在运行的线程，而不是调用它的线程对象，它只对正在运行状态的线程对象有效。如下面的例子

```java
public class Test1 {  
    public static void main(String[] args) throws InterruptedException {  
        System.out.println(Thread.currentThread().getName());  
        MyThread myThread=new MyThread();  
        myThread.start();  
        myThread.sleep(1000);//这里sleep的就是main线程，而非myThread线程  
        Thread.sleep(10);  
        for(int i=0;i<100;i++){  
            System.out.println("main"+i);  
        }  
    }  
} 
```

#### 线程让步 yield

yield()方法和sleep()方法有点相似，它也是Thread类提供的一个静态的方法，它也可以让当前正在执行的线程暂停，让出cpu资源给其他的线程。但是和sleep()方法不同的是，它不会进入到阻塞状态，而是进入到就绪状态。yield()方法只是让当前线程暂停一下，重新进入就绪的线程池中，让系统的线程调度器重新调度器重新调度一次，完全可能出现这样的情况：当某个线程调用yield()方法之后，线程调度器又将其调度出来重新进入到运行状态执行。

sleep方法暂停当前线程后，会进入阻塞状态，只有当睡眠时间到了，才会转入就绪状态。而yield方法调用后 ，是直接进入就绪状态，所以有可能刚进入就绪状态，又被调度到运行状态。

#### 线程合并 join

线程的合并的含义就是将几个并行线程的线程合并为一个单线程执行，应用场景是当一个线程必须等待另一个线程执行完毕才能执行时，Thread类提供了join方法来完成这个功能，注意，它不是静态方法。
从上面的方法的列表可以看到，它有3个重载的方法：

```java
void join()      
     当前线程等该加入该线程后面，等待该线程终止。    
void join(long millis)  
     当前线程等待该线程终止的时间最长为 millis 毫秒。 如果在millis时间内，该线程没有执行完，那么当前线程进入就绪状态，重新等待cpu调度  
void join(long millis,int nanos)   
     等待该线程终止的时间最长为 millis 毫秒 + nanos 纳秒。如果在millis时间内，该线程没有执行完，那么当前线程进入就绪状态，重新等待cpu调度  
```

#### 优先级 setPriority(int newPriority)和getPriority()

每个线程执行时都有一个优先级的属性，优先级高的线程可以获得较多的执行机会，而优先级低的线程则获得较少的执行机会。与线程休眠类似，线程的优先级仍然无法保障线程的执行次序。只不过，优先级高的线程获取CPU资源的概率较大，优先级低的也并非没机会执行。



#### 结束线程

Thread.stop()、Thread.suspend、Thread.resume、Runtime.runFinalizersOnExit这些终止线程运行的方法已经被废弃了，使用它们是极端不安全的！想要安全有效的结束一个线程，可以使用下面的方法：

​    • 正常执行完run方法，然后结束掉；

​    • 控制循环条件和判断条件的标识符来结束掉线程。

```java
class MyThread extends Thread {  
    int i=0;  
    boolean next=true;  
    @Override  
    public void run() {  
        while (next) {  
            if(i==10)  
                next=false;  
            i++;  
            System.out.println(i);  
        }  
    }  
}
```



### 同步

synchronized 修饰方法

1. 锁的是什么：synchronized锁住的是括号里的对象，而不是代码。对于非static的synchronized方法，锁的就是对象本身也就是this。

volatile 修饰变量

volatile的特性

1. 保证不同线程对这个遍历进行操作的可见性，即一个线程修改了某个变量的值，此新值对其他线程来说是立即可见的（实现可见性）

    1. 从内存读写数据比CPU 执行指令的速度慢很多，所以通常程序运行时会把需要的数据从主内存复制一份到CPU 告诉缓存中，当运算结束后再把高速缓存中的数据刷新到主存中，但如果多线程情况下可能会存在**缓存不一致**的情况：

       ```java
       i = i + 1
       ```

       假设i初始为0，如果线程1调用上述操作，i = 1，此时线程2也调用这一代码，i还是1，写入内存的i值为1而不是2，虽然i = i+1被调用了两次。

       	2. 解决缓存不一致：
   	
       	     	1. 总线加Lock，效率低下
   	
       	    2. 通过缓存一致性协议，如果发现时共享变量，会通知其他CPU将该变量的缓存置为无效状态。其他cpu读取时，发现自己缓存中的变量无效，就会从内存中重新读取

2. 禁止进行指令重排序（实现有序性）

   1. 指令重排序：一般来说，处理器为了提高程序运行效率，可能会对输入代码进行优化，它不保证程序中各个语句的执行先后顺序同代码中的顺序一致，但是它会保证程序最终执行结果和代码顺序执行的结果是一致的。

   ```java
   //线程1:
   context = loadContext();   //语句1
   inited = true;             //语句2
    
   //线程2:
   while(!inited ){
     sleep()
   }
   doSomethingwithconfig(context);
   ```

   上面代码中，由于语句1和语句2没有数据依赖性，因此可能会被重排序。假如发生了重排序，在线程1执行过程中先执行语句2，而此是线程2会以为初始化工作已经完成，那么就会跳出while循环，去执行doSomethingwithconfig(context)方法，而此时context并没有被初始化，就会导致程序出错。

   从上面可以看出，指令重排序不会影响单个线程的执行，但是会影响到线程并发执行的正确性。指令重排序的前提是数据之间没有依赖性

   2. 如何实现禁止指令重排序：

3. 但volatile不能保证原子性

   1. 什么叫原子性：一次操作或者多次操作中，要么所有的操作全部都得到了执行并且不会受到任何因素的干扰而中断，要么所有的操作都不执行。比如：

      ```java
      // 以下只有x = 10 具有原子性
      x = 10;
      y = x;
      x++;
      x = x + 1;
      ```


   2. 如何解决原子性问题：用synchronized，锁机制或者使用原子类（如 AtomicInteger）

      示例：

      ```java
      // 错误示范
      public class Test {
          public volatile int inc = 0;
           
          public void increase() {
              inc++;
          }
           
          public static void main(String[] args) {
              final Test test = new Test();
              for(int i=0;i<10;i++){
                  new Thread(){
                      public void run() {
                          for(int j=0;j<1000;j++)
                              test.increase();
                      };
                  }.start();
              }
               
              while(Thread.activeCount()>1)  //保证前面的线程都执行完
                  Thread.yield();
              System.out.println(test.inc);
          }
      }


      // 用synchronized
      public class Test {
          public volatile int inc = 0;
           
          public synchronized void increase() {
              inc++;
          }
           
          public static void main(String[] args) {
              final Test test = new Test();
              for(int i=0;i<10;i++){
                  new Thread(){
                      public void run() {
                          for(int j=0;j<1000;j++)
                              test.increase();
                      };
                  }.start();
              }
               
              while(Thread.activeCount()>1)  //保证前面的线程都执行完
                  Thread.yield();
              System.out.println(test.inc);
          }
      }
    
      // 用Lock
      public class Test {
          public  int inc = 0;
          Lock lock = new ReentrantLock();
          
          public  void increase() {
              lock.lock();
              try {
                  inc++;
              } finally{
                  lock.unlock();
              }
          }
          
          public static void main(String[] args) {
              final Test test = new Test();
              for(int i=0;i<10;i++){
                  new Thread(){
                      public void run() {
                          for(int j=0;j<1000;j++)
                              test.increase();
                      };
                  }.start();
              }
              
              while(Thread.activeCount()>1)  //保证前面的线程都执行完
                  Thread.yield();
              System.out.println(test.inc);
          }
      }
    
      // 用AtomicInteger
      public class Test {
          public  AtomicInteger inc = new AtomicInteger();
           
          public  void increase() {
              inc.getAndIncrement();
          }
          
          public static void main(String[] args) {
              final Test test = new Test();
              for(int i=0;i<10;i++){
                  new Thread(){
                      public void run() {
                          for(int j=0;j<1000;j++)
                              test.increase();
                      };
                  }.start();
              }
              
              while(Thread.activeCount()>1)  //保证前面的线程都执行完
                  Thread.yield();
              System.out.println(test.inc);
          }
      }
      ```
    
      注：AtomicInteger的getAndIncrement方法是通过cas算法实现的，即无锁机制



volatile的使用场景

1. 状态标记量

   ```java
   volatile boolean flag = false;
    
   while(!flag){
       doSomething();
   }
    
   public void setFlag() {
       flag = true;
   }
   ```

   1. double check Lock（DCL）

   ```java
   class Singleton{
       private volatile static Singleton instance = null;
        
       private Singleton() {
            
       }
        
       public static Singleton getInstance() {
           // 如果instance为空，表示此时是首次使用instance，需要锁住它
           // 如果其他thread看到instance不为空，表示instance已经被创建了，就直接得到这个实例，避免再次锁
           if(instance==null) {
               synchronized (Singleton.class) {
                   // 如果有两个thread同时请求getInstance（），且instance为空，则出现锁竞争的问题
                   // 如果A线程率先获得锁，进入代码块创建Singleton（），然后释放锁
                   // 线程B发现instance已经被创建了，直接释放锁
                   if(instance==null)
                       instance = new Singleton();
               }
           }
           return instance;
       }
   }



       public   class  UserCacheDBService {  
             
           private   volatile  Map<Long, UserDO> map =  new  ConcurrentHashMap<Long, UserDO>();  
           private  Object mutex =  new  Object();  
         
           /**  
            * 取用户数据，先从缓存中取，缓存中没有再从DB取  
            * @param userId  
            * @return  
            */   
           public  UserDO getUserDO(Long userId) {  
               UserDO userDO = map.get(userId);  
                 
               if (userDO ==  null ) {                            ① check  
                   synchronized (mutex) {                       ② lock  
                       if  (!map.containsKey(userId)) {        ③ check  
                           userDO = getUserFromDB(userId);    ④ act  
                           map.put(userId, userDO);  
                       }  
                   }  
               }  
                 
               if (userDO ==  null ) {                             ⑤  
                   userDO = map.get(userId);  
               }  
                 
               return  userDO;  
           }  
         
           private  UserDO getUserFromDB(Long userId) {  
               // TODO Auto-generated method stub   
               return   null ;  
           }  
       }  
   ```

   1. 对于多线程编程来说，同步问题是我们需要考虑的最多的问题，同步的锁什么时候加，加在哪里都需要考虑，当然在不影响功能的情况下，同步越少越好，锁加的越迟越优是我们都必须认同的。DCL（Double Check Lock）就是为了达到这个目的。
   2. DCL简单来说就是check-lock-check-act，先检查再锁，锁之后再检查一次，最后才执行操作。这样做的目的是尽可能的推迟锁的时间。

创建锁进行同步

```java
 ReentrantLock() : 创建一个ReentrantLock实例         
 lock() : 获得锁        
 unlock() : 释放锁
   ```



### 线程池

线程使得应用更加充分合理得协调利用CPU、内存、网络、I/O等系统资源。

线程的创建需要开辟虚拟机栈、本地方法栈、程序计数器等线程私有的内存空间。

在线程销毁时需要回收这些系统资源。

频繁地创建和销毁线程会浪费大量的系统资源，增加并发编程风险。



当服务器负载过大的时候，如何让新的线程等待或友好得拒绝服务？

使用线程池去协调多个线程，并实现类似主次线程隔离、定时执行、周期执行等任务。



线程池的作用包括：

1. 利用线程池管理并复用线程、控制最大并发数
2. 实现任务线程队列缓存策略和拒绝机制
3. 实现某些与实践相关的功能，比如定时执行、周期执行等
4. 隔离线程环境。比如隔开交易服务和搜索服务。



在开发中，合理使用线程池能带来3个好处：

1. 降低资源消耗：提高复用率，降低创建、销毁线程造成的系统消耗
2. 提高响应速度：当任务到达时，任务可以不需要等到线程创建就能立即执行
3. 提高线程的可管理性：线程池统一分配、调优和监控。



创建线程池

```java
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), defaultHandler);
    }

```



不同ThreadPoolExecutor的区别

参数详解：

**int corePoolSize** ：表示常驻核心线程数

corePoolSize the number of threads to keep in the pool, even if they are idle, unless {@code allowCoreThreadTimeOut} is set.

corePoolSize 表示能够常驻在池中的线程数量，即使这些常驻的线程没有被使用（idle，虚度），除非allowCoreThreadTimeOut = true。如果指定ThreadPoolExecutor的 allowCoreThreadTimeOut 这个属性为true，那么核心线程如果不干活(闲置状态)的话，超过一定时间( keepAliveTime)，就会被销毁掉



**long keepAliveTime**：非核心线程闲置时时长。

when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.

当线程数量大于常驻核心线程数（corePoolSize），keepAliveTime表示那之后加入的线程空闲等待多久才会被终止（terminating）。但是，如果设置了  allowCoreThreadTimeOut = true，则会作用于核心线程。

注意：一个非核心线程，如果不干活(闲置状态)的时长，超过这个参数所设定的时长，就会被销毁掉。



**TimeUnit unit** ：时间单位

TimeUnit是一个枚举类型， 包括： MILLISECONDS ： 1毫秒 、SECONDS ： 秒、MINUTES ： 分、HOURS ： 小时、DAYS ： 天



**int maximumPoolSize**  ：线程池中线程总数的最大值

The maximum number of threads to allow in the pool.

线程总数 = 核心线程数 + 非核心线程数。



**BlockingQueue<Runnable>  workQueue** : 阻塞任务队列

workQueue the queue to use for holding tasks before they are executed.  This queue will hold only the {@code Runnable} tasks submitted by the {@code execute} method.

workQueue表示排队等待执行的任务。这个队列只会用于已经被执行过execute的Runnable的task。

当线程池中的线程数超过它的corePoolSize的时候，线程会进入阻塞队列进行阻塞等待。通过workQueue，线程池实现了阻塞功能。

1）有界任务队列ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；

2）无界任务队列LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；

3）直接提交队列synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。



**ThreadFactory threadFactory** : 线程工厂

the factory to use when the executor creates a new thread

当executor执行时用来创建一个新的线程。



**RejectedExecutionHandler handler**：

the handler to use when execution is blocked because the thread bounds and queue capacities are reached

当线程数量达到最大线程数量（maximumPoolSize）或者线程等待队列达到最大队列容量时（即线程被拒绝）的策略。





**IllegalArgumentException** if one of the following holds：

{@code corePoolSize < 0}

{@code keepAliveTime < 0}

{@code maximumPoolSize <= 0}

{@code maximumPoolSize < corePoolSize}



**NullPointerException** if {@code workQueue} is null



### 线程死锁

定义：两个或以上的线程互相持有对方所需要的资源，互相等待，造成死锁。

死锁产生的条件：

1. 互斥：一个资源或者一个琐只能被一个线程所占有。在持有锁的线程没有释放之前，其他线程无法获取此锁。
2. 占有且等待：一个线程已经获取到了一个锁，在获取另一个锁的过程中，即使获取不到也不会释放已经获得的锁。
3. 不可剥夺：其他线程无法强制获取别的线程已经持有的锁。
4. 循环等待：线程A持有线程B的锁，线程B持有线程A的锁。

如何避免：

加锁顺序：线程按照相同的顺序加锁。

加锁时限：一定时间内获取不到抛出异常。

死锁检测：

```shell
jps -l
jstack + pid
```

```shell

PS D:\redisPractice\demo> jps -l
772 DeadLock
11212 sun.tools.jps.Jps
396 org.jetbrains.idea.maven.server.RemoteMavenServer36
8748
9900 org.jetbrains.idea.maven.server.RemoteMavenServer36
PS D:\redisPractice\demo> jstack 772
2021-12-30 15:51:49
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.211-b12 mixed mode):

"DestroyJavaVM" #14 prio=5 os_prio=0 tid=0x00000000029c3000 nid=0x3144 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Thread-1" #13 prio=5 os_prio=0 tid=0x000000001f233800 nid=0x260c waiting for monitor entry [0x000000001fbff000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at DeadLock.method2(DeadLock.java:20)
        - waiting to lock <0x000000076b4210c0> (a java.lang.Object)
        - locked <0x000000076b4210d0> (a java.lang.Object)
        at DeadLock.lambda$main$1(DeadLock.java:37)
        at DeadLock$$Lambda$2/990368553.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)

"Thread-0" #12 prio=5 os_prio=0 tid=0x000000001f230800 nid=0x34d0 waiting for monitor entry [0x000000001faff000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at DeadLock.method1(DeadLock.java:11)
        - waiting to lock <0x000000076b4210d0> (a java.lang.Object)
        - locked <0x000000076b4210c0> (a java.lang.Object)
        at DeadLock.lambda$main$0(DeadLock.java:30)
        at DeadLock$$Lambda$1/2003749087.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)

"Service Thread" #11 daemon prio=9 os_prio=0 tid=0x000000001e470800 nid=0x34ec runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread3" #10 daemon prio=9 os_prio=2 tid=0x000000001e42b000 nid=0x2fa8 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

```



### 反射 Reflection

JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意方法和属性；这种动态（dynamic ）获取信息以及动态调用对象方法的功能称为java语言的反射机制。

例子：

```java
public class Apple {

    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static void main(String[] args) throws Exception{
        //正常的调用
        Apple apple = new Apple();
        apple.setPrice(5);
        System.out.println("Apple Price:" + apple.getPrice());
        //使用反射调用
        
        //获取类的class对象实例
        Class clz = Class.forName("com.chenshuyi.api.Apple");
        
        //获取方法的Method对象
        Method setPriceMethod = clz.getMethod("setPrice", int.class);
        
        //根据对象实例获取Constructor对象
        Constructor appleConstructor = clz.getConstructor();
        
        //使用Constructor对象的newInstance方法获取反射类对象
        Object appleObj = appleConstructor.newInstance();
        
        //利用invoke方法调用方法
        setPriceMethod.invoke(appleObj, 14);
        Method getPriceMethod = clz.getMethod("getPrice");
        System.out.println("Apple Price:" + getPriceMethod.invoke(appleObj));
    }
}
```





### 反射好处

RTTI和反射之间真正的区别只在于，对RTTI来说，编译器在在编译时打开和检查.class文件.(换句话说，我们可以用"普通"方式调用对象的所有方法).对于反射机制来说，.class文件在编译时是不可获取的，所以在运行时打开和检查.class文件。配置性大大提高，如同Spring IOC容器，给很多配置设置参数，使得java应用程序能够顺利跑起来，大大提高了Java的灵活性和可配置性，降低模块间的耦合。

实例：

假如现在我有个写好的程序已经放在了服务器上，每天供人家来访问，这时候Mysql数据库宕掉了，改用Oracle,这时候该怎么怎么办呢？假如没有反射的话，我们是不是得修改代码，将Mysql驱动改为Oracle驱动，重新编译运行，再放到服务器上。是不是很麻烦，还影响用户的访问。

假如我们使用反射Class.forName()来加载驱动，只需要修改配置文件就可以动态加载这个类，Class.forName()生成的结果在编译时是不可知的，只有在运行的时候才能加载这个类，换句话说，此时我们是不需要将程序停下来，只需要修改配置文件里面的信息就可以了。这样当有用户在浏览器访问这个网站时，都不会感觉到服务器程序程序发生了变化。



### 反射目的

1. 获取反射Class的对象
2. 通过反射创建类对象实例
3. 反射获取属性、调用方法或构造器

https://juejin.im/post/5c9a4b10f265da60f6731913



#### 获取反射中的Class对象

三种方法

1. 使用Clss.forName静态方法

```java
Class clz = Class.forName("java.lang.String");
```

2. 使用.class方法

```java
Class clz = String.class;
```

3. 使用类对象的getClass()方法

```java
String str = new String("Hello");
Class clz = str.getClass();
```



#### 通过反射创建类对象

1. 通过Class对象的newInstance()方法

```java
Class clz = Apple.class;
Apple apple = (Apple)clz.newInstance();
```

2. 通过Constructor对象的newInstance()方法

```java
Class clz = Apple.class;
Constructor constructor = clz.getConstructo();
Apple apple = (Apple)constructor.newInstance();
```

constructor对象创建类对象可以选择特定构造方法（比如有参数的构造方法）

```java
Class clz = Apple.class;
Constructor constructor = clz.getConstructor(String.class, int.class);
Apple apple = (Apple)constructor.newInstance("红富士", 15);
```



#### 通过反射获取类属性、方法、构造器

1. getFields()方法可以获取 Class 类的属性，但无法获取私有属性。
2. getDeclaredFields()方法则可以获取包括私有属性在内的所有属性。

```java
Class clz = Apple.class;
Field[] fields = clz.getFields();
for (Field field : fields) {
    System.out.println(field.getName());
}

Class clz = Apple.class;
Field[] fields = clz.getDeclaredFields();
for (Field field : fields) {
    System.out.println(field.getName());
}
```



悲观锁：

总是假设最坏的情况，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会阻塞直到它拿到锁（共享资源每次只给一个线程使用，其它线程阻塞，用完后再把资源转让给其它线程）。传统的关系型数据库里边就用到了很多这种锁机制，比如行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁。Java中synchronized和ReentrantLock等独占锁就是悲观锁思想的实现。



乐观锁：

总是假设最好的情况，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，可以使用版本号机制和CAS算法实现。乐观锁适用于多读的应用类型，这样可以提高吞吐量，像数据库提供的类似于write_condition机制，其实都是提供的乐观锁。在Java中java.util.concurrent.atomic包下面的原子变量类就是使用了乐观锁的一种实现方式CAS实现的。



版本号机制或者时间戳机制：
一般是在数据表中加上一个数据版本号version字段，表示数据被修改的次数，当数据被修改时，version值会加一。当线程A要更新数据值时，在读取数据的同时也会读取version值，在提交更新时，若刚才读取到的version值为当前数据库中的version值相等时才更新，否则重试更新操作，直到更新成功。

```sql
update t_goods 
set status=2,version=version+1
where id=#{id} and version=#{version};
```

Synchronized：底层使用指令码方式来控制锁的，映射成字节码指令就是增加来两个指令：monitorenter和monitorexit。当线程执行遇到monitorenter指令时会尝试获取内置锁，如果获取锁则锁计数器+1，如果没有获取锁则阻塞；当遇到monitorexit指令时锁计数器-1，如果计数器为0则释放锁。

Lock：底层是CAS乐观锁，依赖AbstractQueuedSynchronizer类，把所有的请求线程构成一个CLH队列。而对该队列的操作均通过Lock-Free（CAS）操作。

Synchronized是关键字，内置语言实现，Lock是接口。

Synchronized在线程发生异常时会自动释放锁，因此不会发生异常死锁。Lock异常时不会自动释放锁，所以需要在finally中实现释放锁。

Lock是可以中断锁，Synchronized是非中断锁，必须等待线程执行完成释放锁。

Lock可以使用读锁提高多线程读效率。



CAS算法：

即compare and swap（比较与交换），是一种有名的无锁算法。无锁编程，即不使用锁的情况下实现多线程之间的变量同步，也就是在没有线程被阻塞的情况下实现变量的同步，所以也叫非阻塞同步（Non-blocking Synchronization）。CAS算法涉及到三个操作数：

当前内存值 V
旧的预期值 A
拟写入的新值 B

当且仅当 V 的值等于 A时，CAS通过原子方式用新值B来更新V的值，否则不会执行任何操作（比较和替换是一个原子操作）。一般情况下是一个自旋操作，即不断的重试getAndIncrement方法的调用，直到修改成功。

对于资源竞争较少（线程冲突较轻）的情况，使用synchronized同步锁进行线程阻塞和唤醒切换以及用户态内核态间的切换操作额外浪费消耗cpu资源；而CAS基于硬件实现，不需要进入内核，不需要切换线程，操作自旋几率较少，因此可以获得更高的性能。
对于资源竞争严重（线程冲突严重）的情况，CAS自旋的概率会比较大，从而浪费更多的CPU资源，效率低于synchronized。

ABA问题：
ABA问题是CAS中的一个漏洞。CAS的定义，当且仅当内存值V等于就得预期值A时，CAS才会通过原子方式用新值B来更新V的值，否则不会执行任何操作。那么如果先将预期值A给成B，再改回A，那CAS操作就会误认为A的值从来没有被改变过，这时其他线程的CAS操作仍然能够成功，但是很明显是个漏洞，因为预期值A的值变化过了。

比如说一个线程 one 从内存位置 V 中取出 A，这时候另一个线程 two 也从内存中取出 A，并且two 进行了一些操作变成了 B，然后  two 又将 V 位置的数据变成 A，这时候线程 one 进行 CAS 操作发现内存中仍然是 A，然后 one 操作成功。尽管线程 one 的  CAS 操作成功，但是不代表这个过程就是没有问题的。

如何解决这个异常现象？java并发包为了解决这个漏洞，提供了一个带有标记的原子引用类“AtomicStampedReference”，它可以通过控制变量值的版本来保证CAS的正确性，即在变量前面添加版本号，每次变量更新的时候都把版本号+1，这样变化过程就从“A－B－A”变成了“1A－2B－3A”。



### 拷贝 Clone

浅拷贝ShallowClone

在浅克隆中，如果原型对象的成员变量是**值类型**，将复制一份给克隆对象；如果原型对象的成员变量是引用类型，则将**引用对象的地址**复制一份给克隆对象，也就是说原型对象和克隆对象的成员变量**指向相同的内存地址**。

**值类型**：

在Java语言中，数据类型分为值类型（基本数据类型）和引用类型，值类型包括int、double、byte、boolean、char等简单数据类型，引用类型包括类、接口、数组等复杂类型。**浅克隆和深克隆的主要区别在于是否支持引用类型的成员变量的复制**，下面将对两者进行详细介绍。

具体方法：

1. 被复制的类需要实现Clonenable接口（不实现的话在调用clone方法会抛出CloneNotSupportedException异常)， 该接口为标记接口(不含任何方法)

2. 覆盖clone()方法，访问修饰符设为public。方法中调用super.clone()方法得到需要的复制对象。（native为本地方法)

```java
class Student implements Cloneable{ 
	private int number;
    public int getNumber() {
    	return number;
	}
   public void setNumber(int number) {
   		this.number = number;
   }
   @Override public Object clone() {
   		Student stu = null;
   		try{
   			stu = (Student)super.clone();
   		}catch(CloneNotSupportedException e) {
   			e.printStackTrace(); 
   		}
        return stu; 
   	} 
  }
   public class Test {
   		public static void main(String args[]) {
   			Student stu1 = new Student();
   			stu1.setNumber(12345);
   			Student stu2 = (Student)stu1.clone();
   			System.out.println("学生1:" + stu1.getNumber());
   			System.out.println("学生2:" + stu2.getNumber());
   			stu2.setNumber(54321);
   			System.out.println("学生1:" + stu1.getNumber());
            System.out.println("学生2:" + stu2.getNumber());
   		}
   } 

```



深拷贝DeepClone

1. 重写clone()方法以及其属性的clone()方法。将object中不是值类型的属性也克隆一遍。

```java
 @Override public Object clone() {
 	Student stu = null;
 	try{
 		stu = (Student)super.clone(); //浅复制
 	}catch(CloneNotSupportedException e) {
 		e.printStackTrace();
 	}
 	stu.addr = (Address)addr.clone(); //深度复制
 	return stu;
 	}
 } 

```



2. BeanUtils和PropertyUtils进行复制

```java
Student stu1 = new Student();  
stu1.setNumber(12345);  
Student stu2 = new Student(); 
BeanUtils.copyProperties(stu2,stu1);
```





### java web模块



### 设计模式



#### 代理模式

**定义：**

给目标对象提供一个代理对象，并由代理对象控制对模板对象的引用。

在代理模式中，是需要代理对象和模板对象实现同一个接口，如果是不同接口，那就是适配器模式了。

![img](https://upload-images.jianshu.io/upload_images/944365-3e4cc2f9c34a64cc.png?imageMogr2/auto-orient/strip|imageView2/2)

**为什么要使用代理：**

在不改变目前对象方法的情况下对方法进行增强。





#### 单例模式

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



#### 模板模式

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



## 框架



### Spring

#### IOC



#### AOP

实现原理：

利用动态代理进行面向切面编程。具体实现如下

1. 当Spring



#### 事务

**四大特性: ACID**



**原子性（Atomicity）**：事务作为一个整体被执行，包含在其中的对数据库的操作要么全部被执行，要么都不执行。

**一致性（Consistency）**：事务应确保数据库的状态从一个一致状态转变为另一个一致状态。*一致状态*的含义是数据库中的数据应满足完整性约束。

**隔离性（Isolation）**：多个事务并发执行时，一个事务的执行不应影响其他事务的执行。

**持久性（Durability）**：已被提交的事务对数据库的修改应该永久保存在数据库中。



分布式事务：todo

https://blog.csdn.net/mine_song/article/details/64118963

2PC 两段提交协议

https://blog.csdn.net/leeue/article/details/103458450

**事务并发的问题**

  		1、 **脏读(Drity Read)**：事务A修改了一个数据，但未提交，事务B读到了事务A**未提交**的更新结果，如果事务A提交失败，事务B读到的就是脏数据。

　　　**Read Committed**可以解决脏读问题，但仍存在以下两种问题。

​         2、**不可重复读(Non-repeatable read)** : 在同一个事务中，对于同一份数据读取到的结果不一致。比如，事务B在事务A提交前读到的结果，和提交后读到的结果可能不同。不可重复读出现的原因就是**事务并发修改记录**，要避免这种情况，最简单的方法就是对要修改的记录加锁，这导致锁竞争加剧，影响性能。（另一种方法是通过MVCC可以在无锁的情况下，避免不可重复读。待了解。。）

　　　**Repeated Read**可以解决不可重复读问题和脏读问题，但仍无法解决下面的问题。

​         3、**幻读(Phantom Read)**  : 在同一个事务中，同一个查询多次返回的结果不一致。事务A新增了一条记录，事务B在事务A提交前后各执行了一次查询操作，发现后一次比前一次多了一条记录。幻读仅指由于并发事务增加记录导致的问题，这个不能像不可重复读通过记录加锁解决，因为对于新增的记录根本无法加锁。需要将事务串行化，才能避免幻读。



**事务的隔离级别**从低到高有：
**Read Uncommitted**：最低的隔离级别，什么都不需要做，一个事务可以读到另一个事务未提交的结果。所有的并发事务问题都会发生。
**Read Committed**：只有在事务提交后，其更新结果才会被其他事务看见。可以解决脏读问题。
**Repeated Read**：在一个事务中，对于同一份数据的读取结果总是相同的，无论是否有其他事务对这份数据进行操作，以及这个事务是否提交。可以解决脏读、不可重复读。
 **Serialization**：事务串行化执行，隔离级别最高，牺牲了系统的并发性。可以解决并发事务的所有问题。
     通常，在工程实践中，为了性能的考虑会对隔离性进行折中。



| **传播行为**                  | **意义**                                   |
| ------------------------- | ---------------------------------------- |
| PROPERGATION_MANDATORY    | 表示方法必须运行在一个事务中，如果当前事务不存在，就抛出异常           |
| PROPAGATION_NESTED        | 表示如果当前事务存在，则方法应该运行在一个嵌套事务中。否则，它看起来和         PROPAGATION_REQUIRED看起来没什么俩样 |
| PROPAGATION_NEVER         | 表示方法不能运行在一个事务中，否则抛出异常                    |
| PROPAGATION_NOT_SUPPORTED | 表示方法不能运行在一个事务中，如果当前存在一个事务，则该方法将被挂起       |
| PROPAGATION_REQUIRED      | 表示当前方法必须运行在一个事务中，如果当前存在一个事务，那么该方法运行在这个事务中，否则，将创建一个新的事务 |
| PROPAGATION_REQUIRES_NEW  | 表示当前方法必须运行在自己的事务中，如果当前存在一个事务，那么这个事务将在该方法运行期间被挂起 |
| PROPAGATION_SUPPORTS      | 表示当前方法不需要运行在一个是事务中，但如果有一个事务已经存在，该方法也可以运行在这个事务中 |



**Spring中事务使用**

1. jar包导入：

   如果使用Spring Boot, 当我们使用了spring-boot-starter-jdbc或spring-boot-starter-data-jpa依赖的时候，框  架会自动默认分别注入DataSourceTransactionManager或JpaTransactionManager。所以我们不需要任何额外 配置就可以用@Transactional注解进行事务的使用。

2. 定义事务管理器

   ```java
   @EnableTransactionManagement
   @SpringBootApplication
   public class ProfiledemoApplication {
   
       // 其中 dataSource 框架会自动为我们注入
       @Bean
       public PlatformTransactionManager txManager(DataSource dataSource) {
           return new DataSourceTransactionManager(dataSource);
       }
   
       @Bean
       public Object testBean(PlatformTransactionManager platformTransactionManager) {
           System.out.println(">>>>>>>>>>" + platformTransactionManager.getClass().getName());
           return new Object();
       }
   
       public static void main(String[] args) {
           SpringApplication.run(ProfiledemoApplication.class, args);
       }
   }
   ```


3. 声明目标函数需要被事务管理

   ```java
   @Transactional
   ```

4. 根据隔离级别和传播情况设置其属性

   ```java
   @Transactional(value="txManager1")
   ```

5. 如果出现多个事务管理器，可以用value属性具体指定，也可用annotationDrivenTransactionManager()指定默认管理器

   ```java
   @EnableTransactionManagement // 开启注解事务管理，等同于xml配置文件中的 <tx:annotation-driven />
   @SpringBootApplication
   public class ProfiledemoApplication implements TransactionManagementConfigurer {
   
       @Resource(name="txManager2")
       private PlatformTransactionManager txManager2;
   
       // 创建事务管理器1
       @Bean(name = "txManager1")
       public PlatformTransactionManager txManager(DataSource dataSource) {
           return new DataSourceTransactionManager(dataSource);
       }
   
       // 创建事务管理器2
       @Bean(name = "txManager2")
       public PlatformTransactionManager txManager2(EntityManagerFactory factory) {
           return new JpaTransactionManager(factory);
       }
   
       // 实现接口 TransactionManagementConfigurer 方法，其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
       @Override
       public PlatformTransactionManager annotationDrivenTransactionManager() {
           return txManager2;
       }
   
       public static void main(String[] args) {
           SpringApplication.run(ProfiledemoApplication.class, args);
       }
   
   }
   ```


Spring如何使注解（@PostConstruct/@Predestroy...）运作

@PostConstruct -> init function

@Predestroy. - > destroy function

调用InitDescroyAnnotationBeanPostProcessor方法，拿取lifecycleMetadata，利用反射调用方法

同理，AutiwuredAnnotationBeanPostProcessor方法可以处理@Autowired（自动装配）注解

各个注解都是通过BeanPostProcessor方法进行运作的



bean的赋值方式

1. @Value赋值

   1） 基本数值 @Value（"张三"）

   2） 可以写#{} @Value（"#{20-2}"）

   #）配置文件的值${} 

   先设置配置文件@PropertySource(value = {"classpath:/person.properties"})

   再加载配置文件里的值@Value（"${person.nickName}"）



autowire自动装配：Spring利用依赖注入（DI）完成对ioc容器中哥哥组件的依赖关系赋值

@Autowired：自动注入 

	1. 先按照类型找
	2. 如何存在多个，就按照属性名作为id去找
	3. 使用@Qualifier，指定组件id

#### Spring MVC



#### Spring Boot

**Feature**

- Create stand-alone Spring applications（创建独立的Spring应用）
- Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files)（内嵌Tomcat等容器）
- Provide opinionated 'starter' dependencies to simplify your build configuration（提供起步依赖以简化配置）
- Automatically configure Spring and 3rd party libraries whenever possible（自动配置）
- Provide production-ready features such as metrics, health checks, and externalized configuration（提供指标/运行状况检查等生产工具）
- Absolutely no code generation and no requirement for XML configuration（无需xml配置）

起步依赖：

​	不需要添加一堆库依赖，指定版本号，考虑兼容性等问题。

​	问：如何排除起步依赖中不需要的依赖？

​	答：在<exlusions>里排除传递依赖

​	问：如何指定自己需要的依赖版本？

​	答：在pom.xml文件中直接表达诉求<dependency>...<version>2.4.3</version>...</dependency>，Maven总是会用最近的依赖。



@RestController

@RequestMapping("/stock")

@GetMapping("/deduct/{productId/{stockCount}}")

@PathVariable("productId") Long productId



**自动配置**

如何覆盖自动配置

1. 显式地写一段配置

   1. 示例：

      代码块

   2. 

2. 通过属性文件外置配置





##### 四个核心

1. 自动配置

   如果在classpath里发现H2数据库的库，自动配置嵌入式H2数据库；如果在classpath里发现JdbcTemplate，自动配置JdbcTemplate的Bean。

2. 起步依赖

   spring-boot-starter-web 一个依赖搞定所有依赖。

3. 命令行界面

   Spring Boot CLI

4. Actuator

   窥探应用成熟内部情况：

   - 应用程序上下文配置的Bean
   - 应用程序里线程的当前状态




##### Spring 入门

**安装Spring Boot CLI**

1. 下载zip包

   地址：

   https://repo.spring.io/release/org/springframework/boot/spring-boot-cli/2.0.1.RELEASE/spring-boot-cli-2.0.1.RELEASE-bin.zip?spm=a2c6h.12873639.0.0.77af1862OSB06Z&file=spring-boot-cli-2.0.1.RELEASE-bin.zip

2. 添加环境变量

   注意地址到bin目录

   D:\Spring_Boot_CLI\spring-2.0.1.RELEASE\bin

   ![image-20200708003658059](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20200708003658059.png)

   ​	3. 验证：

```
$ spring --version
Spring CLI v2.0.0.RELEASE
```

![image-20200708003733212](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20200708003733212.png)



**运行Groovy脚本**

1. 创建Groovy脚本

   ```groovy
   @RestController
   class ThisWillActuallyRun {
   
       @RequestMapping("/")
       String home() {
           "Hello World!"
       }
       
   }
   ```

   注：

   1. Groovy是一种基于Java平台的面向对象语言，这段脚本的意义是创建一个简单的Web应用，第一次运行时会自动下载依赖，之后可以测试安装是否成功。测试方法：浏览器中打开[localhost:8080](localhost:8080)，看到以下输出：

   ```
   Hello World!
   ```

   2. 为什么会自动下载依赖：源于Spring Boot的起步依赖和自动配置功能。



2. 运行Groovy脚本

   在命令行中键入：

   ```
   spring run hello.groovy
   ```


![image-20200708010005530](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20200708010005530.png)



**初始化Spring Boot项目**

1. 利用官方的Spring Initializr 的Web界面初始化

https://start.spring.io/



![image-20200708011242718](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20200708011242718.png)



2. 利用CLI命令去创建

```
spring init
```



![image-20200708011750659](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20200708011750659.png)









**多数据源的配置和使用**

基于JdbcTemplate的配置：

创建Spring配置类，定义两个DataSource来读取*application.properties*中的不同配置。

@Bean

@Qualifier(当有多个相同类型的bean时，用**@Qualifier** 注释和 **@Autowired** 注释通过指定哪一个真正的 bean 将会被装配来消除混乱。)

@ConfigurationProperties

```java
@Configuration
public class DataSourceConfig {

    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix="spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

}
```

application.properties配置如下：

```java
spring.datasource.primary.url=jdbc:mysql://localhost:3306/test1
spring.datasource.primary.username=root
spring.datasource.primary.password=root
spring.datasource.primary.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.secondary.url=jdbc:mysql://localhost:3306/test2
spring.datasource.secondary.username=root
spring.datasource.secondary.password=root
spring.datasource.secondary.driver-class-name=com.mysql.jdbc.Driver
```

JDBCTemplate支持：

对JdbcTemplate的支持比较简单，只需要为其注入对应的datasource即可，如下例子，在创建JdbcTemplate的时候分别注入名为`primaryDataSource`和`secondaryDataSource`的数据源来区分不同的JdbcTemplate。

```java
@Bean(name = "primaryJdbcTemplate")
public JdbcTemplate primaryJdbcTemplate(
        @Qualifier("primaryDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
}

@Bean(name = "secondaryJdbcTemplate")
public JdbcTemplate secondaryJdbcTemplate(
        @Qualifier("secondaryDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
}
```

演示：

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ApplicationTests {

	@Autowired
	@Qualifier("primaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate1;

	@Autowired
	@Qualifier("secondaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate2;

	@Before
	public void setUp() {
		jdbcTemplate1.update("DELETE  FROM  USER ");
		jdbcTemplate2.update("DELETE  FROM  USER ");
	}

	@Test
	public void test() throws Exception {

		// 往第一个数据源中插入两条数据
		jdbcTemplate1.update("insert into user(id,name,age) values(?, ?, ?)", 1, "aaa", 20);
		jdbcTemplate1.update("insert into user(id,name,age) values(?, ?, ?)", 2, "bbb", 30);

		// 往第二个数据源中插入一条数据，若插入的是第一个数据源，则会主键冲突报错
		jdbcTemplate2.update("insert into user(id,name,age) values(?, ?, ?)", 1, "aaa", 20);

		// 查一下第一个数据源中是否有两条数据，验证插入是否成功
		Assert.assertEquals("2", jdbcTemplate1.queryForObject("select count(1) from user", String.class));

		// 查一下第一个数据源中是否有两条数据，验证插入是否成功
		Assert.assertEquals("1", jdbcTemplate2.queryForObject("select count(1) from user", String.class));

	}


}
```



基于Spring-data-jpa的配置:





#### Spring Cloud



##### Ribion 负载均衡

负载均衡，英文名称为Load Balance，其含义就是指将负载（工作任务）进行平衡、分摊到多个操作单元上进行运行，例如FTP服务器、Web服务器、企业核心应用服务器和其它主要任务服务器等，从而协同完成工作任务。

负载均衡策略：

RandomRule（随即选择）

rand.nextInt(serverCount)

```
public Service choose(ILOadBalancer lb, Object key){
...

}
```

RoundRobinRule（线性轮询）

调用AtomticInteger nextServerCyclicCounter对象实现

count>10报错



nextServerIndex = incrementAndGetModulo(serverCount)





RetryRule(重试机制)

默认使用RandomRule，choose方法中实现反复尝试，超过阈值返回null



WeightedResponseTimeRule（权重机制）

RoundRobinRule（轮询）的扩展

1. 定时任务

   ServerWeightTimer.schedule

2. 权重计算

   权重值写在arrayList里，通过maintainWeights函数实现

   1. 记录平均响应时间
   2. 根据公式计算权重：weightSoFar + totalResponseTime - 实例平均响应时间

   平均响应时间越短，权重区间越大，越可能被选中



BestAvailableRule（最空闲机制）

继承ClientConfigEnabledRoundRobinRule， 过滤掉故障实力，找出请求数最小的，选出最空闲的实例



PredicatedBasedRule（猜测机制）

predicate逻辑



### Mybaits



### 中间件

高可用/安全性



#### kafka



#### Rabbit MQ



### 数据库



#### 数据库三大范式

第一范式：数据库表中的字段都是单一属性的，不可再分(保持数据的原子性)；

第二范式：第二范式必须符合第一范式，非主属性必须完全依赖于主键。

第三范式：在满足第二范式的基础上，在实体中不存在其他实体中的非主键属性，传递函数依赖于主键属性，确保数据表中的每一列数据都和主键直接



Elaticsearch：倒排索引 

索引，是为了加快信息查找过程，基于目标信息内容预先创建的一种储存结构。

![img](https://upload-images.jianshu.io/upload_images/5998834-d2b21b6221ada6be.png?imageMogr2/auto-orient/strip|imageView2/2)



一般地，当接受到用户查询请求时，进入到倒排索引进行检索时，在返回结果的过程中，主要有以下几个步骤：

**Step1**：在分词系统对用户请求等原始Query进行分析，产生对应的terms；

**Step2**：terms在倒排索引中的词项列表中查找对应的terms的结果列表；

**Step3**：对结果列表数据进行微运算，如：计算文档静态分，文档相关性等；

**Step4**：基于上述运算得分对文档进行综合排序，最后返回结果给用户。



**DDL（Data Definition Language）**



```sql
CREATE user
CREATE database 
CREATE table <tablename>(
<column1><DataType1>,
...
)
```



```sql
CREATE TABLE Employees ( Id INT, Name VARCHAR(50), Phone BIGINT, IsContractor BIT );
```



```SQL
ALTER TABLE <TableName> MODIFY <ColumnName> <DataType>
```



常见的sql题目：

用sql语句查询出各班级最高分的学生信息：

示例表：

create table it_student(
 id int primary key auto_increment,  -- 主键id
 name varchar(20),  -- 姓名
 gender enum('male','female'),  -- 性别
 class_id tinyint unsigned,  -- 班级号
 age int unsigned,  -- 年龄
 home varchar(40),  -- 家乡
 score tinyint unsigned  -- 成绩
 );

SQL：

```mysql
select name,home,score from(select * from it_student order by score desc) as s group by class_id
```





#### MySQL

特点：插件式储存引擎，根据不同场景使用不同的引擎。引擎是基于表的，而不是数据库。

Innodb引擎

MySQL 5.1后的默认引擎，MVCC（Mutiversion Concurency Control）实现了完全的事务（ACID），行级锁保证了高并发；聚簇索引（表的储存都是按主键的顺序进行存放）提高对主键查询的效率；自适应哈希索引加速读操作；利用缓冲区加速是插入操作。

事务ACID

Atomicity 原子性： 不可分割，要么全部成功，要么全部失败。

Consistency 一致性：没有部分成功部分失败的情况。

Isolation 隔离性：事务提交以前对其他事务不可见。具体分四个等级：

- read uncommited 未提交读，事务未提交的修改对其他事务可见，所以会出现脏读。
- read committed 提交读，事务提交之前的修改对其他事务不可见，但两次相同的查询可能会出现不同的结果，即会出现幻读。
- repeatable read 可重复读，同一事务相同查询保证能取得相同结果，利用MVCC实现。
- Serializable 串行化，每一行都加锁，保证没有并发来解决一致性问题。

Durability 持久性：修改永久保留，崩溃也不会丢失。

MVCC：多版本并发控制。利用创建时间和删除时间来控制不同事务的并发问题。 每一次CRUD都是一次事务，每个事务生成一个自增ID，通过修改创建时间和删除时间并和事务ID对比来进行控制。MVCC默认隔离级别是repeatable read（即可重复读，可以避免幻读），具体实现：

INSERT： 事务ID-> 创建时间

DELETE：事务ID -> 删除时间

UPDATE：插入新记录 事务ID->创建时间；原来记录 事务ID->删除时间

SELECT：创建时间< 当前事务ID 且 （当前事务ID < 删除时间  或 删除时间为空）



自动提交模式，每个查询都被当作一个事务执行提交操作，可以设置ATUOCOMMIT变量来启动或禁用此模式。在执行ALTER TABLE， LOCK TABLE等语句时也会强制执行COMMIT提交当前的活动事务。

两阶段锁定协议，在事务执行过程中随时锁定，但只有在COMMIT或ROLLABCK的时候才会统一释放锁。



1. 线程

   1. master thread
   2. IO thread
   3. page cleaner thread
   4. purge thread

2. 内存

   1. 缓冲池：由于CPU速度与磁盘速度之间的鸿沟，用缓冲池提高性能。读取的页放入缓冲池，下次读取直接读缓冲池数据；修改时先修改缓冲池数据，再异步刷新到磁盘中，刷新由Checkpoint机制控制。缓冲池存的数据类型有：索引页、数据页、undo页、插入缓存、自适应哈希索引、锁信息、数据字典等。缓冲区的管理通过LRU(Latest Recent Used)算法管理，使用最频繁的页再最前端，使用最少的再最尾端，新页无法存时首先释放尾端页。新页首先放在midpoint位置（37%）而不是最前端，保证热点数据没有频繁变化且非热点数据不会混入。

      Checkpoint：分为Fuzzy Checkpoint部分刷新（刷新一部分脏页而不是所有）、Sharp Checkpoint全部刷新（数据库关闭时将所有脏页刷新回磁盘）。由page clearner thread维护。







MyISAM引擎 

MySql5.1以前版本的默认引擎，不支持事务，表锁。优势：支持全文索引、压缩、空间函数。读多写少的业务，不介意崩溃恢复问题。

archive引擎

只支持INSERT和SEELCT操作，支持行级锁以及有专门的缓冲区，插入时会进行压缩，索引磁盘I/O更少，能实现高并发的插入。适合做日志。

memory引擎

cu你在内存中，不需要磁盘I/O，速度很快。是表级锁，并发写性能低，不支持BLOB或TEXT类型（长字段），长度固定。重启数据会丢失。适合做映射表（比如邮编和洲名映射表）或中间表。

csv引擎

将csv文件作为MySQL的表进行处理，不支持索引。可以作为数据交换的机制，

NDB引擎  MySQL Cluster

集群储存引擎，分布式、sharing-nothing、容灾、高可用。



![image-20220213182637512](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20220213182637512.png)



#### 性能剖析 Profiling

每秒查询次数（即吞吐量）、CPU利用率（只是表象，升级新的引擎导致CPU利用率上升是好事、没有有效利用索引导致CPU利用率上升是坏事）、可扩展性，最终还是看即响应时间。

响应时间可分为执行时间和等待时间。

-执行时间：测量子任务花费时间、降低频率、提升效率、优化SQL

-等待时间：争用磁盘或CPU资源导致阻塞

调优对象：

1. 值得优化的查询：对于总响应时间比重很小的查询不值得优化
2. 异常情况：某些执行虽然次数很少，但每次执行都很慢



剖析单挑查询

```sql
show profiles;
show profile for query 1;
```

分析索引

```sql
explain select * from actor;
-- 查询优化信息
explain extended select * from actor;
show warnings
-- 查询分区
explain partitions select * from actor;
```

分析实时状态、全局状态

```sql
show status;
show global status;
```

分析线程正常状态

```sql
show proccesslist;
```

查看索引使用情况

```sql
show tables from information_schema like '_STATISTICS';
```



数据类型优化

1. 更小更好
2. 简单数据类型，如用date，time，datetime储存日期和时间，用整型储存IP地址，而不是用字符型（因为字符型的比较和排序规则比整形复杂）
3. 尽量避免NULL。NULL值使得索引、索引统计和值比较更复杂，每个null的列被索引时，都需要一个额外的字节储存。



扩展：

datetime与timestmp的区别

timestmp比datetime储存空间更小，且会根据失去变化有自动更新的能力，但timestamp预先的时间范围小一些。

| 类型        | 占据字节 | 表示形式                |
| :-------- | :--- | :------------------ |
| datetime  | 8 字节 | yyyy-mm-dd hh:mm:ss |
| timestamp | 4 字节 | yyyy-mm-dd hh:mm:ss |

如果在时间上要超过Linux时间的，或者服务器时区不一样的就建议选择 datetime。

如果是想要使用自动插入时间或者自动更新时间功能的，可以使用timestamp。

如果只是想表示年、日期、时间的还可以使用 year、 date、 time，它们分别占据 1、3、3 字节，而datetime就是它们的集合。



varchar与char的区别

vharchar是可变长字符串，需要使用1或2格额外字节记录字符串的长度（len<=255字节用1字节表示，否则用2字节）

char为定长字符串，适合储存很短的字符串或所有制都接近同一长度。比如MD5密码值（定长），或者status中的Y/N值，用char（1）表示，不容易产生碎片，存储也更有效率。

ENUM，把枚举列可以把一些不重复的字符串储存到一个预定义的集合中，然后使用“数字-字符串”映射关系查找。但这样的问题是排序会按照内部储存的证是而不是定义的字符串进行排序的，所以最好在定义时就按照字母排序。

```mysql
-- 开始定义的时候就排好序
CREATE TABLE enum_test(e ENUM('apple','dog','fish') not null);
INSERT INTO enum_test(e) values(('fish'),('dog'),('apple'));
-- 也可以在查询的时候自定义排序列
SELECT e FROM enum_test ORDER BY(e,'apple','dog','fish');
```



标识符选择

整数类型通常是最好的选择，因为他们很快且可以使用AUTO_INCREMENT。

字符串类型，很消耗空间，MD5或UUID产生的字符串，值任意分布，使得磁盘随机访问，数据集一样“热”，insert和select都会变慢。





#### 索引

索引储存在引擎层而不是服务器层。所以没有统一的索引标准；不同引擎的索引工作方式也不尽相同。NDB用的是B-Tree。InnoDB用的是B+Tree。

扩展：

B-Tree与B+Tree的区别

B+Tree的每个叶子节点都包含了下一个叶子节点的指针，方便叶子节点范围遍历。





#### sql优化

创建索引

```sql
-- 1.添加PRIMARY KEY（主键索引）  
ALTER TABLE `table_name` ADD PRIMARY KEY (`column`) ;
 -- 2.添加UNIQUE(唯一索引)  
ALTER TABLE `table_name` ADD UNIQUE (`column`); 
-- 3.添加INDEX(普通索引)  
ALTER TABLE `table_name` ADD INDEX index_name (`column`); 
-- 4.添加FULLTEXT(全文索引)  
ALTER TABLE `table_name` ADD FULLTEXT (`column`); 
-- 5.添加联合索引  
ALTER TABLE `table_name` ADD INDEX index_name (`column1`, `column2`, `column3`);

```



索引的优势是：

加快查询速度（包括关联查询）

加快排序速度（ORDER BY）

加快分组速度（GROUP BY）

索引的劣势：

对索引列的增删改需要额外维护索引，也就是说**索引能提高查询速度，但往往会降低增删改的速度**

日常开发通常是建联合索引，而联合索引需要考虑索引失效问题

太多的索引会增加查询优化器的选择时间



分页

```mysql
# 反例（耗时129.570s）
select * from task_result LIMIT 20000000, 10;

# 正例（耗时5.114s）
SELECT a.* FROM task_result a, (select id from task_result LIMIT 20000000, 10) b where a.id = b.id;

# 说明
task_result表为生产环境的一个表，总数据量为3400万，id为主键，偏移量达到2000万
```



尽量避免使用select *，返回无用的字段会降低查询效率。如下：

SELECT * FROM t 

优化方式：使用具体的字段代替*，只返回使用到的字段。



#### Mysql

Mysql 创建索引：

create index idx_text_c1234 on test(c1,c2,c3,c4);

show index from test;

![img](https://images2018.cnblogs.com/blog/706569/201806/706569-20180625114139843-688761483.png)



InnoDB

InnoDB是默认的表存储引擎。其特点是行锁设计、支持MVCC、支持外键、提供一致性非锁定读、同时被设计用来最有效的利用以及使用内存和CPU。

InnoDB默认的隔离级别是RR（repeated read）





在B+Tree中所有数据记录节点都是按照键值大小顺序存放在同一层的叶子节点上，而非叶子节点上只存储key值信息，这样可以大大加大每个节点存储的key值数量，降低B+Tree的高度；

B+Tree在B-Tree的基础上有两点变化：

（1）数据是存在叶子节点中的

（2）数据节点之间是有指针指向的

由于B+Tree的非叶子节点只存储键值信息，假设每个磁盘块能存储4个键值及指针信息，则变成B+Tree后其结构如下图所示：

![img](https://img-blog.csdn.net/20180712091941460?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3OTYyNjAw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

Mybits:

缓存

一级缓存，默认开启，作用在SqlSession上，有任何更新操作都会清空一级缓存

二级缓存：

1. 需要配置

   ```mysql
   <cache eviction="LRU" readOnly = "true" size = "1024"><cache/>
   ```

2. 查询数据在SqlSessionFactory中，用Map实现

3. 默认先用一级缓存，一级缓存关闭或不在再查询二级缓存，也可以用第三方工具（EhCahe或Redis）



分页

逻辑分页：先拿取所有记录，再从内存中取出一页的量

物理分页：用pageHelper实现



#### Redis

### Redis

难题：一瞬间有成千上万的请求，需要再极短时间完成上万次读写操作

解决：引入NoSQL技术，基于**内存的数据库**，并提供持久化功能（内存读写比硬盘读写快，所以能够解决快读读写的问题）

代表：Redis和MongoDB是当前使用最广泛的NoSQL，支持每秒十几万次的读写操作，还支持集群、分布式、主从同步等配置，可以无限扩展，还支持一定的事务能力，保证数据安全性和一致性。、

应用：

- 储存**缓存**用的数据。
- 需要告诉读写场合使用它来**快速读写**。



#### 缓存

背景：日常读、写比例大概是1:9 到3:7。而使用SQL 语句去数据库读写操作较慢，所以希望服务端直接读取内存中的数据。为避免内存开销过大，Redis通常只会存储一些常用数据。

- 读的次数很高
- 写的次数很低
- 数据不大



高速读写场合

流程如下：

![image-20200712024443259](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20200712024443259.png)



#### Redis安装

1. 下载并解压

https://github.com/ServiceStack/redis-windows/tree/master/downloads



2. 创建start.cmd

   ```
   redis-server redis.windows.conf
   ```


3. 双击cmd启动

![preview](https://pic2.zhimg.com/v2-d138e37467f3d739454c40b5b62ed8f5_r.jpg)



（① Redis 当前的版本为 3.0.503；**② Redis 运行在 6379 端口；**③ Redis 进程的 PID 为 14748；④ 64 位）



#### 在JAVA中使用Redis

1. 添加依赖

   Maven： Jedis

   https://mvnrepository.com/

2. 使用连接池

   ```
   JedisPoolConfig poolConfig = new JedisPoolConfig();
   // 最大空闲数
   poolConfig.setMaxIdle(50);
   // 最大连接数
   poolConfig.setMaxTotal(100);
   // 最大等待毫秒数
   poolConfig.setMaxWaitMillis(20000);
   // 使用配置创建连接池
   JedisPool pool = new JedisPool(poolConfig, "localhost");
   // 从连接池中获取单个连接
   Jedis jedis = pool.getResource();
   // 如果需要密码
   //jedis.auth("password");
   ```

   支持的数据类型：

   string/hash/list/set/zset/hyperloglog



3. 在Spring中使用Redis

   引入spring-data-redis包

   https://mvnrepository.com/artifact/org.springframework.data/spring-data-redis

   1. 配置JedisPoolConfig对象

      ```xml
      <bean id="poolConfig" class="redis.clients.jedis.JedisPoolConfig">
          <!--最大空闲数-->
          <property name="maxIdle" value="50"/>
          <!--最大连接数-->
          <property name="maxTotal" value="100"/>
          <!--最大等待时间-->
          <property name="maxWaitMillis" value="20000"/>
      </bean>
      ```

   2. 配置工厂模型

      ```xml
      <bean id="connectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
          <!--Redis服务地址-->
          <property name="hostName" value="localhost"/>
          <!--端口号-->
          <property name="port" value="6379"/>
          <!--如果有密码则需要配置密码-->
          <!--<property name="password" value="password"/>-->
          <!--连接池配置-->
          <property name="poolConfig" ref="poolConfig"/>
      </bean>
      ```

   3. 配置RedosTemplate

      ```xml
      <bean id="redisTemplate"
            class="org.springframework.data.redis.core.RedisTemplate"
            p:connection-factory-ref="connectionFactory"/>
      ```

   4. 测试

      POJO类：

      ```java
      /**
       * @author: @我没有三颗心脏
       * @create: 2018-05-30-下午 22:31
       */
      public class Student implements Serializable{
      
      	private String name;
      	private int age;
      
      	/**
      	 * 给该类一个服务类用于测试
      	 */
      	public void service() {
      		System.out.println("学生名字为：" + name);
      		System.out.println("学生年龄为：" + age);
      	}
      
      	public String getName() {
      		return name;
      	}
      
      	public void setName(String name) {
      		this.name = name;
      	}
      
      	public int getAge() {
      		return age;
      	}
      
      	public void setAge(int age) {
      		this.age = age;
      	}
      }
      
      ```

      测试类

      ```java
      @Test
      public void test() {
      	ApplicationContext context =
      			new ClassPathXmlApplicationContext("applicationContext.xml");
      	RedisTemplate redisTemplate = context.getBean(RedisTemplate.class);
      	Student student = new Student();
      	student.setName("我没有三颗心脏");
      	student.setAge(21);
      	redisTemplate.opsForValue().set("student_1", student);
      	Student student1 = (Student) redisTemplate.opsForValue().get("student_1");
      	student1.service();
      }
      
      ```

      运行结果

      ![preview](https://pic1.zhimg.com/v2-3d3c0c4272b2bce302371c768b841ff8_r.jpg)



3. 在SpringBoot中使用Redis

   1. 添加依赖

      ```xml
      <!-- Radis -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-data-redis</artifactId>
      </dependency>

      ```

   2. 添加配置文件（.properties/.yml）

      ```properties
      # REDIS (RedisProperties)
      # Redis数据库索引（默认为0）
      spring.redis.database=0
      # Redis服务器地址
      spring.redis.host=localhost
      # Redis服务器连接端口
      spring.redis.port=6379
      # Redis服务器连接密码（默认为空）
      spring.redis.password=
      # 连接池最大连接数（使用负值表示没有限制）
      spring.redis.pool.max-active=8
      # 连接池最大阻塞等待时间（使用负值表示没有限制）
      spring.redis.pool.max-wait=-1
      # 连接池中的最大空闲连接
      spring.redis.pool.max-idle=8
      # 连接池中的最小空闲连接
      spring.redis.pool.min-idle=0
      # 连接超时时间（毫秒）
      spring.redis.timeout=0
      ```

   3. 测试

      ```java
      @RunWith(SpringJUnit4ClassRunner.class)
      @SpringBootTest()
      public class ApplicationTests {

      	@Autowired
      	private StringRedisTemplate stringRedisTemplate;

      	@Test
      	public void test() throws Exception {

      		// 保存字符串
      		stringRedisTemplate.opsForValue().set("aaa", "111");
      		Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

      	}
      }
      ```

   4. 存储对象

      POJO类实现Serialzable接口

      ```java
      @RunWith(SpringJUnit4ClassRunner.class)
      @SpringBootTest()
      public class ApplicationTests {
      
      	@Autowired
      	private RedisTemplate redisTemplate;
      
      	@Test
      	public void test() throws Exception {
      
      		User user = new User();
      		user.setName("我没有三颗心脏");
      		user.setAge(21);
      
      		redisTemplate.opsForValue().set("user_1", user);
      		User user1 = (User) redisTemplate.opsForValue().get("user_1");
      
      		System.out.println(user1.getName());
      	}
      }
      ```

      结果：

      ![img](https://pic3.zhimg.com/80/v2-6550d649a402a673033459d3c882cb6a_720w.jpg)





#### 操作集合

1. List

   ```java
   // list数据类型适合于消息队列的场景:比如12306并发量太高，而同一时间段内只能处理指定数量的数据！必须满足先进先出的原则，其余数据处于等待
   @Test
   public void listPushResitTest() {
   	// rightPush依次由右边添加
   	stringRedisTemplate.opsForList().rightPush("myList", "1");
   	stringRedisTemplate.opsForList().rightPush("myList", "2");
   	stringRedisTemplate.opsForList().rightPush("myList", "A");
   	stringRedisTemplate.opsForList().rightPush("myList", "B");
   	// leftPush依次由左边添加
   	stringRedisTemplate.opsForList().leftPush("myList", "0");
   }

   @Test
   public void listGetListResitTest() {
   	// 查询类别所有元素
   	List<String> listAll = stringRedisTemplate.opsForList().range("myList", 0, -1);
   	logger.info("list all {}", listAll);
   	// 查询前3个元素
   	List<String> list = stringRedisTemplate.opsForList().range("myList", 0, 3);
   	logger.info("list limit {}", list);
   }

   @Test
   public void listRemoveOneResitTest() {
   	// 删除先进入的B元素
   	stringRedisTemplate.opsForList().remove("myList", 1, "B");
   }

   @Test
   public void listRemoveAllResitTest() {
   	// 删除所有A元素
   	stringRedisTemplate.opsForList().remove("myList", 0, "A");
   }
   ```

2. Hash

   ```java
   @Test
   public void hashPutResitTest() {
   	// map的key值相同，后添加的覆盖原有的
   	stringRedisTemplate.opsForHash().put("banks:12600000", "a", "b");
   }
   
   @Test
   public void hashGetEntiresResitTest() {
   	// 获取map对象
   	Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("banks:12600000");
   	logger.info("objects:{}", map);
   }
   
   @Test
   public void hashGeDeleteResitTest() {
   	// 根据map的key删除这个元素
   	stringRedisTemplate.opsForHash().delete("banks:12600000", "c");
   }
   
   @Test
   public void hashGetKeysResitTest() {
   	// 获得map的key集合
   	Set<Object> objects = stringRedisTemplate.opsForHash().keys("banks:12600000");
   	logger.info("objects:{}", objects);
   }
   
   @Test
   public void hashGetValueListResitTest() {
   	// 获得map的value列表
   	List<Object> objects = stringRedisTemplate.opsForHash().values("banks:12600000");
   	logger.info("objects:{}", objects);
   }
   
   @Test
   public void hashSize() { // 获取map对象大小
   	long size = stringRedisTemplate.opsForHash().size("banks:12600000");
   	logger.info("size:{}", size);
   }
   ```





## JVM

## JVM结构

https://www.jianshu.com/p/904b15a8281f



## JVM三大核心

类加载器、执行引擎、运行时数据区



### 类加载器

解析  .class文件 转为虚拟机可以识别的二进制机器码。虚拟机将描述类的数据从Class文件加载到内存，并对数据进行校验、准备、解析和初始化，最终就会形成可以被虚拟机使用的Java类型，这就是一个虚拟机的类加载机制。Java中的类是动态加载的，只有在运行期间使用到该类的时候，才会将该类加载到内存中，Java依赖于运行期动态加载和动态链接来实现类的动态使用。



#### 类加载器分类

1. 启动类加载器(Bootstrap ClassLoader)：  主要负责加载lib目录中的，或是-Xbootclasspath参数指定的路径中的，并且可以被虚拟机识别(仅仅按照文件名识别的)的类库到虚拟机内存中。它加载的是：System.getProperty("sun.boot.class.path")所指定的路径或jar。

2. 扩展类类加载器(Extension ClassLoader)：主要负责加载libext目录中的，或者被java.ext.dirs系统变量所指定的路径中的所有类库。它加载的是：

   System.getProperty("java.ext.dirs")所指定的路径或jar。

3. 应用程序类加载器(Application ClassLoader)：也叫系统类加载器，主要负责加载ClassPath路径上的类库，如果应用程序没有自定义自己类加载器，则这个就是默认的类加载器。它加载的是：System.getProperty("java.class.path")所指定的路径或jar。

4. 用户自定义加载器（CustomClassLoader）：由Java实现。我们可以自定义类加载器，并可以加载指定路径下的class文件。



#### JVM类加载方式

类加载有三种方式：

- 1、命令行启动应用时候由JVM初始化加载
- 2、通过Class.forName()方法动态加载
- 3、通过ClassLoader.loadClass()方法动态加载

**Class.forName()和ClassLoader.loadClass()区别**

- `Class.forName()`：将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块；
- `ClassLoader.loadClass()`：只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance才会去执行static块。
- `Class.forName(name,initialize,loader)`带参函数也可控制是否加载static块。并且只有调用了newInstance()方法采用调用构造函数，创建类的对象 



#### 类的生命周期

装载 -> 连接（验证+准备+解析）-> 初始化

java虚拟机通过装载、连接、初始化一个java类型，使该类型可以被正在运行的java程序所使用。

装载：把二进制形式的java类型读到jvm中。

连接：把已经读入虚拟机的二进制形式的类型数据合并到虚拟机的运行时状态中。

​	验证：保证java类型格式正确。

​	准备：负责分配内存。

​	解析：负责把常量池中的符号引用装换为直接引用。（此步骤可以在初始化后进行）

初始化：给类变量赋以适当的值。

**什么时候初始化：**

类或接口首次**主动**使用时初始化。具体来说有六种：

1. 创建某个类的新实例。
2. 调用某个类的静态方法。
3. 使用某个类的静态字段，或者对该字段赋值时。final修饰的静态字段除外，它被初始化为一个编译时的常量表达式。
4. 调用反射方法时，如java.lang.reflect包中的类方法。
5. 初始化子类时（某个类初始化，要求它的超类也初始化）。
6. 启动类，即main（）方法的类。

被动使用不需要初始化，比如某个类实现了某个接口，此接口就不一定要初始化，只有在用到此接口所声明的非常量字段时才会初始化此接口，且一个接口的初始化，并不要求其祖先接口也要初始化。



#### 双亲委派模式

双亲委派机制是当类加载器需要加载某一个.class字节码文件时，则首先会把这个任务委托给他的上级类加载器，递归这个操作，如果上级没有加载该.class文件，自己才会去加载这个.class。

双亲委托模型的重要用途是为了解决类载入过程中的**安全性问题**。

- 假设有一个开发者自己编写了一个名为`java.lang.Object`的类，想借此欺骗JVM。现在他要使用自定义`ClassLoader`来加载自己编写的`java.lang.Object`类。
- 然而幸运的是，双亲委托模型不会让他成功。因为JVM会优先在`Bootstrap ClassLoader`的路径下找到`java.lang.Object`类，并载入它



**如何打破双亲委派模式**：

1. 自定义Classloader并重写loadclass方法或者findClass方法。

   1. loadClass

      ```java
      ClassLoader myloader = new ClassLoader() {
          @Override
      public Class<?> loadClass(String name) throws ClassNotFoundException {
              try {
                  // 这个getClassInputStream根据情况实现
                  InputStream is = getClassInputStream(name);
                  if (is == null) {
                      return super.loadClass(name);
                  }
                  byte[] bt = new byte[is.available()];
                  is.read(bt);
                  return defineClass(name, bt, 0, bt.length);
              } catch (IOException e) {
                  throw new ClassNotFoundException("Class " + name + " not found.");
              }
          }
      }
      ```

      可能的具体实现：

      ```java
          String filename = name.replace('.', '/')+".class";
          InputStream is = getClass().getResourceAsStream(filename);
      
      ```

      2. findClass

         ```java
         protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                 // First, check if the class has already been loaded
                 Class c = findLoadedClass(name);
                 if (c == null) {
                     try {
                         if (parent != null) {
                             c = parent.loadClass(name, false);
                         } else {
                             c = findBootstrapClassOrNull(name);
                         }
                     } catch (ClassNotFoundException e) {
                         // ClassNotFoundException thrown if class not found
                         // from the non-null parent class loader
                     }
                     if (c == null) {
                         // If still not found, then invoke findClass in order 
                         // to find the class.
                         c = findClass(name);
                     }
                 }
                 if (resolve) { 
                     resolveClass(c);
                 }
                 return c;
             }
         ```


2. 用SPI（Service Provide Interface）：当服务的提供者，提供了服务接口的一种实现之后，在jar包的META-INF/services/目录里同时创建一个以服务接口命名的文件。该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，就能通过该jar包META-INF/services/里的配置文件找到具体的实现类名，并装载实例化，完成模块的注入。  基于这样一个约定就能很好的找到服务接口的实现类，而不需要再代码里制定。jdk提供服务实现查找的一个工具类：java.util.ServiceLoader

   具体实现：

   ```java
   package my.xyz.spi;
   import java.util.List;
   public interface Search {
     public List serch(String keyword);
   }
   ```

    A公司采用文件系统搜索的方式实现了 Search接口，B公司采用了数据库系统的方式实现了Search接口
         A公司实现的类  com.A.spi.impl.FileSearch 
         B公司实现的类  com.B.spi.impl.DatabaseSearch 
    那么A公司发布 实现jar包时，则要在jar包中META-INF/services/my.xyz.spi.Search文件中写下如下内容
    com.A.spi.impl.FileSearch
    那么B公司发布 实现jar包时，则要在jar包中META-INF/services/my.xyz.spi.Search文件中写下如下内容
    com.B.spi.impl.DatabaseSearch

   ```java
   package com.xyz.factory;  
   import java.util.Iterator;  
   import java.util.ServiceLoader;  
   import my.xyz.spi.Search;  
   public class SearchFactory {  
       private SearchFactory() {  
       }  
       public static Search newSearch() {  
           Search search = null;  
           ServiceLoader<Search> serviceLoader = ServiceLoader.load(Search.class);  
           Iterator<Search> searchs = serviceLoader.iterator();  
           if (searchs.hasNext()) {  
               search = searchs.next();  
           }  
           return search;  
       }  
   }  
   
   package my.xyz.test;
    
   import java.util.Iterator;
   import java.util.ServiceLoader;
   import com.xyz.factory.SearchFactory;
   import my.xyz.spi.Search;
    
   public class SearchTest {
   　　public static void main(String[] args) {
   　　　　Search search = SearchFactory.newSearch();
   　　　　search.serch("java spi test");
   　　}
   }
   ```



#### 类实例化顺序

先执行父类与子类的静态变量和静态代码块，再执行父类非静态代码块和构造方法，最后是子类的非静态代码块和构造方法，具体如下：

(**先静后非，先父后子**)

1． 父类静态成员和静态初始化块 ，按在代码中出现的顺序依次执行

2． 子类静态成员和静态初始化块 ，按在代码中出现的顺序依次执行

3． 父类实例成员和实例初始化块 ，按在代码中出现的顺序依次执行

4． 父类构造方法

5． 子类实例成员和实例初始化块 ，按在代码中出现的顺序依次执行

6． 子类构造方法

```java
class Dervied extends Base {


    private String name = "Java3y";

    public Dervied() {
        tellName();
        printName();
    }

    public void tellName() {
        System.out.println("Dervied tell name: " + name);
    }

    public void printName() {
        System.out.println("Dervied print name: " + name);
    }

    public static void main(String[] args) {

        new Dervied();
    }
}

class Base {

    private String name = "公众号";

    public Base() {
        tellName();
        printName();
    }

    public void tellName() {
        System.out.println("Base tell name: " + name);
    }

    public void printName() {
        System.out.println("Base print name: " + name);
    }
}
```

输出数据：

```java
Dervied tell name: null
Dervied print name: null
Dervied tell name: Java3y
Dervied print name: Java3y
```





### 执行引擎

解析 字节码文件 使用执行引擎 驱动去加载机器码



### 内存模型Java Memory Model（JMM）todo

https://zhuanlan.zhihu.com/p/51613784



### 内存空间



内存溢出 out of memory，是指程序在申请内存时，没有足够的内存空间供其使用，出现out of memory；比如申请了一个integer,但给它存了long才能存下的数，那就是内存溢出。

内存泄露 memory leak，是指程序在申请内存后，无法释放已申请的内存空间，一次内存泄露危害可以忽略，但内存泄露堆积后果很严重，无论多少内存,迟早会被占光。

在这个例子中，我们循环申请对象o，并将o放入容器中，虽然我们释放了o，但是由于容器还引用这这个对象，所以GC仍然是不会回收的。我们需要通过释放容器才能被GC回收。

```java
Vector v=new Vector(10);
for (int i=1;i<100; i++)
{
    Object o=new Object();
    v.add(o);
    o=null; 
}
```



**内存泄漏**是堆中的存在无用但可达的对象，GC无法回收。
**内存溢出**是空间不足的溢出，主要分为PermGen space不足、堆不足、栈不足。



#### GC垃圾回收机制

名思义就是释放垃圾占用的空间，防止内存泄露。有效的使用可以使用的内存，对内存堆中已经死亡的或者长时间没有使用的对象进行清除和回收。

##### 如何判断对象是否是垃圾

1. 引用计数法：给每个对象添加一个计数器，当有地方引用该对象时计数器加1，当引用失效时计数器减1。

   无法解决循环引用的问题：如下，a与b永远无法被回收

```java
ReferenceCountingGC a = new ReferenceCountingGC("objA");
ReferenceCountingGC b = new ReferenceCountingGC("objB");

a.instance = b;
b.instance = a;

a = null;
b = null;
```



2. 可达性分析算法

   从GC_ROOT的对象作为搜索起始点，通过引用向下搜索，在这条路径上的对象为存活对象，否则为可回收对象。

   可作为`GC ROOT`的对象：

   1. 虚拟机栈中引用的对象
   2. 方法区中类静态属性引用的对象
   3. 方法区中常量引用的对象
   4. 本地方法栈中`JNI`引用的对象。



```java
public AClass{
 
  // something 为方法区中静态属性引用的对象
  public static Something;
  // Apple 方法区中常量引用的对象
  public static final Apple;
   ''''''
}
```



```java
public Aclass{

	public void doSomething(Object A)
	{
    	ObjectB b = new ObjectB;
	}
}
// 某个线程执行该方法时，参数A可以作为root;
// 局部变量b,也可以作为参数。
```



##### 垃圾回收算法



todo

CMS G1 ZGC

https://www.jianshu.com/p/2957b001645d

卡表

https://www.cnblogs.com/hongdada/p/12016020.html



1. 标记-清除算法

![img](https://upload-images.jianshu.io/upload_images/2269232-5b023b00f7bf8f1b.jpg?imageMogr2/auto-orient/strip|imageView2/2)



把内存区域中的这些对象进行标记，哪些属于可回收标记出来，然后把这些垃圾拎出来清理掉。就像上图一样，清理掉的垃圾就变成未使用的内存区域，等待被再次使用。但它存在一个很大的问题，那就是**内存碎片**。

2. 复制算法

   ![img](https://upload-images.jianshu.io/upload_images/2269232-46c30f2ffb8c18af.jpg?imageMogr2/auto-orient/strip|imageView2/2)

   是在标记清除算法基础上演化而来，解决标记清除算法的内存碎片问题。它将可用内存按容量划分为大小相等的两块，**每次只使用其中的一块**。当这一块的内存用完了，就将还存活着的对象复制到另外一块上面，然后再把已使用过的内存空间一次清理掉。保证了内存的连续可用，内存分配时也就不用考虑内存碎片等复杂情况。复制算法暴露了另一个问题，例如硬盘本来有500G，但却只能用200G，代价实在太高。

3. 标记整理算法

![img](https://upload-images.jianshu.io/upload_images/2269232-d7cb73cb0e50c060.jpg?imageMogr2/auto-orient/strip|imageView2/2)



标记-整理算法标记过程仍然与标记-清除算法一样，但后续步骤不是直接对可回收对象进行清理，而是让所有存活的对象都向一端移动，再清理掉端边界以外的内存区域。标记整理算法对内存变动更频繁，需要整理所有存活对象的引用地址，在效率上比复制算法要差很多。一般是把Java堆分为**新生代**和**老年代**，这样就可以根据各个年代的特点采用最适当的收集算法。



4. 分代收集算法

对象的内存分配都是在堆上进行。Java内存分配和回收的机制概括的说，就是：分代分配，分代回收。对象将根据存活的时间被分为：年轻代（Young Generation）、年老代（Old Generation）、永久代（Permanent Generation，也就是方法区）。

分代收集算法分代收集算法严格来说并不是一种思想或理论，而是融合上述3种基础的算法思想，而产生的针对不同情况所采用不同算法的一套组合拳，根据对象存活周期的不同将内存划分为几块。

- 在新生代中，每次垃圾收集时都发现有大批对象死去，只有少量存活，那就选用**复制算法**，只需要付出少量存活对象的复制成本就可以完成收集。
- 在老年代中，因为对象存活率高、没有额外空间对它进行分配担保，就必须使用**标记-清理算法**或者**标记-整理算法**来进行回收。



对象先进入新生代，大对象（很长的字符串以及数组）直接进入老年代。新生代空间不足时进行minor GC，把存活对象放到Survivor区或者老年区（Survivor区不够时），经历16次（`MaxPretenuringThreshold`可以设置）minor GC还存活的对象进入老年代。如果老年代空间不足，则进行major GC。

JVM 象关参数

| 参数                              | 内容                                       |
| ------------------------------- | ---------------------------------------- |
| **-Xms**                        | 初始堆大小。如：-Xms256m                         |
| -Xmx                            | 最大堆大小。如：-Xmx512m                         |
| -Xmn                            | 新生代大小。通常为 Xmx 的 1/3 或 1/4。新生代 = Eden + 2 个 Survivor 空间。实际可用空间为 = Eden + 1 个 Survivor，即 90% |
| -Xss                            | JDK1.5+ 每个线程堆栈大小为 1M，一般来说如果栈不是很深的话， 1M 是绝对够用了的。 |
| -XX:NewRatio                    | 新生代与老年代的比例，如 –XX:NewRatio=2，则新生代占整个堆空间的1/3，老年代占2/3 |
| **-XX:SurvivorRatio**           | 新生代中 Eden(8) 与 Survivor(1+1) 的比值。默认值为 8。即 Eden 占新生代空间的 8/10，另外两个 Survivor 各占 1/10 |
| -XX:PermSize                    | 永久代(方法区)的初始大小                            |
| **-XX:MaxPermSize**             | 永久代(方法区)的最大值                             |
| -XX:+PrintGCDetails             | 打印 GC 信息                                 |
| -XX:+HeapDumpOnOutOfMemoryError | 让虚拟机在发生内存溢出时 Dump 出当前的内存堆转储快照，以便分析用      |



## 如何减少GC出现的次数：

1. 对象不用时显示置null。
2. 少用System.gc()。
3. 尽量少用静态变量。
4. 尽量使用StringBuffer，而不用String累加字符串。
5. 分散对象创建或删除的时间。
6. 少用finalize函数。
7. 如果需要使用经常用到的图片，可以使用软引用类型，它可以尽可能将图片保存在内存中，供程序调用，而不引起OOM。
8. 能用基本类型就不用基本类型封装类。
9. 增大-Xmx的值。



**内存空间又分为五个区域：**

堆、栈、pc计数器、本地方法栈、方法区



#### 堆（Heap）

储存java 实例或对象的地方。这是GC的主要区域。 方法区和堆是被所有java线程共享的

#### 栈（Stack）

每当创建一个线程，JVM就会为它创建一个对应的java栈。栈中包含多个栈帧（stack frame），每运行一个方法，就创建一个栈帧，用于储存局部变量表、操作栈、方法返回值等。每一个方法从调用到执行完成的过程，就对应一个栈帧在java栈中入栈到出栈的过程。java栈是线程（Thread）私有的。

#### 方法区（Method Area）

用于储存结构信息的地方，包括常量池、静态变量、构造函数等。

PS：针对于jdk1.7之后：

- 运行时常量池位于**方法区中** 
- 字符串常量池位于**堆中** 



##### 对象储存问题

intern() 方法：

1. 如果常量池中存在当前字符串，那么直接返回常量池中它的引用。
2. 如果常量池中没有此字符串, 会将此字符串引用保存到常量池中后, 再直接返回该字符串的引用！

JVM 例题解析：

```java
String str1 = "a";
String str2 = "b";
String str3 = "ab";
String str4 = str1 + str2;
String str5 = new String("ab");
 
System.out.println(str5.equals(str3));
System.out.println(str5 == str3);
System.out.println(str5.intern() == str3);
System.out.println(str5.intern() == str4);
```

输出结果是：

```java
true
false
true
false
```



```java
String s1 = new String("he") + new String("llow");
String s2 = new String("h") + new String("ellow");
String s3 = s1.intern();
String s4 = s2.intern();
System.out.println(s1 == s3);// true
System.out.println(s1 == s4);// true
```





## 网络

  Servlet的生命周期分为5个阶段：加载、创建、初始化、处理客户请求、卸载。 

  (1)加载：容器通过类加载器使用servlet类对应的文件加载servlet 

  (2)创建：通过调用servlet构造函数创建一个servlet对象 

  (3)初始化：调用init方法初始化 

  (4)处理客户请求：每当有一个客户请求，容器会创建一个线程来处理客户请求 

  (5)卸载：调用destroy方法让servlet自己释放其占用的资源 



servlet 线程安全性

  servlet在多线程下其本身并不是线程安全的。 

如果在类中定义成员变量，而在service中根据不同的线程对该成员变量进行更改，那么在并发的时候就会引起错误。最好是在方法中，定义局部变量，而不是类变量或者对象的成员变量。由于方法中的局部变量是在栈中，彼此各自都拥有独立的运行空间而不会互相干扰，因此才做到线程安全。 



单机和分布式的区别



## 微服务

### 概念

微服务是什么？

微服务就是把一个服务达成jar包发布

微服务的好处？

1. 各个功能解耦，减少异常影响范围
2. 整体架构开发麻烦
3. 拆分数据库
4. 针对性的增减机器、扩容

微服务的问题

1. 架构复杂
2. 出现很多跨系统的操作，需要分布式事务管理
3. 调用问题
4. 负载均衡问题

如何解决

...

若一个微服务有多实例，如何正确调用？

初期 nginx

现在 注册中心 nacos

注册中心

多实例的微服务在注册中心里注册，尽管端口不同，但服务名相同，故此，其他微服务系统需要此多实例微服务时，可先去注册中心拿去所有该名称的实例存到本地，然后采用轮询或者其他方式去获取及交互。

Spring Cloud -> Nacos（已经集成ribbon）

在spring boot的pom.xml包引入discovery依赖，并在配置文件中配好application name以及注册中心的地址，每次微服务实例启动时候，会向注册中心调用注册实例的接口。

@LoadBalanced（服务发现以及负载均衡）用注解方式引入Ribbon，以此实现服务发现与负载均衡

Ribbon 根据名称从注册中心拿去所有该微服务的实例，并通过一些负载均衡的机制进行访问（轮询、随机、最小并发等、响应时间加权等）

增加或减少机器如何被其他服务知道

增加的时候会自动注册

健康的实例会定时发送心跳

注册中心的健康检查任务会定时检查实例心跳

去标记长时间没有心跳的实例或删除被关掉的实例

Feign 可以帮你组装url，让你的远端调用像本地一样

如果调用到不健康的实例，用sentine去解决

具体：

降级 ：fallback -> 实现失败后的具体操作，比如记录日志，后续补上

限流：先进行压力测试，配置低于峰值的限流

限流常见有两种：

1. 漏桶
2. 令牌桶 Token Bucket
3. 滑动事时间窗口

Gateway 网关

路由功能：后端有许多个微服务的地址，但全部暴露给前端显示，所以需要Gateway

QPS阈值限制：每秒钟调用次数

熔断：10秒内异常比例超过0.6，这10秒内直接使用降级操作

warm up：在可预见的流量增加之前加一些缓存来应对高并发



## 其他



内容监控



动态代理

java中注解的实现

AnnotationInvocationHandler

从前端到后端的逻辑

事务什么时候回滚：

1. 自动回滚：

   RuntimeException/error

2. 手动回滚

   ```java
   TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
   ```

Exception的分类





java1.8 的GC

在执行机制上JVM提供了串行GC(SerialGC)、并行回收GC(ParallelScavenge)和并行GC(ParNew)：

**Parallel GC** -- (Java1.8 的默认GC),它的速度是最快；

  并行回收GC：在新生代采用复制算法，在老年代采用标记-压缩算法，在整个扫描和复制过程采用**多线程**的方式来进行，适用于多CPU、对暂停时间要求较短的应用上，是server级别默认采用的GC方式，可用-XX:+UseParallelGC来指定，用-XX:ParallelGCThreads=4来指定线程数；

jdk1.7 默认垃圾收集器Parallel Scavenge（新生代【标记-复制算法】）+Parallel Old（老年代【标记整理算法】）

jdk1.8 默认垃圾收集器Parallel Scavenge（新生代）+Parallel Old（老年代）

Parallel Scavenge收集器能够配合自适应调节策略，把内存管理的调优任务交给虚拟机去完成。只需要把基本的内存数据设置好（如-Xmx设置最大堆），然后使用-XX：MaxGCPauseMillis参数（更关注最大停顿时间）或GCTimeRatio参数（更关注吞吐量）给虚拟机设立一个优化目标，那具体细节参数的调节工作就由虚拟机完成了。
 自适应调节策略也是Parallel Scavenge收集器与ParNew收集器的一个重要区别。

synchronized 锁的是什么

- 如果修饰的是`具体对象`：锁的是`对象`；
- 如果修饰的是`成员方法`：那锁的就是 `this` ；
- 如果修饰的是`静态方法`：锁的就是这个`对象.class`。





## 待解决问题

TCP

UDP



kafka activeMQ 比较 为什么kafka更加好

mysql innodb如何解决幻读问题

Innodb的索引结构

为什么要用B+树而不是B树

MQ 多线程消费问题

Redis中的结构

jinfo jstack jstat 等问题 






## Tomcat 



Tomcat是一个Servlet容器



### Servlet容器



一个Servlet容器包括：

response

request

调用servlet的service方法，从request取值，给response写值



### Catalina



Connector 接受HTTP请求，构造request和response对象

container 从connector获取request和response，调用servlet的service方法用于响应



### Web服务器

Web服务器也被称为HTTP服务器（超文本传输协议），一个简单的Web服务器同城要给予Socket类和serverSocket类，并通过HTTP通信。



#### HTTP

HTTP是一种协议，允许Web服务器和浏览器通过互联网进行发送和接收数据。他是一种请求和响应协议。客户端通过TCP连接以及发送HTTP请求开启事务，浏览器接收但不需要回复或者回调。无论是服务器还是浏览器都有权利终止并关闭连接。

##### HTTP请求

分为URI、请求头部、主体内容

##### HTTP响应

分为URI、响应头部、主体内容

##### Socket类
