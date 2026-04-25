# COLA Component Docs

Use this documentation to answer three practical questions as quickly as possible:

- Do I need this component in the current project?
- What is the minimum setup to get it working?
- What should I verify right after integration?

If you are new to this repository, start here:

1. [Quick Start](quick-start.md)
2. [Nacos Registry & Configuration Center](nacos.md)
3. [Data Source Configuration](datasource.md)
4. Then pick Redis, MQ, observability, or scheduling docs as needed

## Reading Guide

- **Get the project running first**: start with [Quick Start](quick-start.md)
- **Set up core infrastructure next**: Nacos, data source, and Redis are the usual first stops
- **Read by scenario**: only open the component docs you need for the current task
- **Troubleshoot efficiently**: jump to the last sections of each doc for common issues and recommendations

## Browse by Integration Stage

### 1. Startup and Core Configuration

- [Quick Start](quick-start.md) - Local build, startup, and first verification
- [Nacos Registry & Configuration Center](nacos.md) - Service registry, config delivery, and environment isolation
- [Data Source Configuration](datasource.md) - H2, MySQL, and connection pool setup
- [Liquibase Database Version Management](liquibase.md) - Database change tracking and migration scripts

### 2. Common Runtime Capabilities

- [Redis Cache](redis.md) - Caching, distributed locks, and connection basics
- [Dynamic-Datasource Multi-datasource](dynamic-datasource.md) - Datasource switching and read/write splitting
- [ShardingSphere Sharding](sharding-sphere.md) - Sharding strategy and read/write separation

### 3. Messaging Capabilities

- [RocketMQ Message Queue](rocketmq.md) - RocketMQ producer, consumer, and common configuration
- [Kafka Message Engine](kafka.md) - Kafka producer, consumer, and listener setup
- [Dynamic-MQ Dynamic Message Queue](dynamic-mq.md) - Switching between RocketMQ and Kafka

### 4. Observability and Governance

- [CAT Application Monitoring](cat.md) - Application monitoring and trace visibility
- [Jaeger Distributed Tracing](jaeger.md) - Trace collection and distributed tracing analysis
- [Zipkin Distributed Tracing](zipkin.md) - Zipkin reporting and trace-log correlation
- [Sentinel Traffic Governance](sentinel.md) - Rate limiting, circuit breaking, and rule persistence
- [Arthas Online Diagnostics](arthas.md) - Online diagnostics and troubleshooting
- [Dynamic-TP Dynamic Thread Pool](dynamic-tp.md) - Thread-pool monitoring, tuning, and alerts

### 5. RPC and Scheduling

- [Dubbo RPC Framework](dubbo.md) - Dubbo service publishing, calling, and governance
- [XXL-Job Distributed Task Scheduling](xxl-job.md) - Job registration, scheduling, and execution management
