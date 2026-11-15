整合no sql

1.redis
1.1. 数据类型
1.1.1. string: 字符串 opsForValue
1.1.2. set: 集合 opsForSet
1.1.3. list: 列表 opsForList
1.1.4. zset: 有序集合 opsForZSet
1.1.5. hash: (k,v)集合 opsForHash
1.1.6. xxx : opsForXXX
1.2. 保存对象时，默认使用 jdk 序列化，不利于查看，一般使用 json 序列化
1.2.1 自定义序列化时，一般需要 RedisConfig 重新定义 redisTemplate
1.3. 默认提供两种客户端， jedis， lettuce ，可以在配置文件进行设置
1.3.1. 默认使用 lettuce， 基于netty的异步通信框架，并保持线程安全
1.3.2. 想使用 jedis 时需要在启动包排除lettuce 并引入相关依赖