### 1. ACID靠什么保证的

```
A原子性由undo log日志保证，它记录了需要回滚的日志信息，事务回滚时撤销已经执行成功的sql
C一致性由其他三大特性保证，程序代码要保证业务上的一致性
I隔离性有MVCC保证
D持久性由内存+redo log日志保证，mysql修改数据同时在内存和redo log记录这次操作，宕机的时候可以从redo log恢复

InnoDB redo log写盘，InnoDB事务进入prepare状态
如果前面prepare成功，binlog写盘，再继续将事务日志持久化到binlog，如果持久化成功，那么InnoDB事务则进入commit状态(在redo log里面写一个commit记录)

redo log的刷盘会在系统空闲时进行
```

### 2. B树和B+树的区别，为什么Mysql使用B+树

```
B树的特点:
	1. 节点排序
	2. 一个节点里可以存多个元素，多个元素也排序了
	
B+树的特点:
	1. 拥有B树的特点
	2. 叶子节点之间有指针
	3. 非叶子节点上的元素在叶子节点上都冗余了，也就是叶子节点中存储了所有的元素，并且排好顺序了
	
Mysql索引使用的是B+树，因为索引是用来加快查询的，而B+树通过对数据进行排序所以是可以提高查询速度的，然后通过一个节点可以存储多个元素，从而可以使得B+树的高度不会太高，在Mysql中一个InnoDB页就是一个B+树节点，一个InnoDB页默认16kb，所以一般情况下一颗两层的B+树可以存2000万行左右的数据，然后通过B+树叶子节点存储了所有数据并且进行了排序，并且叶子节点之间有指针，可以很好的支持全表扫描，范围查找等SQL语句
```

### 3. Explain语句结果中各个字段分别表示什么

| 列名          | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| id            | 查询语句中每出现一个SELECT关键字，Mysql就会为它分配一个唯一的id值，某些子查询会被优化为join查询，那么出现的id会一样 |
| select_type   | SELECT关键字对应的那个查询的类型                             |
| table         | 表名                                                         |
| partitions    | 匹配的分区信息                                               |
| type          | 针对单表的查询方式(全表扫描，索引)                           |
| possible_keys | 可能用到的索引                                               |
| key           | 实际上用到的索引                                             |
| key_len       | 实际使用到的索引长度                                         |
| ref           | 当使用索引列等值查询时，与索引列进行等值匹配的对象信息       |
| rows          | 预估的需要读取的记录条数                                     |
| filtered      | 某个表经过搜索条件过滤后剩余记录条数的百分比                 |
| Extra         | 一些额外的信息，比如排序等                                   |

### 4. InnoDB是如何实现事务的

```
InnoDB通过Buffer Pool，LogBuffer，Redo Log，Undo Log来实现事务，以一个update语句为例:
	1. InnoDB在收到一个update语句后，会先根据条件找到数据所在的页，并将该页缓存在Buffer Pool中
	2. 执行update语句，修改Buffer Pool中的数据，也就是内存中的数据
	3. 针对update语句生成一个RedoLog对象，并存入LogBuffer中
	4. 针对update语句生成undoLog日志，用于事务回滚
	5. 如果事务提交，那么则把RedoLog对象进行持久化，后续还有其他机制将Buffer Pool中所修改的数据页持久化到磁盘中
	6. 如果事务回滚，则利用UndoLog日志进行回滚
```

### 5. Msyql的索引结构是什么样的？聚簇索引和非聚簇索引

```
二叉树 --> AVL树 --> 红黑树 --> B-树 --> B+树
二叉树: 每个节点最多只有两个子节点，左边的子节点逗比当前节点小，右边的子节点都比当前节点大
AVL树: 树中任意节点的两个子树的高度差最大为1
红黑树: 
	1. 每个节点都是红色或黑色
	2. 根节点是黑色
	3. 每个叶子节点都是黑色的空节点
	4. 红色节点的父子节点都必须是黑色
	5. 从任一节点到其每个叶子节点的所有路径都包含相同的黑色节点
B-树:
	1. B-树的每个非叶子节点的子节点个数都不会超过D(这个D就是B-树的阶)
	2. 所有的叶子节点都在同一层
	3. 所有节点关键字都是按照递增顺序排列
B+树:
	1. 非叶子节点不存储数据，只进行数据索引
	2. 所有数据都存储在叶子节点当中
	3. 每个叶子节点都存有相邻叶子节点指针
	4. 叶子节点按照本身关键字从小到大排序
```

```
聚簇索引就是数据和索引是在一起的
MyISAM使用的是非聚簇索引，树的子节点上的data不是数据本身，而是数据存放的地址
InnoDB采用的是聚簇索引，树的叶子节点上的data就是数据本身

聚簇索引的数据物理存放顺序和索引顺序是一致的，所以一个表当中只能有一个聚簇索引，而非聚簇索引可以有多个
InnoDB中，如果表定义了PK，那PK就是聚簇索引。如果没有PK,就会找第一个非空的unique列作为聚簇索引，否则，InnoDB会创建一个隐藏的row-id作为聚簇索引

Mysql的覆盖索引和回表
	如果只需要在一颗索引树上就可以获取SQL所需要的所有列，就不需要再回表查询，这样查询速度就可以更快
	实现索引覆盖最贱的方式就是将要查询的字段，全部建立到联合索引当中
```

```sql
user (PK id, name, sex)
select count(name) from user;   ----> 在name字段上建立一个索引
select id, name, sex from user;  ----> 将name上的索引升级成为(name,sex)的联合索引
```

### 6. Mysql的锁有哪些？什么是间隙锁？

```
从锁的粒度区分
	1. 行锁:
		行锁粒度小，但是加锁资源开销比较大，InnoDB支持
		共享锁: 读锁，多个事务可以对同一个数据共享同一把锁，持有锁的事务都可以访问数据，但是只能读不能修改，select xxx LOCK IN SHARE MODE.
		排他锁: 写锁，只有一个事务能够获得排他锁，其他事务都不能获取该行的锁。InnoDB会对 update/delete/insert 语句自动添加排他锁。SELECT xxx FROM UPDATE。
		自增锁: 通常是针对Mysql当中的自增字段，如果有事务回滚这种情况，数据会回滚，但是自增序列不会回滚
	2. 表锁:
		表共享读锁
		表排他写锁
		意向锁: 是InnoDB自动添加的一种锁，不需要用户干预
	3. 全局锁: 
		Flush tables with read lock。加锁之后整个数据库实例都处于只读状态，所有的数据变更操作都会被挂起，一般用于全库备份的时候
		
		常见的锁算法: 
			user:userid(1,4,9) update user set xxx where userid = 4;
			user:userid(1,4,9) update user set xxx where userid = 5;REPETABLE READ 间隙锁锁住(5,9)
			(-xx, 1)(1,4)(4,9)(9,+xx)
			1. 记录锁: 锁一条具体的数据
			2. 间隙锁: RR隔离级别下，会加间隙锁。锁一定的范围，而不锁具体的记录，是为了防止产生幻读
			3. Next-key: 间隙锁 + 右记录锁。(-xx, 1](1,4](4,9](9,+xx)
```

### 7. Mysql的集群是如何搭建的？读写分离是怎么做的？

```
Mysql通过将主节点的BinLog同步给从节点完成主从之间的数据同步
Mysql的主从集群只会讲BinLog从主节点同步到从节点，而不会反过来同步，由此也就引申出了读写分离的问题。
因为要保证主从之间的数据一致，写数据的操作只能在主节点完成，而读数据的操作，可以在主节点或者从节点上完成。
```

### 8. Mysql聚簇和非聚簇索引的区别

```
都是B+树的数据结构:
	1. 聚簇索引:
		将数据存储与索引放到了一块，并且是按照一定的顺序组织的，找到索引也就找到了数据，数据的物理存放顺序与索引顺序是一致的，即: 只要索引是相邻的，那么对应的数据一定也是相邻地存放在磁盘上的
	2. 非聚簇索引:
		叶子节点不存储数据，存储的数据行地址，也就是说根据索引查找到数据行的位置再去磁盘查找数据，这个就有点类似一本书的目录，比如我们要找第三章第一节，那我们就先在这个目录里面找，找到对应的页码再去对应的页码看文章
		
优势:
	1. 查询通过聚簇索引可以直接获取数据，相比非聚簇索引需要二次查询(非覆盖索引的情况下)效率更高
	2. 聚簇索引对于范围查询的效率更高，因为其数据是按照大小排列的
	3. 聚簇索引适合用在排序的场合，非聚簇索引不适合
劣势:
	1. 维护索引很昂贵，特别是插入新行或者主键被更新导致要分页(page split)的时候，建议在大量插入新行后，选在负载较低的时间段，通过OPTIMIZE TABLE优化表，因为必须被移动的行数据可能造成碎片，使用独享空间可以弱化碎片
	2. 表因为使用UUID(随机ID)作为主键，使数据存储稀疏，这就会出现聚簇索引有可能有比全表扫描更慢，所以建议使用int的auto_increment作为主键
	3. 如果主键比较大的话，那辅助索引将会变得更大，因为辅助索引的叶子存储的是主键值，过长的主键值，会导致叶子节点占用更多的物理空间
	
InnoDB中一定有主键，主键一定是聚簇索引，不手动设置，则会使用unique索引，没有unique索引，则会使用数据库内部的一个行的隐藏id来当作主键索引，在聚簇索引智商创建的索引称之为辅助索引，辅助索引访问数据总是需要二次查找，非聚簇索引都是辅助索引，像符合索引，前缀索引，唯一索引，辅助索引叶子节点存储的不再是行的物理位置，而是主键值

MyISAM使用的是非聚簇索引，没有聚簇索引，非聚簇索引的两个B+树看上去没什么不同，节点的结构完全一致只是存储的内容不同而已，主键索引B+树的节点存储了主键，辅助索引B+树存储了辅助键，表结构存储在独立的地方，这两颗B+树的叶子节点都使用一个地址指向真正的表数据，对于表数据来说，这两个键没有任何差别，由于索引树是独立的，通过辅助键检索无需访问主键的索引树。

如果涉及到大数据量的排序，全表扫描，count之类的操作的话，还是MyISAM占优势写，因为索引所占空间小，这些操作是需要在内存中完成的。
```

### 9. Mysql慢查询该如何优化？

```
1. 检查是否走了索引，如果没有则优化SQL利用索引
2. 检查所利用的索引，是否是最优索引
3. 检查所查字段是否都是必须的，是否查询了过多字段，查出了多余的数据
4. 检查表中数据是否过多，是否应该进行分库分表了
5. 检查数据库实例所在的机器的性能配置，是否太低，是否可以适当增加资源
```

### 10. Mysql索引的数据结构，以及各自优劣

```
索引的数据结构和具体的存储引擎的实现有关，在Mysql中使用较多的索引有Hash索引，B+树索引等，InnoDB存储引擎的默认索引实现为: B+树索引，对于哈希索引来说，底层的数据结构就是哈希表，因此在绝大多数需求为单条记录查询的时候，可以选择哈希索引，查询性能更快;其余大部分场景，建议选择BTree索引
B+树:
	B+树是一个平衡多叉树，从根节点到每个叶子节点的高度差值不超过1，而且同层级的节点间有指针相互链接，
	在B+树上的常规检索，从根节点到叶子节点的搜索效率基本相当，不会出现大幅波动，而且基于索引的顺序扫描时，也可以利用双向指针快速左右移动，效率非常高，因此，B+树索引被广泛应用于数据库，文件系统等场景
	
Hash索引:
	hash索引就是采用一定的哈希算法，把键值换算成新的哈希值，检索时不需要类似B+树那样从根节点到叶子节点逐级查找，只需一次哈希算法即可立刻定位到相应的位置，速度非常快
	如果是等值查询，那么哈希索引明显有绝对优势，因为只需要经过一次算法即可找到相应的键值，前提是键值都是唯一的，如果键值不是唯一的，就需要先找到该键所在位置，然后再根据链表往后扫描，直到找到相应的数据
	哈希索引也不支持多列联合索引的最左匹配原则;
	B+树索引的关键字检索效率比较平均，不像B树那样波动幅度大，在有大量重复键值情况下，哈希索引的效率也是极低的，因为存在哈希碰撞问题
```

### 11. Mysql的锁

```
基于锁的属性分类: 共享锁，排他锁
基于锁的粒度分类: 行级锁(InnoDB),表级锁(InnoDB,MyISAM),页级锁(BDB引擎)，记录锁，间隙锁，临键锁
基于锁的状态分类: 意向共享锁，意向排他锁

共享锁(Share Lock):
	共享锁又称读锁，简称S锁;当一个事务为数据加上读锁之后，其他事务只能兑该数据加读锁，而不能对数据加写锁。共享锁的特性主要是为了支持并发的读取数据，读取数据的时候不支持修改，避免出现重复读的问题
	
排他锁(eXclusive Lock):
	排他锁又称写锁，简称X锁;当一个事务未数据加上写锁时，其他请求将不能再为数据加任何锁，知道该锁释放之后，其他事务才能对数据进行加锁，排他锁的目的是在数据修改时，不允许其他人同时修改，也不允许其他人读取，避免了出现脏数据和脏读的问题
	
表锁:
	表锁是指上锁的时候锁住的是整个表，当下一个事务访问该表的时候，必须等待前一个事务释放了锁才能对表进行访问;
	特点: 粒度大，加锁简单，容易冲突

行锁:
	行锁是指上锁的时候锁住的是表的某一行或多行记录，其他事务访问同一张表时，只有被锁住的记录不能访问，其他的记录可正常访问;
	特点: 粒度小，加锁比表锁麻烦，不容易冲突，相比表锁支持的并发度高
	
记录锁(Recore Lock):
	记录锁也属于行锁的一种，只不过记录锁的范围只是表中的某一条记录，记录锁是说事务在加锁后锁住的只是表的某一条记录
	精准条件命中，并且命中的条件字段是唯一索引
	加了记录锁之后数据可以避免数据在查询的时候背修改的重复读问题，也避免了在修改的事务未提交前被其他事务读取的脏读问题
	
页锁:
	页级锁是Mysql中锁定粒度介于行级锁和表级锁中间的一种锁，表级锁速度快，但冲突多，行级冲突少，但速度快，所以取了折衷的页级，一次锁定相邻的一组记录
	特点: 开销和加锁时间介于表锁和行锁之间，会出现死锁，锁定粒度介于表锁和行锁之间，并发度一般
	
间隙锁(Gap Lock):
	属于行锁的一种，间隙锁是在事务加锁后其锁住的是表记录的某一个区间，当表的相邻ID之间出现空隙则会形成一个区间，遵循左开右闭原则
	范围查询并且查询未命中记录，查询调价您必须命中索引，间隙锁只会出现在REPEATABLE_READ(重复读)的事务级别中
	触发条件: 房主幻读问题，事务并发的时候，如果没有间隙锁，就会发生幻读
	比如表里面的数据ID 为1，4，5，7，10，那么会形成以下几个间隙区间，(-n,1],(1,4],(4,7],(7,10],(10,+n)
	
临键锁(Next-Key Lock):
	也属于行锁的一种，并且是InnoDB的行锁默认算法，是记录锁和间隙锁的组合，临键锁会把查询出来的记录锁住，同时也会把该范围查询内的所有间隙空间也会锁住，再之它会把相邻的下一个区间也会锁住
	触发条件: 范围查询并命中，查询命中了索引
	结合记录锁和间隙锁的特性，临键锁避免了在范围查询时出现脏读，重复读，幻读问题，加了临键锁之后，在范围区间内数据不允许被修改和插入
	
如果事务A加锁成功之后就设置了一个状态告诉后面的人，已经有人堆表里的行加了一个排他锁了，不能对整个表加共享锁或排他锁了，那么后面需要对整个表加锁的人只需要获取这个状态就知道自己是不是可以对表加锁，避免了对整个所引树的每个节点扫描是否加锁，而这个状态就是意向锁
	意向共享锁:
		当一个事务视图对整个表进行加共享锁之前，首先需要获得这个表的意向共享锁
	意向排他锁:
		当一个事务视图对整个表进行加排他锁之前，首先需要获得这个表的意向排他锁
```

### 12. Mysql有哪几种数据存储引擎？有什么区别？

```
Mysql中通过show Engines指令可以看到所有支持的数据库存储引擎，最为常用的就是MyISAM和InnoDB两种
MyISAM和InnoDB的区别:
	1. 存储文件，MyISAM每个表有两个文件，MYD和MYISAM文件，MYD是数据文件，MYI是索引文件，而InnoDB每个表只有一个文件.idb
	2. InnoDB支持事务，支持行级锁，支持外键
	3. InnoDB支持XA事务
	4. InnoDB支持savePoints
```

### 13. Mysql执行计划

```
执行计划就是sql的执行查询的顺序，以及如何使用索引查询，返回的结果集的行数
1. id: 是一个有顺序的编号，是查询的顺序号，有几个select就显示几行。id的顺序是按select出现的顺序增长的，id列的值越大执行优先级越高越优先执行，id列的值相同从上往下执行，id列的值为null最后执行
2. selectType: 表示查询中每个select子查询的类型
3. table: 表示该语句查询的表
4. type: 优化sql的重要字段，也是我们判断sql性能和优化程度重要指标
	const: 通过索引一次命中，匹配一行数据(id = 1)
	system: 表中只有一行记录，相当于系统表
	eq_ref: 唯一性索引扫描，对于每个索引键，表中只有一条记录与之匹配
	ref: 非唯一性索引扫描，返回匹配某个值的所有
	range: 只检索给定范围的行，使用一个索引来选择行，一般用于between,<,>
	index: 只遍历索引树
	ALL: 表示全表扫描，这个类型的查询是性能最差的查询之一，那么基本就是随着表的数量增多，执行效率越慢
	
	执行效率:
		ALL<index<range<ref<eq_ref<const<system,最好避免ALL和index
		eq_ref,ref 都可能会回表
		
5. possible_keys: 可能命中的索引
6. key: 实际命中的索引
7. key_len: 命中索引长度
8. ref: 当使用索引列等值查询时，与索引列进行等值匹配的对象信息
9. rows: 命中的行数(扫描行数越少，执行计划越优)
10. filtered: 返回结果的行占需要读到的行的百分比，百分比越高，说明需要查询到数据越准确
11. Extra: 
	using filesort: 
	using index: 
	using temporary: 查询有使用临时表，一般出现于排序，分组和多表join的情况，查询效率不高
	using where: sql使用了where过滤，效率较高
```

### 14. Mysql主从同步原理

```
mysql主从同步的过程:
	三个线程: master(binlog dump thread),slave(I/O thread,SQL thread),Master一条线程和Slave中的两条线程
	1. 主节点binlog，主从复制的基础是主库记录数据库的所有变更记录到binlog，binlog是数据库服务器启动的时候，保存所有修改数据库结构或内容的一个文件
	2. 主节点log dump线程，当binlog有变动时，log dump线程读取其内容并发送给从节点
	3. 从节点I/O线程接收binlog内容，并将其写入到relay log文件中
	4. 从节点的SQL 线程读取relay log文件内容对数据更新进行重放，最终保证主从数据库的一致性
	注: 主从节点使用binlog文件+position偏移量来定位主从同步的位置，从节点会保存其已接收到的偏移量，如果从节点发生宕机重启，则会自动从position的位置发起同步
	由于mysql默认的复制方式是异步的，主库把日志发送给从库后不关心从库是否已经处理了，这样会产生一个问题就是假设主库挂了，从处理失败了，这时候从库升为主库后，日志就丢失了，由此产生两个概念:
	1. 全同步复制
		主库写入binlog后强制同步日志到从库，所有的从库都执行完成后才返回给客户端，但是很显然这个方式的性能会受到严重影响
	2. 半同步复制:
		和全同步不同的是，半同步复制的逻辑是，从库写入日志成功后返回ack确认给主库，主库受到至少一个从库的确认就认为写操作完成
```

### 15. Mysql数据库中，什么情况下设置了索引但无法使用

```
1. 没有符合最左前缀原则
2. 字段进行了隐私数据类型转化
3. 走索引没有全表扫描效率高
```

### 16. 存储拆分后，如何解决唯一主键

```
UUID: 简单，性能好，没有顺序，没有业务含义，存在泄漏mac地址的风险
数据库主键: 实现简单，单调递增，具有一定的业务可读性，强依赖db，存在性能瓶颈，存在暴露业务信息的风险
redis，mongodb,zk等中间件: 增加了系统的复杂度和稳定性
雪花算法
```

### 17. 海量数据下，如何快速查找一条记录？

```
1. 使用布隆过滤器，快速过滤不存在的记录
	使用Redis的bitmap结构来实现布隆过滤器
2. 在Redis中建立数据缓存
	以普通字符串的形式来存储，(userId -> user.json),以一个hash来存储一条记录(userId key -> username field -> userAge)。以一个整的hash来存储所有的数据，UserInfo -> field就用userId，value就用user.json。一个hash最多能支持2^32 - 1(40多个亿)个键值对
	缓存击穿: 对不存在的数据也建立key。这些key都是经过布隆过滤器过滤的，所以一般不会太多
	缓存过期: 将热点数据设置成永不过期，定期重建缓存，使用分布式锁重建缓存
3. 查询优化
	按槽位分配数据
	自己实现槽位计算，找到记录应该分配在哪台机器上，然后直接去目标机器上找
```

### 18. MyISAM和InnoDB的区别

```
MyISAM:
	不支持事务，每次查询都是原子的
	支持表级锁，即每次操作都是对整个表加锁
	存储表的总行数:
		一个MyISAM表有三个文件: 索引文件，表结构文件，数据文件;
		采用非聚簇索引，索引文件的数据域存储指向数据文件的指针，辅索引与主索引一致，但是辅索引不用保证			唯一性
InnoDB:
	支持ACID的事务，支持事务的四种隔离级别
	支持行级锁及外键约束，因此可以支持写并发
	不存储总行数
	一个InnoDB引擎存储在一个文件空间(共享表空间，表大小不受操作系统控制，一个表可能分布在多个文件里)，也有可能为多个(设置为独立表空间，表大小收操作系统文件大小限制，一般为2G)，受操作系统文件大小的限制
	主键索引采用聚簇索引(索引的数据域存储数据文件本身)，辅索引的数据域存储主键的值，因此从辅索引查找数据，需要先通过辅索引找到主键值，在访问辅索引;最好使用自增主键，防止插入数据时，为维持B+树结构，文件的大调整
```

### 19. Mysql中索引类型及对数据库性能的影响

```
普通索引: 允许被索引的数据包含重复的值
唯一索引: 可以保证数据记录的唯一性
主键: 是一种特殊的唯一索引，在一张表中只能定义一个主键索引，主键用于唯一标识一条记录，使用关键字Primary Key来创建
联合索引: 索引可以覆盖多个数据列，如像INDEX(columnA, columnB)索引
全文索引: 通过简历倒排索引，可以极大的提升检索效率，解决判断字段是否包含的问题，是目前搜索引擎使用的一种关键技术，可有通过 alter table table_name add fulltext(column);创建全文索引

优势:
	索引可以极大的提高数据的查询速度
	通过使用索引，可以在查询的过程中，使用优化隐藏器，提高系统的性能
劣势:
	但是会降低插入，删除，更新表的速度，因为在执行这些写操作时，还要操作索引文件
	索引需要占物理空间，除了数据表占数据空间之外，每一个索引还要占一定的物理空间，如果要建立聚簇索引，那么需要的空间就会更大，如果非聚簇索引很多，一旦聚簇索引改变，那么所有非聚簇索引都会跟着变
```

### 20. Mysql的锁

```
锁分类:
	行锁: 锁某行数据，锁粒度最小，并发度最高
	表锁: 锁整张表，锁粒度最大，并发度低
	间隙锁: 锁的是一个区间
	
	共享锁: 也就是读锁，其他事务只能读，不能写
	排他锁: 写锁，其他事务不能读，不能写
	
	乐观锁: 并不会真正的去锁某行记录，而是通过一个版本号来实现的
	悲观锁: 上面说的行锁，表锁都是悲观锁
	
	在事务隔离级别中，就需要利用锁来解决幻读
```

### 21. 事务的基本特性和隔离级别

```
事务: 表示多个数据操作组成一个完整的事务单元，这个事务内的所有数据操作要么同时成功，要么同时失败
事务的特性: ACID
	1. 原子性: 事务是不可分割的，要么完全成功，要么完全失败
	2. 一致性: 事务无论是完成还是失败，都必须保持事务内操作的一致性，当失败时，都要对前面的操作进行回滚，不管中途是否整个
	3. 隔离性: 当多个事务操作一个数据的时候，为防止数据损坏，需要将每个事务进行隔离，互相不干扰
	4. 持久性: 事务开始就不会终止，他的结果不受其他外在因素影响
	
事务的隔离级别:
	SHOW VARIABLES like 'transaction%'
	设置隔离级别: set transaction level xxx 设置下次事务的隔离级别
	set session transaction level xxx 设置当前会话的事务隔离级别
	set global transaction level xxx 设置全局事务隔离级别
	
Mysql当中有五种给级别:
	1. NONE: 不使用事务
	2. READ UNCOMMITED(未提交读): 允许脏读
	3. READ COMMITED(已提交读): 防止脏读，最常用的隔离级别，大多数数据库默认隔离级别
	4. REPEATABLE READ(可重复读): 防止脏读和不可重复读(Mysql默认隔离级别)
	5. SERIALIZABLE(串行): 事务串行，可以防止脏读，幻读，不可重复读
	
	五种隔离级别，级别越高，事务的完全性更高，但是，事务的并发性能也会降低
```

### 22. 如何实现分库分表

```
将原本单个数据库上的数据拆分到多个数据库，多张表中，实现数据切分，从而提升数据库操作性能。
分库分表可以分为两种方式: 垂直切分和水平切分

水平:将数据分散到多张表，涉及分区键
	分库: 每个库结构一样，数据不一样，没有交集，库多了可以缓解io和cpu压力
	分表: 每个表结构一样，数据不一样，没有交集，表数量减少可以提高sql执行效率，减轻cpu压力
	
垂直: 将字段拆分为多张表，需要一定的重构
	分库: 每个库结构，数据都不一样，所有库的并集为全量数据
	分表: 每个表结构，数据不一样，至少有一列交集，用于关联数据，所有表的并集为全量数据
```

### 23. 什么是MVCC

```
多版本并发控制: 读取数据时通过一种类似快照的方式将数据保存下来，这样读锁和写锁就不冲突了，不同的事务session会看到自己特定版本的数据，版本链
MVCC只在READ COMMITED 和 REPEATABLE READ两个隔离级别下工作，其他两个隔离级别和MVCC不兼容，因为REPEATABLE READ总是读取最新的数据行，而不是符合当前事务版本的数据行，而SERIALIZABLE则会对所有读取的行都加锁

聚簇索引记录中有两个必要的隐藏列:
	trx_id: 用来存储每次对某条聚簇索引记录进行修改的时候的事务ID
	roll_pointter: 每次对哪条聚簇索引记录有修改的时候，都会把老版本写入undo日志中，这个roll_pointer就是存了一个指针，它指向这条聚簇所以记录的上一个版本的位置，通过它来获得上一个版本的记录信息(注意插入操作的undo日志没有这个属性，因为它没有老版本)
	
已提交读和可重复读的区别在于它们生成ReadView的策略不同

开始事务时创建readview，readview维护当前活动的事务id，即未提交的事务id，排序生成一个数组访问数据，
获取数据中的数据id(获取的是事务id最大的记录)，对比readview:
如果在readview的左边(比readview都小)，可以访问(在左边意味着该事务已经提交)
如果在readview的右边(比readview都大)或者就在readview中，不可以访问，获取roll_pointer，取上一版本重新对比(在右边意味着，该事务在readview生成之后出现，在readview中意味着该事务还未提交)
已提交读隔离级别下的事务在每次查询的开始都会生成一个独立的readview，而可重复读隔离级别则在第一次读的时候生成一个readview，之后的读都复用之前的readview
这就是Mysql的MVCC，通过版本链，实现多版本，可并发读-写，写-读。通过readview生成策略的不同实现不同的隔离级别。
```

### 24. 什么是脏读，幻读，不可重复读？要怎么处

```
脏读: 在事务进行过程中，读到了其他事务未提交的数据
不可重复读: 在一个事务过程中，多次查询的结果不一致
幻读: 在一个事务过程中，用同样的操作查询数据，得到的记录数不同
处理的方式有很多种: 加锁，事务隔离，MVCC
加锁:
	1. 脏读: 在修改时加排他锁，直到事务提交才释放，读取时加共享锁，读完释放锁
	2. 不可重复读: 读数据时加共享锁，写数据时加排他锁
	3. 幻读: 加范围锁
```

### 25. 事务的基本特性和隔离级别

```
事务的基本特性ACID:
	原子性: 一个事务要么全部成功，要么全部失败
	一致性: 数据库总是从一个一致性的状态转换到另一个一致性的状态
	隔离性: 一个事务的修改在最终提交时，对其他事务是不可见的
	持久性: 事务一旦提交，所做的修改就会永久保存在数据库中
	
隔离性有4个隔离级别,分别是:
	1. read uncommit: 读未提交，可能会读到其他事务未提交的数据，也叫做脏读
	2. read commit: 读已提交，两次读取结果不一致，叫做不可重复读
	3. repeatable read: 可重复读，这是mysql的默认隔离级别，就是每次读取的结果都是一样的，但是有可能产生幻读(间隙锁解决幻读问题)
	4. serializable: 串行，一般是不会使用的，他会给每一行读取的数据加锁，会导致大量超时和锁竞争的问题
```

### 26. 索引的基本原理

```
索引是用来快速寻找那些具有特定值的记录，如果没有索引，一般来说执行查询时需要遍历整张表
索引的原理: 就是把无序的数据变成有序的查询
	1. 把创建了索引的列的内容进行排序
	2. 对排序结果生成倒排表
	3. 在倒排表内容上拼上数据地址链
	4. 在查询的时候，先拿到倒排表内容，再取出数据地址链，从而拿到具体数据
```

### 27. 索引设计的原则

```
查询更快，占用空间更小
	1. 适合索引的列是出现在where子句中的列，或者连接子句中指定的列
	2. 基数较小的表，索引效果较差，没有必要在此列建立索引
	3. 使用短索引，如果对长字符串列进行索引，应该指定一个前缀长度，这样能够节省大量索引空间，如果搜索词超过索引前缀长度，则使用索引排除不匹配的行，然后检查其余行是否可能匹配
	4. 不要过度索引，索引需要额外的磁盘空间，并降低写操作的性能。在修改表内容的时候，索引会进行更新甚至重构，索引列越多，这个时间就会越长，所以只保持需要的索引有利于查询即可
	5. 定义有外键的数据列一定要建立索引
	6. 更新频繁字段不适合创建索引
	7. 若是不能有效区分数据的列不适合做索引列(如性别，男女未知，最多也就三种，区分度太低)
	8. 尽量的扩展索引，不要新建索引，比如表中已经有a的索引，现在要加(a,b)的索引，那么只需要修改原来的索引即可
	9. 对于那些查询中很少涉及的列，重复值比较多的列不要建立索引
	10. 对于定义为text，image和bit的数据类型的列不要建立索引
```

### 28. 索引覆盖是什么

```
索引覆盖是一个Sql在执行时，可以利用索引来快速查找，并且此sql所要查询的字段在当前索引对应的字段中都包含了，那么就表示此sql走完索引就不用回表了，所需要的租店在当前索引的叶子节点上存在，可以直接作为结果返回了
```

### 29. Mysql分库分表？多大数据量需要分库分表？分库分表的方式和分片策略有哪些？分库分表后，Sql的执行流程是怎样的

```
分库分表:
	当表中的数据量较大时，整个查询效率降低，为了提升查询效率，就要将一个表中的数据分散到多个数据库的多个表中
	分库分表最常用的组件: Mycat/ShardingSphere
	数据分片有垂直分片和水平分片，垂直分片就是从业务角度讲不同的表拆分到不同的库中，能够解决数据库文件过大的问题，但是不能从根本上解决查询问题，水平拆分就是从数据角度将一个表中的数据拆分到不同的库或表中，这样可以从根本上解决数据量过大造成的查询效率低的问题
	有非常多的分片策略，比如取模，按时间，按枚举值
	阿里提供的开发手册中，建议: 一个表的数据量超过500w或者数据文件超过2G，就要考虑分库分表了
	
分库分表后的执行流程:
```

![Mysql执行流程](Mysql.assets/Mysql%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B.png)

### 30. 如何优化慢查询

```
慢查询的优化首先要搞明白慢的原因是什么，是查询条件没有命中索引，是load了不需要的数据列，还是数据量太大

慢sql优化主要从以下三个方面来:
	1. 首先优化语句，看看是否load了额外的数据，可能是查询了多余的行并且抛弃掉了，可能是加载了许多结果中并不需要的列，对语句进行分析以及重写
	2. 分析语句的执行计划，然后获得其使用索引的情况，之后修改语句或者修改索引，使得语句可以尽可能的命中索引
	3. 如果对语句的优化已经无法进行，可以考虑表中的数据量是否太大，如果是的话可以进行横向或者总想的分库分表
```

### 31. 最左前缀原则

```
当一个sql想要利用索引时，就一定要提供索引所对应字段中最左边的字段，也就是排在最前面的字段，比如针对a,b,c三个字段建立了一个联合索引，那么在写一个sql时就一定要提供a字段的条件，这样才能用到联合索引，这是由于在建立a，b，c三个字段的联合索引，底层的B+树是按照a，b，c三个字段从左往右去比较大小进行排序的，所以如果想要利用B+树进行快速查找得符合这个规则
```

### 32. 索引失效

七字口诀：模型数空运最快

-   **模**：模糊查询，like的模糊查询一%开头，索引失效

    ```sql
    SELECT * FROM `user` WHERE `name` LIKE '%模糊';
    ```

-   **型**：代表数据类型。类型错误，如字段类型为varchar，where条件用number，索引也会失效。

    ```sql
    SELECT * FROM `user` WHERE height= 180;
    -- height为varchar类型导致索引失效。
    ```

-   **数**：是函数的意思。对索引的字段使用内部函数，索引也会失效。这种情况下应该建立基于函数的索引。

    ```sql
    SELECT * FROM `user` WHERE DATE(create_time) = '2020-09-03';
    -- create_time字段设置索引，那就无法使用函数，否则索引失效。
    ```

-   **空**：是Null的意思。索引不存储[空值](https://www.zhihu.com/search?q=空值&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2251613556})，如果不限制索引列是not null，数据库会认为索引列有可能存在空值，所以不会按照索引进行计算。

    ```sql
    SELECT * FROM `user` WHERE address IS NULL;不走索引。
    
    SELECT * FROM `user` WHERE address IS NOT NULL;走索引。
    ```

-   **运**：是运算的意思。对索引列进行（+，-，*，/，!, !=, <>）等运算，会导致索引失效。

    ```sql
    SELECT * FROM `user` WHERE age - 1 = 20;
    ```

-   **最**：是最左原则。在复合索引中索引列的顺序至关重要。如果不是按照索引的最左列开始查找，则无法使用索引。

-   **快**：[全表](https://www.zhihu.com/search?q=全表&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2251613556})扫描更快的意思。如果数据库预计使用全表扫描要比使用索引快，则不使用索引。

### 33. 数据库三大范式













