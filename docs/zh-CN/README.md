# COLA 组件集成文档

这份文档主要帮你更快找到当前要接入的组件，以及对应的最小接入路径。

如果你是第一次接手这个项目，建议先看：

1. [快速开始](quick-start.md)
2. [Nacos 注册中心与配置中心](nacos.md)
3. [数据源配置](datasource.md)
4. 然后再按需选择 Redis、MQ、监控或任务调度文档

## 阅读建议

- **先把项目跑起来**：从 [快速开始](quick-start.md) 开始
- **先补基础设施**：通常优先看 Nacos、数据源、Redis
- **按场景阅读**：只打开当前任务需要的组件文档
- **排查问题时**：优先看每篇文档后半部分的常见问题和使用建议

## 按接入阶段查看

### 1. 启动与基础配置

- [快速开始](quick-start.md) - 本地构建、启动和首次验证
- [Nacos 注册中心与配置中心](nacos.md) - 服务注册、配置下发和环境隔离
- [数据源配置](datasource.md) - H2、MySQL 和连接池配置
- [Liquibase 数据库版本管理](liquibase.md) - 数据库变更管理和迁移脚本

### 2. 常用运行时能力

- [Redis 缓存](redis.md) - 缓存、分布式锁和基础连接配置
- [Dynamic-Datasource 多数据源](dynamic-datasource.md) - 数据源切换与读写分离
- [ShardingSphere 分库分表](sharding-sphere.md) - 分片策略与读写分离

### 3. 消息队列能力

- [RocketMQ 消息队列](rocketmq.md) - RocketMQ 的发送、消费和常用配置
- [Kafka 消息引擎](kafka.md) - Kafka 的生产消费和监听配置
- [Dynamic-MQ 动态消息队列](dynamic-mq.md) - 在 RocketMQ 和 Kafka 之间切换

### 4. 观测与治理能力

- [CAT 应用监控](cat.md) - 应用监控与链路追踪
- [Jaeger 分布式追踪](jaeger.md) - Trace 采集与链路分析
- [Zipkin 链路追踪](zipkin.md) - Zipkin 上报与日志关联
- [Sentinel 流量治理](sentinel.md) - 限流、熔断和规则持久化
- [Arthas 在线诊断](arthas.md) - 在线诊断与问题排查
- [Dynamic-TP 动态线程池](dynamic-tp.md) - 线程池监控、调参与告警

### 5. RPC 与任务调度

- [Dubbo RPC 框架](dubbo.md) - Dubbo 服务发布、调用与治理
- [XXL-Job 分布式任务调度](xxl-job.md) - 任务注册、调度和执行管理
