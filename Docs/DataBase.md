# 数据库基础及概念
## 数据库三大范式

第一范式：数据库表中的字段都是单一属性的，不可再分(保持数据的原子性)；

第二范式：第二范式必须符合第一范式，非主属性必须完全依赖于主键。

第三范式：在满足第二范式的基础上，在实体中不存在其他实体中的非主键属性，传递函数依赖于主键属性，确保数据表中的每一列数据都和主键直接


## DDL（Data Definition Language
数据库模式定义语言，通俗来说就是建表语句，常见如下：

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

## 常见的sql题目

用sql语句查询出各班级最高分的学生信息：

示例表：
```mysql
create table it_student(
id int primary key auto_increment,  -- 主键id
name varchar(20),  -- 姓名
gender enum('male','female'),  -- 性别
class_id tinyint unsigned,  -- 班级号
age int unsigned,  -- 年龄
home varchar(40),  -- 家乡
score tinyint unsigned  -- 成绩
);
```
SQL：

```mysql
select name,home,score from(select * from it_student order by score desc) as s group by class_id
```


## MySQL
特点：插件式储存引擎，根据不同场景使用不同的引擎。引擎是基于表的，而不是数据库。

### Innodb引擎

MySQL 5.1后的默认引擎，MVCC（Mutiversion Concurency Control）实现了完全的事务（ACID），行级锁保证了高并发；聚簇索引（表的储存都是按主键的顺序进行存放）提高对主键查询的效率；自适应哈希索引加速读操作；利用缓冲区加速是插入操作。

#### 事务 ACID

Atomicity 原子性： 不可分割，要么全部成功，要么全部失败。
Consistency 一致性：没有部分成功部分失败的情况。
Isolation 隔离性：事务提交以前对其他事务不可见。具体分四个等级：
- read uncommited 未提交读，事务未提交的修改对其他事务可见，所以会出现脏读。
- read committed 提交读，事务提交之前的修改对其他事务不可见，但两次相同的查询可能会出现不同的结果，即会出现幻读。
- repeatable read 可重复读，同一事务相同查询保证能取得相同结果，利用MVCC实现。
- Serializable 串行化，每一行都加锁，保证没有并发来解决一致性问题。
Durability 持久性：修改永久保留，崩溃也不会丢失。

#### MVCC（多版本并发控制）
利用创建时间和删除时间来控制不同事务的并发问题。 每一次CRUD都是一次事务，每个事务生成一个自增ID，通过修改创建时间和删除时间并和事务ID对比来进行控制。MVCC默认隔离级别是repeatable read（即可重复读，可以避免幻读），具体实现：

INSERT： 事务ID-> 创建时间
DELETE：事务ID -> 删除时间
UPDATE：插入新记录 事务ID->创建时间；原来记录 事务ID->删除时间
SELECT：创建时间< 当前事务ID 且 （当前事务ID < 删除时间  或 删除时间为空）

自动提交模式，每个查询都被当作一个事务执行提交操作，可以设置ATUOCOMMIT变量来启动或禁用此模式。在执行ALTER TABLE， LOCK TABLE等语句时也会强制执行COMMIT提交当前的活动事务。
两阶段锁定协议，在事务执行过程中随时锁定，但只有在COMMIT或ROLLABCK的时候才会统一释放锁。

#### InnoDB原理
1. 线程
    1. master thread
    2. IO thread
    3. page cleaner thread
    4. purge thread

2. 内存
    1. 缓冲池：由于CPU速度与磁盘速度之间的鸿沟，用缓冲池提高性能。读取的页放入缓冲池，下次读取直接读缓冲池数据；修改时先修改缓冲池数据，再异步刷新到磁盘中，刷新由Checkpoint机制控制。缓冲池存的数据类型有：索引页、数据页、undo页、插入缓存、自适应哈希索引、锁信息、数据字典等。缓冲区的管理通过LRU(Latest Recent Used)算法管理，使用最频繁的页再最前端，使用最少的再最尾端，新页无法存时首先释放尾端页。新页首先放在midpoint位置（37%）而不是最前端，保证热点数据没有频繁变化且非热点数据不会混入。
       Checkpoint：分为Fuzzy Checkpoint部分刷新（刷新一部分脏页而不是所有）、Sharp Checkpoint全部刷新（数据库关闭时将所有脏页刷新回磁盘）。由page clearner thread维护。

### MySQL其他引擎

#### MyISAM引擎
MySql5.1以前版本的默认引擎，不支持事务，表锁。优势：支持全文索引、压缩、空间函数。读多写少的业务，不介意崩溃恢复问题。

#### archive引擎
只支持INSERT和SEELCT操作，支持行级锁以及有专门的缓冲区，插入时会进行压缩，索引磁盘I/O更少，能实现高并发的插入。适合做日志。

#### memory引擎
cu你在内存中，不需要磁盘I/O，速度很快。是表级锁，并发写性能低，不支持BLOB或TEXT类型（长字段），长度固定。重启数据会丢失。适合做映射表（比如邮编和洲名映射表）或中间表。

#### csv引擎
将csv文件作为MySQL的表进行处理，不支持索引。可以作为数据交换的机制，

#### NDB引擎  MySQL Cluster
集群储存引擎，分布式、sharing-nothing、容灾、高可用。


