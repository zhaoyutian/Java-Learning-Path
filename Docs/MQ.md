# 消息队列 MQ（Message Queue）

## 消息队列的核心（优势）

1. 解耦：统一模板、分工合作、并行开发、互不干扰
2. 异步：将发送信息、接收信息和处理信息分开，提高可用性
3. 削峰：减少短时间内大量数据的冲击

结合qb系统：

爬虫团队和后台开发并行，发布-订阅的设计模式（上下游）加快进度、降低耦合。
因为光闸、VPN流量、上游爬虫处理等限制、大量数据可能在同一时间发送，需要进行削峰。
避免宕机、系统升级等问题出现数据丢失。

## 消息队列的劣势

1. 复杂度提高，可能出现数据重复消费（幂等性）、数据丢失、数据积压、数据过期失效、数据顺序性等问题。
2. 可用性降低，MQ挂了可能导致系统出错。
3. 数据一致性问题。



### 数据积压

分析问题产生的可能性、产生的后果、手头的资源以及恢复故障承受的代价。
如果仅仅是测试环境或者产品试用期间或者是实时性不太强的场景，可以着手调查consumer的问题，是服务宕机，数据库问题，还是逻辑问题。然后修复上线，恢复消费。
如果是实时性要求比较高的场景，而且积压的数据量非常大，可以着手扩容，新建topic，增加partition，用多台机器部署consumer，一起消费入库。



### 重复消费（幂等性）问题

kafka等中间件是不主动维护幂等性的，但系统消费了某条信息，但是没有成功提交offset就被kill了，或者被系统重启了，就会导致已经入库的消息被重复消费。解决幂等性问题的方法是**添加唯一主键** ，根据业务场景设计逻辑，比如主键相同就update，否则insert即可，或者主键重复直接丢掉。





### 数据顺序性问题

出现的场景：以Kafka为例，若要同步案件信息，指定案件ID为key，同key放入同一个partition，此时的消息是顺序的，此时消费者单线程消费，就能保证顺序，但如果消费者多线程消费，就无法保证顺序。
如果是吞吐量低的场景，只用一个topic，一个partition，一个consumer，内部单线程消费，就能保证顺序问题。
如果要保证吞吐量，可以在每个线程中维护一个queue，然后通过queue入库即可。

### 数据丢失问题

数据流转过程：生产者->kafka->消费者->返回offset 数据消费完成

丢失可能的场景：

1. 生产者没发，这个严格意义上不算数据丢失，但在实际情况中并不少见。我当时遇到的情况就是我需要每天消费前一天所有案件信息，但是上游生产者的sql有问题，逻辑是每天大于前一天零点到小于23点59分29秒的数据，但这样会漏调等于零点的数据，而上游有些数据就是在零点产生的或者搬运过来的，这就导致数据没有同步过来。查找这类问题的方法也比较简单，两张数据库根据id对比一下，然后把缺少的数据捞出来一看，就发现数据产生时间都是零点，在排除了我下游服务或kafka零点宕机升级的可能性后，唯一最大可能就是生产者根本没发。

2. kafka宕机

3. 消费到了就自动offset，但是还没处理完系统就宕机了->可以设置手动提交offset

4. 消费到内存queue中，queue还没处理，系统就重启了->放弃用内存缓冲，或者建立完善的log，用以恢复，但要注意删除历史log，否则会导致log太多。

   



### 可用性问题

kafka 主副机制，leader选举

一条消息只有被LSR中的所有followers都从leader中复制过去，才会被认为已提交。若leader宕机，则需要从剩余follower中选举出新的leader

todo

各个MQ的优劣比较

| 对比方向 | 概要                                                         |
| -------- | ------------------------------------------------------------ |
| 吞吐量   | 万级的 ActiveMQ 和 RabbitMQ 的吞吐量（ActiveMQ 的性能最差）要比 十万级甚至是百万级的 RocketMQ 和 Kafka 低一个数量级。 |
| 可用性   | 都可以实现高可用。ActiveMQ 和 RabbitMQ 都是基于主从架构实现高可用性。RocketMQ 基于分布式架构。 kafka 也是分布式的，一个数据多个副本，少数机器宕机，不会丢失数据，不会导致不可用 |
| 时效性   | RabbitMQ 基于 erlang 开发，所以并发能力很强，性能极其好，延时很低，达到微秒级。其他三个都是 ms 级。 |
| 功能支持 | 除了 Kafka，其他三个功能都较为完备。 Kafka 功能较为简单，主要支持简单的 MQ 功能，在大数据领域的实时计算以及日志采集被大规模使用，是事实上的标准 |
| 消息丢失 | ActiveMQ 和 RabbitMQ 丢失的可能性非常低， RocketMQ 和 Kafka 理论上不会丢失。 |

模型中心用的是ActiveMQ，因为其吞吐量很低，对延时要求也很低，社区比较成熟，版本很稳定，适合内网低吞吐量的场景，加上公司之前有类似的项目，代码复用率比较高。
qb系统用的是Kafka，因为吞吐量比较大，功能简单，就是外网情报向内网的信息传输，并且考虑到之后有可能增加的业务，kafka分布式架构，可拓展性也比较好，主副本也不会丢失数据。加上Kafka本身社区活跃度也很高，也算是大数据方面的行业标准。
RabbitMQ是elang语言，有学习成本。
RocketMQ是阿里系出品，跟我们是合作竞争关系


### 限流方式

#### 计数器

利用原子类进行计数，确保线程安全，在单位时间内，若访问数小于最大访问量，则请求统一，否则限流。

劣势，若单位时间内的请求分布不均衡，导致很短时间内访问超过阈值，则会导致崩溃。比如设置60秒内的处理阈值为600，系统在58-59秒内收到500+的请求，导致系统处理不过来。

```java
public class Counter {
    /**
     * 最大访问数量
     */
    private final int limit = 10;
    /**
     * 访问时间差
     */
    private final long timeout = 1000;
    /**
     * 请求时间
     */
    private long time;
    /**
     * 当前计数器
     */
    private AtomicInteger reqCount = new AtomicInteger(0);

    public boolean limit() {
        long now = System.currentTimeMillis();
        if (now < time + timeout) {
            // 单位时间内
            reqCount.addAndGet(1);
            return reqCount.get() <= limit;
        } else {
            // 超出单位时间
            time = now;
            reqCount = new AtomicInteger(0);
            return true;
        }
    }
}

```

#### 滑动窗口

增加时间粒度，每个粒度独立计数。核心是维护线程安全的队列，每次尝试获取都要判断队列长度，满足则增加到队列中，否则限流，同时创建永续线程定时清理过期队列。

```java
package com.example.demo1.service;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class TimeWindow {
    private ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<Long>();

    /**
     * 间隔秒数
     */
    private int seconds;

    /**
     * 最大限流
     */
    private int max;

    public static void main(String[] args) throws Exception {
    /**
     * 获取令牌，并且添加时间
     */
    public void take(){

        long start = System.currentTimeMillis();
        try {

            int size = sizeOfValid();
            if (size > max) {
                System.err.println("超限");

            }
            synchronized (queue) {
                if (sizeOfValid() > max) {
                    System.err.println("超限");
                    System.err.println("queue中有 " + queue.size() + " 最大数量 " + max);
                }
                this.queue.offer(System.currentTimeMillis());
            }
            System.out.println("queue中有 " + queue.size() + " 最大数量 " + max);
        } catch (Exception e){
            e.printStackTrace();
            }
        }
    }

    public int sizeOfValid() {
        Iterator<Long> it = queue.iterator();
        Long ms = System.currentTimeMillis() - seconds * 1000;
        int count = 0;
        while (it.hasNext()) {
            long t = it.next();
            if (t > ms) {
                // 在当前的统计时间范围内
                count++;
            }
        }
        return count;
    }

    /**
     * 清理过期的时间
     */
    public void clean() {
        Long c = System.currentTimeMillis() - seconds * 1000;

        Long tl = null;
        while ((tl = queue.peek()) != null && tl < c) {
            System.out.println("清理数据");
            queue.poll();
        }
    }
}
```





## Kafka

### Kafka是什么

1. 流平台，用来发布订阅数据流
2. 分布式系统，以集群方式运行，自由伸缩
3. 存储系统，可以保持数据，保证数据传输可复制、持久化



### 一些概念

数据单元被称为**消息**，为提高效率，消息被分批次写入Kafka，**批次**是一组消息。如果每一个消息都单独传输，会导致大量的网络开销，把消息分批次传输可以减少网络开销。但批次越大，单位时间内处理的消息越多，单个消息的传输时间就越长。所以需要我们在时间延迟和吞吐量之间u呕出权衡。

消息**模式**（schema）维护了数据格式的一致性，消除了消息读和写操作的耦合性。

kafka消息通过**主题**分类，不同类别的消息分别交由不同主题，消费者也可也根据需求定于自己感兴趣的主题而不需要全部信息。同一个主题又分为若干个**分区**，分区内部先入先出，可以保证顺序，但同一主题下的消息顺序难以保持一致。Kafka通过分区实现数据冗余和伸缩性，分区可以分布在不同的服务器上，换言之，一个主题可以横跨多个服务器，这比单一服务器性能更高。

Kafka的客户端分为两种，一种是生产者，一种是消费者。**生产者**创建消息，消息会被发布到特定主题中，默认情况下消息会均衡分布在主题的所有分区上，特定情况生产者可以指定分区。一般来说，消息到哪个分区由**分区器**和消息键决定，分区器为键生成散列值，映射到不同分区，用户也可以自定义分区器。**消费者**读取消息，消费者可以订阅一个或者多个主题，，并按照消息生成的顺序读取。通过检查**偏移量**区分是否读取过此消息。**偏移量**是递增整数，每个消息的偏移量都是唯一的，消费者把每个分区最后读取到的消息偏移量保存在Kafka中，过消费者宕机或者重启，可以从此偏移量后继续读取，而不会丢失其读取状态。

一个独立的Kafka服务器被称为**broker**，broker负责接生产者的消息，设置偏移量，提交消息到磁盘保存，同时为消费者提供服务，对读取分区请求做出相应，返回磁盘中的消息。一个broker可以处理数千分区以及每秒百万计的消息量。

broker是**集群**的组成部分。每个集群都有一个broker同时充当**集群控制器**的角色（自动从活跃成员选举出来）。集群控制器负责管理工作，包括分区分配、监控broker等。一个分区从属于一个broker，此broker被成为这个分区的**首领**。一个分区可以分配多个broker，者会发生**分区复制**，提供消息冗余，当某个broker失效，可以由其他broker接管领导权。

**保留消息**。Kafka在一定时间（比如7天）、一定空间内（比如1GB）会保留消息，由参数配置（log.retention.ms和log.retention.bytes）。同时，用户可以根据不同主题配置保留策略。



### 为什么选择Kafka

1. 多个生产者。Kafka支持多个生产者，不管是使用单个主题还是多个主题。

2. 多个消费者。Kafka支持多个消费者从一个消息流上读取数据，且消费者们互不影响。也支持多个消费者组成一个群组，共享一个消息流，整个群组对每个给定的消息至处理一次。
3. 基于磁盘的数据存储。因为数据保留的特性，Kafka支持消费者非实时地读取消息。在面对流量徒增活消费者处理速度降时，Kafka的持久化和数据保留机制，可以保证消费者可以离线维护一段时间而不用担心消息丢失。
4. 伸缩性。用户可以从单个broker扩展到3个broker的小型开发集群，甚至到生产环境的上百个broker集群。
5. 高性能。横向扩展生产者、消费者和broker，可以轻松处理大量数据且亚秒级的消息延迟。



### 如何选定分区数量

- 主题需要达到多大的吞吐量，每秒钟写入100KB还是1GB？
- 单个分区读取数据的最大吞吐量的多少？每个分区一般有一个消费者，如果该消费者写入数据库的速度不超过每秒50MB，那读取吞吐量也不会超过每秒50MB。
- 通过类似方法估算生产者向单个分区写入数据库的吞吐量。

如果每秒需要从主题上写入和读取1GB的数据，且每个消费者每秒可以处理50MB的数据，那么至少需要20个分区。



### 需要多少个Broker

一个Kafka集群需要多少个broker，取决于磁盘空间和集群处理请求能力。

- 如果集群需要保留10TB的数据，每个broker可以存2TB，那么至少需要5个broker，如果启用了数据复制，则至少还需要一般空间。
- 如果单个broker的网络接口在高峰时可以达到80%的使用量，丙炔有两个消费者，那么消费者就无法保持峰值，除非有两个broker。



### 生产者

生产者产生消息的过程：



![image-20220324154018133](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220324154018133.png)

实例化生产者对象后，就可以发送小心 ，消息的发送一共有三种方式

- **发送并忘记** 我们把消息发送到服务器，并不关心它是否正常到达，Kafka是高可用的，且生产者会自动尝试重发，但此方法还是可能会丢失一些消息。

  以下就是发送并忘记的示例代码：

  ```java
  ProducerRecord<String,String> record = new ProducerRecord<>("CustomerCountry","Precision Products","France");
  try{
  	producer.send(record);
  } catch(Exception e){
  	e.printStackTrace();
  }
  ```

  

- **同步发送** 调用send()方法发送消息，返回Future对象，调用get()方法进行等待，就可以知道消息是否发送成功。

  get()方法就是同步发送与发送并忘记的区别

  ```java
  roducerRecord<String,String> record = new ProducerRecord<>("CustomerCountry","Precision Products","France");
  try{
  	producer.send(record).get();
  } catch(Exception e){
  	e.printStackTrace();
  }
  ```

  

- **异步发送** 调用send()方法，并指定回调函数，服务器在返回响应时调用该函数。

  定义回调函数（实现Callback接口），重写onCompletion()方法，并在send中指定此回调函数。

  ```java
  private calss DemoProducerCallback implements Callback{
  	@Override
  	public void onCompletion(RecordMetadata recordMetadata, Exception e){
  		if(e!=null){
  			e.printStackTrace();
  		}
  	}
  }
  
  roducerRecord<String,String> record = new ProducerRecord<>("CustomerCountry","Precision Products","France");
  producer.send(record,new DemoProducerCallback());
  ```

  

#### 生产者的配置

- **acks** acks参数指定必须要有多少分区副本收到消息，生产者才会认为消息写入是成功的。如
  - 若acks=0，生产者无需等待任何我服务器的响应，这样容易丢失消息，但也无需等待服务器响应，有很高的吞吐量
  - 若ack=1，只要集群的首领节点收到消息，生产者就会收到成功响应。若此时无首领节点（首领崩溃，选举未完成），生产者就会收到错误响应并重发。不过，如果一个没有收到消息的节点成为新首领，此消息还是会丢失。
  - 若ack=all，只有所有参与复制的节点全部收到消息，生产者才会收到成功响应，整个模式最为安全，但延迟更高。
- **buffer.memory** 缓冲区大小
- **compression.type** 压缩方式，默认不压缩，可选snappy/gazip/lz4。
- **retires** 生产者重发消息的次数。默认是每次重试之间等待100ms（可由retry.backoff.ms参数控制）



#### 分区机制

若键值为null，分区器默认使用**轮询算法**将消息均衡分布在各个分区上。

若键值不为空，Kafka会对键进行散列，根据散列值映射到不同分区上。也可以自定义分区器，给特定用户单独分区。



### 消费者

消费者从属于**消费者群组**，一个群组里的消费者订阅同一个主题，每个消费者接收主题一部分分区的消息。

假设某主题有4个分区，一个消费者群组订阅这个主题，群组中仅有一个消费者，那此消费者将收到4个分区的消息。

![image-20220324170601918](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220324170601918.png)

假设某主题有4个分区，一个消费者群组订阅这个主题，群组中有2个消费者，那这两个消费者分别消费两个分区。

![](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220324170459564.png)

4个消费者情况

![image-20220324170948210](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220324170948210.png)

5个消费者情况，会有一个消费者闲置

![image-20220324170922503](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220324170922503.png)

2个消费者群组情况

![image-20220324170909957](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220324170909957.png)

#### 群组与再均衡

当消费者群组的消费者共同读取主题的分区，当新的消费者加入时，原本由其他消费者读取的消息会被分配给它读取，当一个消费者崩溃或关闭时，原本由它读取的分区会转给群组中的其他消费者读取，当主题变化比如添加新的分区时，会发送分区重分配。

分区所有权从一个消费者到另一个，被称为**再均衡**。再均衡保证了高可用和伸缩性，但不必要的在均衡可能会导致读取状态丢失以及缓存刷新拖慢程序。

消费者通过向被指派为**群组协调器** 的broker发送**心跳**维持群组从属关系和分区所有权关系。消费者会再固定时间间隔、轮询消息和提交偏移量时发送心跳。若消费者停止发送超过一定时间，比如消费者崩溃，群组协调器会等待几秒，确认其死亡后就会触发再均衡。



#### 创建消费者

创建KafkaConsumer对象，需要几个属性：bootstrap.servers（连接字符串）、key.deserializer（键的反序列化）、value.deseriaizer（值的反序列化）、group.id（不是必须的，指定了消费群组，如果不从属于群组，可以为null）

```java
Properties props = new Properties();
props.put("bootstrap.servers","broker1:9092,broker2:9092");
props.put("group.id","CountryCounter");
props.put("key.deserializer","org.apache.kafka.common.serialization.StriingDeserializer");
props.put("value.deserializer","org.apache.kafka.common.serialization.StriingDeserializer");
KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(props);
```

#### 订阅主题

消费者可以调用subscribe()方法完成订阅，此方法接受主题列表、正则表达式等方式作为入参

```java
consumer.subscribe(Collections.singletionList("cistomerCountries"));
consumer.subsscribe("test.*");
```

#### 轮询

消费者订阅某主题，轮询会处理其中的所有细节，包括群组协调、分区再均衡、发送心跳、获取数据。

```java
try{
    // 一直保持轮询，否则会被认为死亡
	while(true){
        // poll方法传递的是超时时间
        // records中记录所属主题信息，分区信息、偏移量、键值对等
		ConsumerRecords<String,String> records = consumer.poll(100);
		for(ConsumerRecord<String,String> record:records){
			log.debug("topic=%s,partition=%s,iffset=%d,consumer=%s,contry=%s\n",
             		record.topic(),record.artition(),record.offset(),record.key(),record.value());
			int updatedCount = 1;
            if(CustCountryMap.containsValue(record.value())){
                updatedCount = custCountryMap.get(record.value())+1;
            }
            custCountryMap.put(record.value(),updatedCount);
            JsonObject json - nee JSONObject(cstCountryMap);
            System.out.println(json.toString(4));
		}
	} finally{
        // 退出程序时关闭消费者，群组协调器收到关闭通知后会出发再均衡，保证数据会被正常消费
		consumer.close();
    }
}
```



#### 提交与偏移量

调用poll()方法，返回还没被消费者读取过的记录，我们可以基于此追踪哪些记录被群组里的哪个消费者读取。更新分区当前位置的操作成为**提交**。消费者往_consumer_offset的主题发送消息，此消息包含每个分区的偏移量。

Kafka提供简单自动的提交方式，即自动提交偏移量。当enable.auto.commit设置为true时，每隔5s（这个由auto.commit.inerval.ms控制，默认是5s），消费者会自动把poll()方法接收到的最大偏移量提交上去。但若是在者5s期间发生了再均衡，比如在最近一次提交的3s添加了新的消费者在此群组，此消费者从最后一次提交的偏移量位置开始读取消息，但此时消息已经落后3s了，者3s内的消息会被重复消费。究其根源，就是啊自动提交并不知道具体哪些消息被处理，想要避免此情况，就需要开发者在处理完消息后调用commitSync()方法去手动提交。

```java

while(true){  
	ConsumerRecords<String,String> records = consumer.poll(100);
	for(ConsumerRecord<String,String> record:records){
		log.debug("topic=%s,partition=%s,iffset=%d,consumer=%s,contry=%s\n",
             record.topic(),record.artition(),record.offset(),record.key(),record.value());
	}
	try{
		consumer.commitSync();
	} catch (Exception e){
		log.error("commit faild",e);
	}
}
```

commitSync()若失败会一直重试，以保证偏移量正确提交。但这样，在broker对提交请求作出回应之前，程序会一直阻塞，这会限制应用程序的吞吐量，我们可以通降低体提交频率莱提升吞吐量，但如果发生再均衡，会增加重复消息的数量。

```java
while(true){  
	ConsumerRecords<String,String> records = consumer.poll(100);
	for(ConsumerRecord<String,String> record:records){
		log.debug("topic=%s,partition=%s,iffset=%d,consumer=%s,contry=%s\n",
             record.topic(),record.artition(),record.offset(),record.key(),record.value());
	}
	consumer.commitAsync();
}
```

commitAsync()不会重试。如果commitAsync()希望提交偏移量2000，但是由于通信问题没有顺利提交，而此时有另一批数据被处理，成功提交了偏移量3000。如果commitAsync()重新尝试，那么偏移量2000会在最后成功提交，此时就会出现重复消息。所以在commitAsync()方法之后进行回调并记录错误，可以有效避免这类问题。

```java
while(true){  
	ConsumerRecords<String,String> records = consumer.poll(100);
	for(ConsumerRecord<String,String> record:records){
		log.debug("topic=%s,partition=%s,iffset=%d,consumer=%s,contry=%s\n",
             record.topic(),record.artition(),record.offset(),record.key(),record.value());
	}
	consumer.commitAsync(new OffsetCommitCallback){
		public void onComplete(Map<TopicPartition,OffsetAndMetadata> offsets, Exception e){
			if(e!=null){
				log.error("commit failed for offsets {}",offsets, e);
			}
		}
	});
}
```

若出现网络通信等偶发问题，不进行重试没有太大问题，因为这次失败，后续提交总会有成功的，只要保证最后偏移量正确即可。但若是发生在关闭消费者或者再均衡前的最后一次提交，就要确保成功，可以使用同步和异步组合提交的方式。

```java
try{
	while(true){
		ConsumerRecords<String,String> records = consumer.poll(100);
		for(ConsumerRecord<String,String> record:records){
			log.debug("topic=%s,partition=%s,iffset=%d,consumer=%s,contry=%s\n",
             		record.topic(),record.artition(),record.offset(),record.key(),record.value());
		}
        // 通常情况下用异步提交，提高吞吐量
		consumer.commitAsync();
	} catch (Exception e){
		log.error("Unexpected error",e);
	}
	finally{
		try{
			// 退出程序或关闭消费者之前，使用同步提交，同步提交会一直重试知道成功，可以保证最后偏移量无误。
			consumer.commitSync();
		}
        // 退出程序时关闭消费者，群组协调器收到关闭通知后会出发再均衡，保证数据会被正常消费。
		consumer.close();
    }
}
```



#### 退出

通过另一个线程调用consumer.wakeup()方法。如果循环在主程序李，可以在ShutdownHook里调用该方法。

```java
Runtime.getRunTime().addShutdownHook(new Thread(){
	public void run(){
		System.out.println("Start exit");
		consumer.wakeup();
		try{
			mainThread.join();
		} catch(InterruptedExpcetion e){
			e.printStackTrace();
		}
	}
});
```

