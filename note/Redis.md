# Redis

## 启动redis

```bash
redis-server kconfig/redis.conf
```

## 查看日志文件

```
tail -f log-redis.log
```

## 检查是否启动

```bash
redis-cli -p 6379
redis-cli --raw      #中文
```

- redis-cli 跟上命令会将参数发送到本地redis 6379 的端口

## 关闭命令

```bash
redis-cli shutdown
```

## 查看redis进程

```bash
ps -ef | grep -i redis
```

## Redis基本操作

```bash
redis-cli -p 6379
127.0.0.1:6379[3]> DBSIZE
(integer) 0
127.0.0.1:6379[3]> set name cp
OK
127.0.0.1:6379[3]> DBSIZE
(integer) 1
127.0.0.1:6379[3]> get name
"cp"
127.0.0.1:6379[3]> keys *						查看所有的key
1) "name"
127.0.0.1:6379[3]> flushdb					清空当前数据库
OK
127.0.0.1:6379[3]> keys *
(empty array)
127.0.0.1:6379[3]> flushall					清空所有数据库
OK
```

- Redis是单线程的。Redis是很快的，官方表示，Redis是基于内存操作的，CPU不是redis的性能瓶颈，他的瓶颈是根据机器的内存和网络带宽。
- 6.0之后优化了IO用了多线程，不过执行命令还是单线程的。
- Redis是C语言写的，官方提供的数据为 100000+ 的QPS，完全不比同样是使用key-value的 memcache差！

## Redis为什么单线程的还那么快

1. 误区1:高性能的服务器一定是多线程的
2. 误区2:多线程（CPU上下文切换）一定比单线程效率高

核心：redis是将所有的数据全部放在内存中的，所以说用单线程去操作效率是最高的，多线程（CPU上下文会切换，耗时的操作！！！），对于内存系统来说，如果没有上下文切换效率是最高的！多次读写都是在一个CPU上，在内存情况下，这个是最佳方案。



## Redis 五大数据类型

### Redis-Key

```bash
127.0.0.1:6379> keys *
1) "name"
127.0.0.1:6379> set name cp
OK
127.0.0.1:6379> set age 1
OK
127.0.0.1:6379> EXISTS age						# 查看key是否存在
(integer) 1
127.0.0.1:6379> EXISTS age1
(integer) 0
127.0.0.1:6379> move age 1						# 移动key到数据库1中
(integer) 1
127.0.0.1:6379> keys *
1) "age"
2) "name"
127.0.0.1:6379> EXPIRE name 10				# 设置过期时间
(integer) 1
127.0.0.1:6379> ttl name							# 查看剩余时间
(integer) 8
127.0.0.1:6379> ttl name
(integer) 6
127.0.0.1:6379> type age							# 查看key的类型
string
127.0.0.1:6379> type name
string
```



### String(字符串)

```bash
127.0.0.1:6379> set key1 v1								# 设置值
OK
127.0.0.1:6379> get key1									# 获取值
"v1"
127.0.0.1:6379> EXISTS key1								# 判断key是否存在
(integer) 1
127.0.0.1:6379> append key1 "hello"				# 追加字符串，如果当前key不存在，就相当于setkey
(integer) 7
127.0.0.1:6379> get key1
"v1hello"
127.0.0.1:6379> strlen key1								# 获取字符串长度
(integer) 7
127.0.0.1:6379> append key1 ",cp"
(integer) 10
127.0.0.1:6379> strlen key1
(integer) 10
```



```bash
# 自增自减
127.0.0.1:6379> set views 0							# 初始浏览量
OK
127.0.0.1:6379> get views
"0"
127.0.0.1:6379> incr views							# 自增1
(integer) 1
127.0.0.1:6379> incr views
(integer) 2
127.0.0.1:6379> get views
"2"
127.0.0.1:6379> decr views							# 自减1
(integer) 1
127.0.0.1:6379> decr views
(integer) 0
127.0.0.1:6379> decr views
(integer) -1
127.0.0.1:6379> get views
"-1"
127.0.0.1:6379> incrby views 10				# 自增10
(integer) 9
127.0.0.1:6379> decrby views 10				# 自减10
(integer) -1

```



```bash
# 字符串范围 range
127.0.0.1:6379> set key1 "hello,chenpeng"			# 设置 key1 值
OK
127.0.0.1:6379> get key1
"hello,chenpeng"
127.0.0.1:6379> getrange key1 0 3							# 截取字符串[0，3]
"hell"
127.0.0.1:6379> getrange key1 0 -1						# 获取全部字符串 和 get key 一样
"hello,chenpeng"
```



```bash
# 替换
127.0.0.1:6379> set key2 abcdefg
OK
127.0.0.1:6379> get key2
"abcdefg"
127.0.0.1:6379> setrange key2 1 xx          # 替换指定位置开始的字符串
(integer) 7
127.0.0.1:6379> get key2
"axxdefg"
```



```bash
# setex (set with expire) 						# 设置过期时间
# setnx (set if not exist)						# 不存在再设置 (在分布式锁中常常使用！)

127.0.0.1:6379> setex key3 30 "hello"	# 设置key3值 30秒后过期
OK
127.0.0.1:6379> ttl key3
(integer) 26
127.0.0.1:6379> get key3
"hello"
127.0.0.1:6379> setnx mykey "redis"		# 如果mykey不存在，就创建
(integer) 1
127.0.0.1:6379> keys *
1) "key2"
2) "key3"
3) "mykey"
4) "key1"
127.0.0.1:6379> ttl key3
(integer) -2
127.0.0.1:6379> setnx mykey "MongoDb"	# 如果mykey存在，创建失败
(integer) 0
127.0.0.1:6379> get mykey
"redis"

```



```bash
mset
mget

127.0.0.1:6379> mset k1 v1 k2 v2 k3 v3 # 同时设置多个值
OK
127.0.0.1:6379> keys *
1) "k1"
2) "k3"
3) "k2"
127.0.0.1:6379> mget k1 k2 k3					# 同时获取多个值
1) "v1"
2) "v2"
3) "v3"
127.0.0.1:6379> msetnx k1 v1 k4 v4		# msetnx 是原子性的操作 要么一起成功，要么一起失败！
(integer) 0
127.0.0.1:6379> get k4
(nil)
```



```bash
# 对象
set user:1{name:zhangsan,age:1} # 设置一个user:1 对象 值为json字符来保存一个对象

# 这里的key是一个巧妙的设计： user:{id}:{filed} 如此设计在redis中是完全OK的！

127.0.0.1:6379> mset user:1:name zhangsan user:1:age 2
OK
127.0.0.1:6379> mget user:1:name user:1:age
1) "zhangsan"
2) "2"
```



```bash
getset # 先get再set
127.0.0.1:6379> getset db redis			# 如果不存在值，返回nil
(nil)
127.0.0.1:6379> get db
"redis"
127.0.0.1:6379> getset db mongodb		# 如果存在值，获取原来的值，并设置新的值
"redis"
127.0.0.1:6379> get db
"mongodb"
```

String类似的使用场景：value除了是我们的字符串还可以是我们的数字！

- 计数器
- 统计多单位的数量
- 粉丝数
- 对象缓存存储



### List（列表）

基本的数据类型

在redis里面，可以把list玩成：队列、栈、阻塞队列

所有的list命令都是l开头的

```bash
127.0.0.1:6379> lpush list one		# 将一个值或者多个值，插入到列表头部
(integer) 1
127.0.0.1:6379> lpush list two
(integer) 2
127.0.0.1:6379> lpush list three
(integer) 3
127.0.0.1:6379> lrange list 0 -1	# 获取list值
1) "three"
2) "two"
3) "one"
127.0.0.1:6379> lrange list 0 1		# 通过区间获取具体的值
1) "three"
2) "two"
127.0.0.1:6379> rpush list right	# 将一个值或者多个值，插入到列表尾部
(integer) 4
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "two"
3) "one"
4) "right"
127.0.0.1:6379> lrange list 0 1
1) "three"
2) "two"
```



```bash
lpop
rpop

127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "two"
3) "one"
4) "right"
127.0.0.1:6379> lpop list		# 移除list的第一个元素
"three"
127.0.0.1:6379> rpop list		# 移除list的最后一个元素
"right"
127.0.0.1:6379> lrange list 0 -1
1) "two"
2) "one"
```



```bash
lindex

127.0.0.1:6379> lrange list 0 -1
1) "two"
2) "one"
127.0.0.1:6379> lindex list 1		# 通过下标获得list中的某一个值
"one"
127.0.0.1:6379> lindex list 0
"two"
```



```bash
llen

127.0.0.1:6379> lpush list one
(integer) 1
127.0.0.1:6379> lpush list two
(integer) 2
127.0.0.1:6379> lpush list three
(integer) 3
127.0.0.1:6379> llen list		# 返回列表的长度
(integer) 3
```



```bash
移除指定的值

127.0.0.1:6379> lpush list one
(integer) 1
127.0.0.1:6379> lpush list two
(integer) 2
127.0.0.1:6379> lpush list three
(integer) 3
127.0.0.1:6379> llen list
(integer) 3
127.0.0.1:6379> lpush list three
(integer) 4
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "three"
3) "two"
4) "one"
127.0.0.1:6379> lrem list 1 one		# 移除list集合中指定个数的value，精确匹配
(integer) 1
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "three"
3) "two"
127.0.0.1:6379> lrem list 2 three
(integer) 2
127.0.0.1:6379> lrange list 0 -1
1) "two"

```



```bash
trim 修剪 list 截断

127.0.0.1:6379> rpush mylist "hello"
(integer) 1
127.0.0.1:6379> rpush mylist "hello1"
(integer) 2
127.0.0.1:6379> rpush mylist "hello2"
(integer) 3
127.0.0.1:6379> rpush mylist "hello3"
(integer) 4
127.0.0.1:6379> ltrim mylist 1 2		# 通过下标截取指定的长度,这个list已经被改变了，截断了只剩下的元素
OK
127.0.0.1:6379> lrange mylist 0 -1
1) "hello1"
2) "hello2"
```



```bash
rpoplpush # 移除列表的最后一个元素，并且将它移动到新的列表中

127.0.0.1:6379> rpush mylist "hello"
(integer) 1
127.0.0.1:6379> rpush mylist "hello1"
(integer) 2
127.0.0.1:6379> rpush mylist "hello2"
(integer) 3
127.0.0.1:6379> rpoplpush mylist myotherlist		# 移除列表的最后一个元素，并且将它移动到新的列表中
"hello2"
127.0.0.1:6379> lrange mylist 0 -1	# 查看原来的列表
1) "hello"
2) "hello1"
127.0.0.1:6379> lrange myotherlist 0 -1		# 查看目标列表
1) "hello2"
```



```bash
lset # 将列表中指定下标的值替换为另一个值，更新操作
127.0.0.1:6379> exists list		# 判断这个列表是否存在
(integer) 0
127.0.0.1:6379> lset list 0 item	# 如果不存在列表我们去更新就会报错
(error) ERR no such key
127.0.0.1:6379> lpush list value
(integer) 1
127.0.0.1:6379> lrange list 0 0
1) "value"
127.0.0.1:6379> lset list 0 item	# 如果存在，更新当前下标的值
OK
127.0.0.1:6379> lrange list 0 0
1) "item"
127.0.0.1:6379> lset list 1 other	# 如果不存在，就会报错
(error) ERR index out of range
```



```bash
linsert # 将某个具体的value插入到列表中某个元素的前面或者后面

127.0.0.1:6379> rpush mylist hello
(integer) 1
127.0.0.1:6379> rpush mylist world
(integer) 2
127.0.0.1:6379> linsert mylist before world other
(integer) 3
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "other"
3) "world"
127.0.0.1:6379> linsert mylist after world new 
(integer) 4
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "other"
3) "world"
4) "new"
```

#### 小结

- 实际上是一个链表
- 如果key不存在，创建新的链表
- 如果key存在，新增内容
- 如果移除所有值，空链表，代表不存在
- 在两边插入或者改动值，效率最高。中间元素，相对效率会低。

消息排队 消息队列 （lpush rpop）、栈（lpush lpop）



### set（集合）

set中的值不能重复。

```bash
###############################################################
127.0.0.1:6379> sadd myset hello	# set集合中添加值
(integer) 1
127.0.0.1:6379> sadd myset chen
(integer) 1
127.0.0.1:6379> sadd myset peng
(integer) 1
127.0.0.1:6379> smembers myset	# 查看指定set的所有值
1) "hello"
2) "peng"
3) "chen"
127.0.0.1:6379> sismember myset hello	# 判断某一个值是否在set集合中
(integer) 1
127.0.0.1:6379> sismember myset peng
(integer) 1
127.0.0.1:6379> sismember myset world
(integer) 0
###############################################################
127.0.0.1:6379> scard myset	# 获取set集合中的内容元素个数
(integer) 3

###############################################################
rem

127.0.0.1:6379> srem myset hello	# 移除set集合中的指定元素
(integer) 1
127.0.0.1:6379> scard myset
(integer) 2
127.0.0.1:6379> smembers myset
1) "peng"
2) "chen"
###############################################################
set 无序不重复集合，抽随机

127.0.0.1:6379> srandmember myset		# 随机抽选一个元素
"world"
127.0.0.1:6379> srandmember myset
"peng"
127.0.0.1:6379> srandmember myset
"world"
127.0.0.1:6379> srandmember myset
"chen"
127.0.0.1:6379> srandmember myset 2	#随机抽选出指定个数的元素
1) "peng"
2) "world"
###############################################################
删除定的key，随机删除key

127.0.0.1:6379> smembers myset
1) "peng"
2) "chen"
3) "world"
127.0.0.1:6379> spop myset		# 随机删除一些set集合中的元素
"world"
127.0.0.1:6379> spop myset
"peng"
127.0.0.1:6379> smembers myset
1) "chen"

###############################################################
将一个指定的值，移动到另外一个set中

127.0.0.1:6379> sadd myset hello
(integer) 1
127.0.0.1:6379> sadd myset world
(integer) 1
127.0.0.1:6379> sadd myset chenpeng
(integer) 1
127.0.0.1:6379> sadd myset2 set2
(integer) 1
127.0.0.1:6379> smove myset myset2 hello
(integer) 1
127.0.0.1:6379> smembers myset2
1) "hello"
2) "set2"
127.0.0.1:6379> smembers myset
1) "chenpeng"
2) "world"

###############################################################
共同关注（交集）
数字集合类：
- 差集
- 交集
- 并集

127.0.0.1:6379> sadd key1 a
(integer) 1
127.0.0.1:6379> sadd key1 b
(integer) 1
127.0.0.1:6379> sadd key1 c
(integer) 1
127.0.0.1:6379> sadd key2 c
(integer) 1
127.0.0.1:6379> sadd key2 d
(integer) 1
127.0.0.1:6379> sadd key2 e
(integer) 1
127.0.0.1:6379> sdiff key1 key2		# 差集
1) "b"
2) "a"
127.0.0.1:6379> sinter key1 key2	# 交集		共同好友
1) "c"
127.0.0.1:6379> sunion key1 key2	# 并集
1) "b"
2) "a"
3) "c"
4) "d"
5) "e"

微博，A用户将所有关注的人放在一个set集合中，将它的粉丝也放在一个集合中
共同关注、共同爱好、二度好友、推荐好友
```



### Hash（哈希）

Map集合，key-map 这个值是一个map集合!	本质和string类型没有太大的区别，还是一个简单的key-value

set myhash field 111

```bash
127.0.0.1:6379> hset myhash field1 chenpeng	# set一个具体 key-value
(integer) 1
127.0.0.1:6379> hget myhash field1	# 获取一个字段值
"chenpeng"
127.0.0.1:6379> hmset myhash field1 hello field2 world	# set多个 key-value
OK
127.0.0.1:6379> hmget myhash field1 field2	# 获取多个字段值
1) "hello"
2) "world"
127.0.0.1:6379> hgetall myhash	# 获取全部的数据
1) "field1"
2) "hello"
3) "field2"
4) "world"
127.0.0.1:6379> hdel myhash field1 #删除hash指定key字段！对应的value也就消失了！
(integer) 1
127.0.0.1:6379> hgetall myhash
1) "field2"
2) "world"
###############################################################
hlen
127.0.0.1:6379> hmset myhash field1 hello field2 world
OK
127.0.0.1:6379> hgetall myhash
1) "field2"
2) "world"
3) "field1"
4) "hello"
127.0.0.1:6379> hlen myhash	# 获取hash表的字段数量
(integer) 2

###############################################################
127.0.0.1:6379> hexists myhash field1	# 判断hash中指定字段是否存在
(integer) 1
127.0.0.1:6379> hexists myhash field3
(integer) 0
###############################################################
# 只获得所有的field
127.0.0.1:6379> hkeys myhash
1) "field2"
2) "field1"
# 只获得所有的value
127.0.0.1:6379> hvals myhash
1) "world"
2) "hello"
###############################################################
127.0.0.1:6379> hset myhash field3 5
(integer) 1
127.0.0.1:6379> hincrby myhash field3 1			# 自增1
(integer) 6
127.0.0.1:6379> hincrby myhash field3 -1	# 自减1 可以用hdecrby
(integer) 5
127.0.0.1:6379> hsetnx myhash field4 hello	# 如果不存在则可以设置
(integer) 1
127.0.0.1:6379> hsetnx myhash field4 world	# 如果存在则不可以设置
(integer) 0
```

hash变更的数据 user name age，尤其是用户信息之类的，经常变动的信息！

hash更适合于对象的存储，string更适合字符串的存储

### Zset（有序集合）

在set基础上加了一个值，set k1 v1 zset k1 score v1

```bash
###############################################################
127.0.0.1:6379> zadd myset 1 one	# 添加一个值
(integer) 1
127.0.0.1:6379> zadd myset 2 two 3 three	# 添加多个值
(integer) 2
127.0.0.1:6379> zrange myset 0 -1
1) "one"
2) "two"
3) "three"
###############################################################
排序如何实现
127.0.0.1:6379> zadd salary 2500 xiaohong
(integer) 1
127.0.0.1:6379> zadd salary 5000 zhangsan
(integer) 1
127.0.0.1:6379> zadd salary 500 kuangshen
(integer) 1
127.0.0.1:6379> zrangebyscore salary -inf +inf	# 显示全部的用户 从小到大
1) "kuangshen"
2) "xiaohong"
3) "zhangsan"

127.0.0.1:6379> zrevrange salary 0 -1		# 从大到小排序
1) "zhangsan"
2) "kuangshen"
127.0.0.1:6379> 

127.0.0.1:6379> zrangebyscore salary -inf +inf withscores	# 显示全部的用户并且附带薪水
1) "kuangshen"
2) "500"
3) "xiaohong"
4) "2500"
5) "zhangsan"
6) "5000"
127.0.0.1:6379> zrangebyscore salary -inf 2500 withscores	# 显示从小到大并且小雨2500的用户 并显示薪水
1) "kuangshen"
2) "500"
3) "xiaohong"
4) "2500"
###############################################################
127.0.0.1:6379> zrange salary 0 -1
1) "kuangshen"
2) "xiaohong"
3) "zhangsan"
127.0.0.1:6379> zrem salary xiaohong	# 移除有序集合中的指定元素
(integer) 1
127.0.0.1:6379> zrange salary 0 -1
1) "kuangshen"
2) "zhangsan"
127.0.0.1:6379> zcard salary	# 获取有序集合中的个数
(integer) 2
###############################################################
127.0.0.1:6379> zadd myset 1 hello
(integer) 1
127.0.0.1:6379> zadd myset 2 world 3 kuangshen
(integer) 2
127.0.0.1:6379> zcount myset 1 3	# 获取指定区间的成员数量
(integer) 3
127.0.0.1:6379> zcount myset 1 2
(integer) 2
###############################################################
```

案例思路：set 排序 存储班级成绩表，工资表排序

普通消息1、 重要消息2 	带权重进行判断

排行榜应用实现，取top N

## 三种特殊数据类型

### geospatial地理位置

朋友的定位，附近的人，打车距离计算？

redis的geo在redis3.2 就推出了！这个功能可以推算地理位置的信息，两地之间的距离，方圆几里的人！

只有6个命令

- GEOADD
- GEODIST
- GEOHASH
- GEOPOS
- GEORADIUS
- GEORADIUSBYMEMBER

```bash
# geoadd 添加地理位置
# 规则：两级无法直接添加，我们一般会下载城市数据，直接通过java程序一次性倒入！
# 参数 key 值（纬度、经度、名称）
127.0.0.1:6379> geoadd china:city 116.40 39.90 beijing
(integer) 1
127.0.0.1:6379> geoadd china:city 121.47 31.23 shanghai
(integer) 1
127.0.0.1:6379> geoadd china:city 106.50 29.53 chongqin 114.05 22.52 shengzhen
(integer) 2
127.0.0.1:6379> geoadd china:city 120.16 30.24 hangzhou 108.96 34.26 xian
(integer) 2
###############################################################
# geopos
# 获得当前定位：一定是一个坐标值
# 获取指定的城市的经纬度
127.0.0.1:6379> geopos china:city beijing
1) 1) "116.39999896287918091"
   2) "39.90000009167092543"
127.0.0.1:6379> geopos china:city beijing xian
1) 1) "116.39999896287918091"
   2) "39.90000009167092543"
2) 1) "108.96000176668167114"
   2) "34.25999964418929977"
###############################################################
# geodist
# 两人之间的距离
#	单位
#	m 表示 米
# km 表示 千米
# mi 表示 英里
# ft 表示 英尺
127.0.0.1:6379> geodist china:city beijing shanghai
"1067378.7564"
127.0.0.1:6379> geodist china:city beijing shanghai km
"1067.3788"
127.0.0.1:6379> geodist china:city beijing xian km
"910.0565"
127.0.0.1:6379> geodist china:city beijing chongqin km
"1464.0708"
###############################################################
# 附近的人？（获得所有附近的人的地址，定位！）通过半径来查询
# 获得指定数量的人
# georadius

# 以110，30这个经纬度为中心，寻找方圆1000km内的城市
127.0.0.1:6379> georadius china:city 110 30 1000 km
1) "chongqin"
2) "xian"
3) "shengzhen"
4) "hangzhou"
127.0.0.1:6379> georadius china:city 110 30 500 km
1) "chongqin"
2) "xian"
# 显示到中间距离的位置
127.0.0.1:6379> georadius china:city 110 30 500 km withdist
1) 1) "chongqin"
   2) "341.9374"
2) 1) "xian"
   2) "483.8340"
# 显示指定数量的结果  
127.0.0.1:6379> georadius china:city 110 30 500 km withdist withcoord count 1
1) 1) "chongqin"
   2) "341.9374"
   3) 1) "106.49999767541885376"
      2) "29.52999957900659211"


###############################################################
# 找出位于指定元素周围的其他元素
# georadiusbymember
127.0.0.1:6379> georadiusbymember china:city beijing 1000 km
1) "beijing"
2) "xian"

###############################################################
# geohash
# 返回一个或多个位置元素的geohash表示
# 该命令将返回11个字符的geohash字符串
# 将二维的经纬度转换为一维的字符串。
127.0.0.1:6379> geohash china:city beijing chongqin
1) "wx4fbxxfke0"
2) "wm5xzrybty0"

###############################################################
# geo底层的实现原理其实就是Zset！我们可以使用Zset命令来操作geo！
127.0.0.1:6379> zrange china:city 0 -1	# 查看地图中全部的元素
1) "chongqin"
2) "xian"
3) "shengzhen"
4) "hangzhou"
5) "shanghai"
6) "beijing"
127.0.0.1:6379> zrem china:city beijing	# 移除指定元素
(integer) 1
127.0.0.1:6379> zrange china:city 0 -1
1) "chongqin"
2) "xian"
3) "shengzhen"
4) "hangzhou"
5) "shanghai"
```



### Hyperloglog

什么是基数？

基数（不重复的元素），可以接受误差

Redis hyperloglog 基数统计的算法！

优点：占用的内存是固定，2^64不同的元素的技术，只需要用12kb内存！如果要从内存角度来比较的话 首选hyperloglog

**网页的UV（一盒人访问一个网站多次，但是还是算作一个人！）**

传统的方式，set保存用户的id，然后就可以统计set中的元素数量作为标准判断！

这个方式如果保存大量的用户id，就会比较麻烦！我们的目的就是为了计数，而不是保存用户id。

0.81%错误率！统计UV任务，可以忽略不计！ 

```bash
127.0.0.1:6379> pfadd mykey a b c d e f g h i j	# 创建第一组元素mykey
(integer) 1
127.0.0.1:6379> pfcount mykey	# 统计 mykey 元素的基数数量
(integer) 10
127.0.0.1:6379> pfadd mykey2 i j z x c v b n m # 创建第二组元素mykey2
(integer) 1
127.0.0.1:6379> pfcount mykey2
(integer) 9
127.0.0.1:6379> pfmerge mykey3 mykey mykey2 #合并两组mykey mykey2 =》mykey3 并集
OK
127.0.0.1:6379> pfcount mykey3	# 看并集的数量！
(integer) 15
```

如果允许容错，那么一定可以使用hyperloglog

如果不允许容错，就使用set或者自己的数据类型即可



### Bitmaps

位存储

统计疫情感染人数 0000011100101

统计用户信息，活跃，不活跃。登录，未登录。打卡，365打卡

两个状态的，都可以使用bitmaps。

bitmaps位图，数据结构！都是操作二进制位来进行记录，就只有0和1两个状态

365天=365bit 1字节=8bit 46个字节左右

```bash
# 使用bitmap来记录 周一到周日的打卡
127.0.0.1:6379> setbit sign 0 1
(integer) 0
127.0.0.1:6379> setbit sign 1 0
(integer) 0
127.0.0.1:6379> setbit sign 2 0
(integer) 0
127.0.0.1:6379> setbit sign 3 1
(integer) 0
127.0.0.1:6379> setbit sign 4 1
(integer) 0
127.0.0.1:6379> setbit sign 5 0
(integer) 0
127.0.0.1:6379> setbit sign 6 0
(integer) 0

# 查看某一天是否打卡
127.0.0.1:6379> getbit sign 3
(integer) 1
127.0.0.1:6379> getbit sign 6
(integer) 0

# 统计操作
127.0.0.1:6379> bitcount sign
(integer) 3
```



## 事务

Redis事务本质：一组命令的集合！一个事务中的所有命令都会被序列化，在事务执行过程中，会按照顺序执行！

一次性、顺序性、排他性！

```
----- 队列 set set set 执行 -----
```

**Redis事务没有隔离级别的概念**

所有的命令在事务中，并没有直接被执行！只是发起执行命令的时候才会执行！Exec

Redis单条命令式保存原子性的，但是事务不保证原子性！

redis的事务：

- 开启事务（multi）
- 命令入队（。。。。）
- 执行事务（exec）

### 正常执行事务：

```bash
127.0.0.1:6379> multi					# 开启事务
OK
127.0.0.1:6379> set k1 v1 
QUEUED
127.0.0.1:6379> set k2 v2 
QUEUED
127.0.0.1:6379> get k2 
QUEUED
127.0.0.1:6379> set k3 v3
QUEUED
127.0.0.1:6379> exec					# 执行事务
1) OK
2) OK
3) "v2"
4) OK

```

### 放弃事务：

```bash
127.0.0.1:6379> multi				# 开启事务
OK
127.0.0.1:6379> set k1 v1
QUEUED
127.0.0.1:6379> set k2 v2
QUEUED
127.0.0.1:6379> set k4 v4 
QUEUED
127.0.0.1:6379> discard			# 取消事务
OK
127.0.0.1:6379> get k4			# 事务队列中命令都不会被执行
(nil)
```

### 编译型异常（代码有问题！命令有错！），事务中所有的命令都不会被执行

```bash
127.0.0.1:6379> multi
OK
127.0.0.1:6379> set k1 v1
QUEUED
127.0.0.1:6379> set k2 v2
QUEUED
127.0.0.1:6379> set k3 v3
QUEUED
127.0.0.1:6379> getset k3		# 错误的命令
(error) ERR wrong number of arguments for 'getset' command
127.0.0.1:6379> set k4 v4
QUEUED
127.0.0.1:6379> set k5 v5
QUEUED
127.0.0.1:6379> exec		# 执行事务报错
(error) EXECABORT Transaction discarded because of previous errors.
127.0.0.1:6379> get k5		# 所有的命令都不会被执行
(nil)
```

### 运行时异常（1/0），如果事务队列中存在语法性，那么执行命令的时候没其他命令是可以正常执行的，错误命令抛出异常。

```bash
127.0.0.1:6379> set k1 v1
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> incr k1	# 会执行的时候失败！
QUEUED
127.0.0.1:6379> set k2 v2
QUEUED
127.0.0.1:6379> set k3 v3
QUEUED
127.0.0.1:6379> get k3
QUEUED
127.0.0.1:6379> exec
1) (error) ERR value is not an integer or out of range	# 虽然第一条命令报错了，但是依旧正常执行成功了！
2) OK
3) OK
4) "v3"
127.0.0.1:6379> get k2
"v2"
127.0.0.1:6379> get k3
"v3"
```

### 监控（watch）

#### 悲观锁

- 认为什么时候都会出问题，无论做什么都会被加锁

#### 乐观锁

- 认为什么时候都不会出问题，所以不会上锁！更新数据的时候去判断一下，再次期间是否有人修改过这个数据
- 获取version
- 更新的时候比较version

#### Redis监视测试

```bash
# 正常执行成功
127.0.0.1:6379> set money 100
OK
127.0.0.1:6379> set out 0
OK
127.0.0.1:6379> watch money 	# 监视 money 对象
OK
127.0.0.1:6379> multi					# 事务正常结束，数据期间没有发生变动，这个时候就正常执行成功
OK
127.0.0.1:6379> decrby money 20
QUEUED
127.0.0.1:6379> incrby out 20
QUEUED
127.0.0.1:6379> exec
1) (integer) 80
2) (integer) 20

# 测试多线程修改值，使用watch可以当作redis的乐观锁操作
127.0.0.1:6379> watch money
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> decrby money 10
QUEUED
127.0.0.1:6379> incrby out 10
QUEUED
127.0.0.1:6379> exec		# 执行之前，另外一个线程，修改了我们的值，这个时候，就会导致事务执行失败
(nil)


# 如果修改失败，获取最新的值就好
127.0.0.1:6379> unwatch			# 如果发现事务执行失败，就先解锁
OK
127.0.0.1:6379> watch money	# 获取最新的值，再次监视，select version
OK
127.0.0.1:6379> multi 
OK
127.0.0.1:6379> decrby money 10
QUEUED
127.0.0.1:6379> incrby money 10
QUEUED
127.0.0.1:6379> exec	# 比对监视的值是否发生了变化，如果没有变化，那么可以执行成功，如果变了就执行失败
1) (integer) 990
2) (integer) 1000
```

## Jedis

什么是jedis 是redis官方推荐的java连接开发工具。使用java操作redis中间件！如果你要使用java操作redis，那么一定要对jedis十分熟悉。

所有的API 就是和上面一模一样

## Springboot 整合 redis

在springboot 2.x之后，原来使用的jedis换成了lettuce

jedis：采用的直连，多个线程操作的话，是不安全的，如果想要避免不安全的，使用jedis pool连接池，更像BIO模式

lettuce：采用netty，实例可以在多个线程中进行共享，不存在线程不安全的情况！可以减少线程数据，更像NIO模式

### 源码分析

```java
public class RedisAutoConfiguration {
    public RedisAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean(
        name = {"redisTemplate"}
    )
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
      // 默认的 RedisTemplate 没有过多的设置，redis对象都是需要序列化的
      // 两个泛型都是Object，后面使用需要强制转换<String,Object>
      RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
  // 由于String是redis中最常使用的类型，所以被单独提出来了一个bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
```

### 整合测试

1. 导入依赖

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```

2. 配置连接

```properties
#配置redis
spring.redis.host=127.0.0.1
spring.redis.port=6379
```

3. 测试

```java
package com.lianwei;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisSpringbootApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        // redisTemplate    操作不同的数据类型，api和指令是一样的
        // opsForValue 操作字符串 类似String
        // opsForList 操作List 类似List
        // opsForSet 操作Set 类似Set
        // opsForHash 操作Hash 类似Hash

        // 除了基本的操作，常用的方法都可以直接通过redisTemplate操作。比如事务，和基本的增删改查

        // 获取redis连接对象
//        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
//        connection.flushDb();
//        connection.flushAll();

        redisTemplate.opsForValue().set("key","cp");
     System.out.println(redisTemplate.opsForValue().get("key"));
    }
}
```

### 重写配置文件 （序列化）

新建一个RedisTemplate.class

```java
package com.lianwei.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  @SuppressWarnings("all")
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
      RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
      template.setConnectionFactory(factory);
      Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
      ObjectMapper om = new ObjectMapper();
      om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
      om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
      jackson2JsonRedisSerializer.setObjectMapper(om);
      StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

      // key采用String的序列化方式
      template.setKeySerializer(stringRedisSerializer);
      // hash的key也采用String的序列化方式
      template.setHashKeySerializer(stringRedisSerializer);
      // value序列化方式采用jackson
      template.setValueSerializer(jackson2JsonRedisSerializer);
      // hash的value序列化方式采用jackson
      template.setHashValueSerializer(jackson2JsonRedisSerializer);
      template.afterPropertiesSet();

      return template;
  }
}
```



### 封装一个RedisUtil 工具类，简化代码

```java
package com.lianwei.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public final class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // =============================common============================
    /**
     * 指定缓存失效时间
     * @param key  键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }


    // ============================String=============================

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 普通缓存放入并设置时间
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 递增
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * 递减
     * @param key   键
     * @param delta 要减少几(小于0)
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    // ================================Map=================================

    /**
     * HashGet
     * @param key  键 不能为null
     * @param item 项 不能为null
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet 并设置时间
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }


    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }


    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }


    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取set缓存的长度
     *
     * @param key 键
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */

    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取list缓存的长度
     *
     * @param key 键
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */

    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */

    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
```

## Redis持久化

### RDB（Redis Database）

redis会单独创建（fork）一个子进程来进行持久化，会先将数据写入一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件，整个过程中，主进程是不进行任何IO操作的，这就确保了极高的性能。如果需要进行大规模数据的恢复，且对于数据恢复的完整性不是非常敏感，那RDB方式要比AOF方式更加高效。RDB缺点是最后一次持久化后的数据可能丢失，我们默认的就是RDB，一般情况下不需要修改这个配置。

#### 触发机制

1. save的规则满足的情况下，会自动触发rdb规则
2. 执行flushall命令，也会触发rdb规则
3. 退出redis，也会产生rdb文件

备份就自动生成一个dump.rdb

#### 如何恢复rdb文件

1. 只需要将rdb文件放在redis启动目录就可以，redis启动的时候就会自动检查dump.rdb恢复其中数据。
2. 查看需要存在的位置。

#### 优缺点

**优点：**

- 适合大规模的数据恢复
- 对数据的完整性要求不高

**缺点：**

- 需要一定的时间间隔进程操作！如果redis意外宕机了，这个最后一次修改数据就没有了
- fork进程时候，会占用一定的内容空间

### AOF（Append Only File）

将所有命令都记录下来，恢复的时候就把这个文件全部执行一遍。

以日志的形式来记录每个写操作，将redis执行过的所有指令记录下来（读操作不记录），只允许追加文件不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，redis重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作。

**AOF保存的是appendonly.aof文件**

默认config文件中是不开启的，将appendonly 后的no改成yes 就开启了aof。

如果aof文件有错误，这时候redis启动不了。需要修复这个aof文件。

redis提供了一个工具`redis-check-aof --fix`

如果文件正常，重启就可以恢复了。

#### 优缺点

**优点：**

1. 每一次修改都同步，文件的完整性会更加好
2. 每秒同步一次，可能会丢失一秒的数据
3. 从不同步，效率是最高的

**缺点：**

1. 相对于数据文件来说，aof远远大于rdb，修复的速度比rdb慢！
2. aof运行效率比rdb慢，所以redis默认的rdb持久化

## Redis发布订阅

redis发布订阅（pus/sub）是一种消息通信模式：发送者（pub）发送消息，订阅者（sub）接收消息。微信、微博、关注系统。

Redis客户端可以订阅任意数量的频道。

订阅/发布消息图：

第一个：消息发送者，第二个：频道，第三个：消息订阅者

```bash
# 订阅者
127.0.0.1:6379> subscribe cp 			# 订阅一个频道
subscribe
cp
1
message
cp
1
message
cp
nihao

```

```bash
127.0.0.1:6379> publish cp "1" 
(integer) 1
127.0.0.1:6379> publish cp "1" 
(integer) 1
127.0.0.1:6379> publish cp "nihao" 
(integer) 1
```



## Redis主从复制

### 概念

主从复制，是指将一台redis服务器的数据，复制到其他的reids服务器。前者称为主节点（master/leader），后者称为从节点（slave/follower）；

数据的复制是单向的，只能由主节点到从节点。Master以写为主，Slave已读为主

默认情况下，每台reids服务器都是主节点；且一个主节点可以有多个从节点（或者没有从节点），但是一个从节点只能有一个主节点。

### 主从复制的作用主要包括：

1. 数据冗余：主从复制实现数据的热备份，是持久化之外的一种数据冗余方式
2. 故障恢复：当主节点出现问题时，可以由从节点提供服务，实现快速的故障恢复；实际上是一种服务的冗余。
3. 负载均衡：在主从复制的基础上，配合读写分离，可以由主节点提供写服务，由从节点提供读服务（即写redis数据时应用连接主节点，读redis数据时应用连接从节点），分担服务器负载；尤其是在写少读多的场景下，通过多个从节点分担读负载，可以大大提高redis服务器的并发量。
4. 高可用基石：除了上述作用以外，主从复制还是哨兵和集群能够实施的基础，硬刺说主从复制时redis高可用的基础。

主从复制，读写分离！80%的情况下都是在进行读操作！减缓服务器的压力！架构中经常使用！

### 环境配置

只配置从库，不用配置主库！

```bash
127.0.0.1:6379> info replication		# 查看当前库的信息
# Replication
role:master			# 角色 master
connected_slaves:0		# 没有从机
master_replid:358b217bde8b64c3067170a0effbacae31fba9ee
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```

复制三个配置文件，然后修改对应的信息

1. 端口号
2. pid名字
3. log文件名字
4. dump.rdb文件名字

### 一主二从

默认情况下，每台Redis服务器都是主节点；一般情况下只用配置从机就好。

```bash
 SLAVEOF 127.0.0.1 6379			#SLAVEOF host 6379
```

真实的主从配置应该是在配置文件中修改，这样就是永久的配置。replication

主机可以写，从机不能写只能读！主机中的所有信息和数据，都会自动被从机保存

测试：主机断开连接，从机依旧连接到主机。但是没有写操作，这个时候，主机如果回来了，从机依旧可以直接获取到信息。

如果是使用命令行来配置主从，这个时候如果重启了，就会变回主机。只要变为从机，立马就会从主机中获取值。

### 复制原理

slave 启动成功连接到master后会发送一个sync同步命令

Master接到命令，启动后台的存盘进程，同时收集所有接收到的用于修改数据集命令，在后台进程执行完毕后，master将传送整个数据文件到slave，并完成一次完全同步。

全量复制：slave服务在接收到数据库文件数据后，将其存盘并加载到内存中。

增量复制：Master继续将新的所有收集到的修改命令一次传给slave，完成同步

但是只要重新连接master，一次完全同步（全量复制）将被自动执行。

### 层层链路

上一个M连接下个S

### 谋朝篡位

如果主机断开了连接，可以使用`SLAVEOF no one` 让自己变成主机！其他的节点就可以手动连接到最新的这个主节点（手动）。如果这时候之前的主节点修复了，就要重新连接！

## 哨兵模式

主从切换技术的方法是：当主服务器宕机后，需要手动吧一台从服务器切换为主服务器，这就需要人工干预，费时费力，还会造成一段时间内服务不可用。这不是一种推荐的方式，更多时候，我们优先考虑哨兵模式。Redis从2.8开始正式提供了Sentinel（哨兵）架构来解决这个问题。

谋朝篡位的自动版，能够后台监控主机是否故障，如果故障了根据投票数自动将从库转换为主库。

哨兵模式是一种特殊的模式，首先Redis提供了哨兵的命令，哨兵是一个独立的进程，作为进程，他会独立运行。其原理是：

**哨兵通过发送命令，等待Redis服务器响应，从而监控运行的多个Redis实例**

单个哨兵的两个作用：

- 通过发送命令，让Redis服务器返回监控其运行状态，包括主服务器和从服务器
- 当哨兵检测到master宕机，会自动将slave切换成master，然后通过**发布订阅模式**通知其他的从服务器，修改配置文件，让它们切换主机。

然而一个哨兵进程对Redis服务器进行监控，可能会出现问题，为此，我们可以使用多个哨兵进行监控。各个哨兵之间还会进行监控，这样就形成了多哨兵模式。

假设主服务器宕机，哨兵1先检测到这个结果，系统并不会马上进行failover过程，仅仅是哨兵1主管的认为主服务器不可用，这个现象称为**主观下线**。当后面的哨兵也检测到主服务器不可用，并且数量达到一定值时，那么哨兵之间就会进行一次投票，投票的结果由一个哨兵发起，进行failover（故障转移）操作。切换成功后，就会通过发布订阅模式，让各个哨兵把自己监控的从服务器实现切换主机。这个过程称为**客观下线**

1、配置哨兵配置文件 sentinel.conf

```bash
# sentinel monitor 被监控的名称 host port 1
sentinel monitor myredis 127.0.0.1 6379 1
```

后面这个数字1，代表主机挂了，slave投票看让谁接替称为主机，票数最多的，就会成为主机！

2、启动哨兵

```bash
redis-sentinel sentinel.conf
```

如果主机此时回来了，只能归并到新的主机下，当作从机，这就是哨兵模式的规则

### 优缺点

**优点：**

1. 哨兵集群，基于主从复制模式，所有的主从配置优点，他全有
2. 主从可以切换，故障可以转移，系统的可用性就会更好
3. 哨兵模式就是主从模式的升级，手动到自动，更加健壮！

**缺点：**

1. Redis不好在线扩容的，集群容量一旦到达上限，在线扩容就十分麻烦！
2. 实现哨兵模式的配置其实是相当麻烦的，里面有很多选择！

## Redis缓存穿透和雪崩

### 缓存穿透（查不到）

**概念**

用户想要查询一个数据，发现redis内存数据库没有，也就是缓存没有命中，于是向持久层数据库查询。发现也没有，于是本次查询失败。当用户很多的时候，缓存都没有命中（秒杀），于是都去请求了持久层数据库。这会给持久层数据库造成很大的压力，这时候就相当于出现了缓存穿透。

**解决方案**

1. 布隆过滤器

布隆过滤器是一种数据结构，对所有可能查询的参数以hash形式存储，在控制层先进行校验，不符合则丢弃，从而避免了对底层存储系统的查询压力；

2. 缓存空对象

当存储层不命中后，即使返回的空对象也将其缓存起来，同时会设置一个过期时间，之后再访问这个数据将会从缓存中获取，保存了后端数据源。

但是这种方法会存在两个问题：

- 如果控制能够被缓存起来，这就意味着缓存需要更多的空间存储更多的键，应为这当中可能会有很多的空值的键；
- 即使对空值设置了过期时间，还是会存在缓存层和存储层的数据会有一段时间窗口不一致，这对于需要保持一致性的业务会有影响。

### 缓存击穿（量太大，缓存过期！！）

**概念**

缓存击穿是指一个key非常热点，在不停的扛着大并发，大并发集中对这个点进行访问，当这个key在失效的瞬间，持续的大并发就会穿破缓存，直接请求数据库，就像在一个屏障上凿开一个洞。

当某个key在过期的瞬间，有大量的请求并发访问，这类数据一般是热点数据，由于缓存过期，会同时访问数据库来查询最新数据，并且回写缓存，会导致数据库瞬间压力过大。

**解决方案**

- 设置热点数据永不过期

从缓存层面来看，没有设置过期时间，所以不会出现热点key过期后产生的问题。

- 加互斥锁

分布式锁：使用分布式锁，保证对于每个key同时只有一个线程去查询后段服务，其他现场没有获得分布式锁的权限，因此只需要等待即可。这种方式将高并发的压力转移到了分布式锁，硬刺队分布式锁的考验很大。

### 缓存雪崩

**概念**

缓存雪崩，是指在某一个时间段，缓存集中过期失效。Redis宕机！

产生雪崩的原因之一，比如在写本文的时候，马上就要到双十二零点，很快就会迎来一波抢购，这波商品时间比较集中的放入了缓存，假设缓存一小时。那么到了凌晨一点的时候，这批商品的缓存就过期了。而对这批商品的访问查询，都落到了数据库上，对于数据库而言，就会产生周期性的压力波峰。于是所有的请求都会达到存储层，存储层的调用量就会暴增，造成存储层也会挂掉的情况。

其实集中过期，倒不是非常致命，比较致命的缓存雪崩，是缓存服务器某个节点宕机或断网。因为自然形成的缓存雪崩，一定是在某个时间段集中创建缓存。这个时候，数据库也是可以顶住压力的。无非就是对数据库产生周期性的压力而已。而缓存服务节点宕机，对数据库服务器造成的压力是不可预知的，很有可能瞬间就把数据库压垮。

**解决方案**

- redis高可用

这个思想的含义是，既然redis有可能挂掉，那我多增设几台redis，这样一台挂掉之后其他的还可以继续工作，其实就是搭建的集群。

- 限流降级

这个解决方案的思想是，在缓存失效后，通过加锁或者队列来控制读数据库写缓存的线程数量。比如对某个key只允许一个线程查询数据和写缓存，其他线程等待。

- 数据预热

数据加热的含义就是在正式部署之前，我先把可能的数据先预先访问一遍，这样部分可能大量访问的数据就会加载到缓存中。在即将发生大并发访问前手动出发加载缓存不同的key，设置不同的过期时间，让缓存失效的时间点尽量均匀。