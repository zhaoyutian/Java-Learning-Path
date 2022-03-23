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
    public void take() {

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

