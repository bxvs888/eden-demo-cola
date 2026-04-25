# Redis Cache

In this project, Redis mainly serves two purposes: caching hot data and providing lightweight distributed coordination such as simple locks.

## When to Use

Redis is a good fit when you are dealing with situations like these:

- reads are frequent and direct database access is too expensive
- multiple service instances need to share cached data
- you need lightweight distributed locking to prevent duplicate work
- some short-lived state should live in an in-memory store

If you are only trying to get the project running locally, you can postpone Redis until the base flow is already working.

## Setup

### 1. Prepare a Redis instance

A quick local option is Docker:

```bash
docker run -d --name redis -p 6379:6379 redis:latest
```

### 2. Enable project configuration

Update `application-dev.yml`:

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

If your local Redis does not use a password, adjust `password` accordingly.

### 3. Verify connectivity with minimal code

```java
@Autowired
private StringRedisTemplate redisTemplate;

redisTemplate.opsForValue().set("key", "value", 30, TimeUnit.MINUTES);
String value = redisTemplate.opsForValue().get("key");
```

If both write and read succeed, Redis is connected correctly.

## Example Scenarios

### Scenario 1: Cache user information

This fits read-heavy queries where short-lived caching is acceptable:

```java
@Cacheable(value = "user", key = "#id")
public User getUserById(Long id) {
    return userRepository.findById(id);
}
```

### Scenario 2: Use a simple distributed lock

This is suitable for short transactions or idempotency protection:

```java
Boolean locked = redisTemplate.opsForValue()
    .setIfAbsent("lock:order:" + orderId, "1", 30, TimeUnit.SECONDS);
if (Boolean.TRUE.equals(locked)) {
    try {
        // Business logic
    } finally {
        redisTemplate.delete("lock:order:" + orderId);
    }
}
```

If the lock protects a critical business flow, do not treat this minimal sample as a production-ready locking strategy. Consider timeout handling, renewal, and delete-safety as well.

## Common Issues

1. **Cannot connect to Redis**: verify port, password, and database index first
2. **Pool size is too small under load**: adjust Lettuce pool settings based on actual concurrency
3. **Cache hit rate is low**: review key design and expiration settings
4. **Locks fail occasionally**: check whether business execution time exceeds the lock timeout

## Recommendations

1. In local development, focus on basic connectivity before tuning the pool
2. Always set expiration times for cache entries to avoid unbounded growth
3. Use business-prefixed keys for easier troubleshooting and management
4. Do not use cache results alone as the final source of truth for strongly consistent business flows
