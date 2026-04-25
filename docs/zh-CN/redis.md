# Redis 缓存

Redis 在这个项目里主要承担两类工作：缓存热点数据，以及提供简单的分布式协作能力，例如分布式锁。

## 适用场景

如果你遇到下面这些问题，可以优先考虑接入 Redis：

- 数据读取频繁，直接查数据库成本高
- 多实例部署后，需要共享缓存数据
- 需要做简单的分布式锁控制，避免重复处理
- 希望把部分短期状态放到内存型存储里

如果你只是本地把项目先跑起来，可以暂时不接 Redis，等基础链路通了再补。

## 接入步骤

### 1. 准备 Redis 服务

可以先用 Docker 快速起一个本地实例：

```bash
docker run -d --name redis -p 6379:6379 redis:latest
```

### 2. 开启项目配置

修改 `application-dev.yml`：

```yaml
spring:
  redis:
    enabled: true
    host: localhost
    port: 6379
    password: demo@123
    database: 1
    timeout: 5000
    lettuce:
      pool:
        min-idle: 1
        max-idle: 64
        max-active: 64
        max-wait: -1
```

如果你本地没有设置密码，记得同步调整 `password`。

### 3. 用最小代码验证连通性

```java
@Autowired
private StringRedisTemplate redisTemplate;

redisTemplate.opsForValue().set("key", "value", 30, TimeUnit.MINUTES);
String value = redisTemplate.opsForValue().get("key");
```

如果能正常写入和读取，说明 Redis 基本接通。

## 示例场景

### 场景一：缓存用户信息

适合读多写少、允许短时间缓存的查询场景：

```java
@Cacheable(value = "user", key = "#id")
public User getUserById(Long id) {
    return userRepository.findById(id);
}
```

### 场景二：做一个简单的分布式锁

适合短事务、幂等保护这类轻量控制场景：

```java
Boolean locked = redisTemplate.opsForValue()
    .setIfAbsent("lock:order:" + orderId, "1", 30, TimeUnit.SECONDS);
if (Boolean.TRUE.equals(locked)) {
    try {
        // 业务逻辑
    } finally {
        redisTemplate.delete("lock:order:" + orderId);
    }
}
```

如果锁对应的是核心业务，建议额外考虑超时、续期和误删保护，不要把这个最小示例直接当成生产方案。

## 常见问题

1. **连不上 Redis**：先确认端口、密码和数据库编号是否一致
2. **高并发下连接不够**：根据实际并发量调整 Lettuce 连接池参数
3. **命中率低**：检查缓存 Key 设计和过期时间是否合理
4. **锁偶发失效**：确认业务执行时间是否超过锁超时时间

## 使用建议

1. 本地开发优先保证可连通，再逐步调连接池参数
2. 缓存要设置过期时间，避免无界增长
3. Key 命名尽量带业务前缀，方便排查和批量管理
4. 对一致性要求高的业务，不要只靠缓存结果做最终判断
