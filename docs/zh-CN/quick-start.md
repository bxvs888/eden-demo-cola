# 快速开始

如果你只想先把项目跑起来，按这篇文档走就够了。

## 你会得到什么

完成下面步骤后，你可以：

- 在本地成功构建 `eden-demo-cola`
- 启动示例应用
- 通过一个简单接口验证项目已正常运行
- 了解后续应该先看哪些组件文档

## 开始前先确认

### 环境要求

- JDK 1.8+（`2.4.x` 分支）/ JDK 11+（`2.7.x` 分支）/ JDK 17+（`3.0.x` 分支）
- Maven 3.6+
- Git

### 前置依赖

这个项目依赖 `eden-architect`，第一次本地启动前需要先安装它。

## 第一步：拉取代码

```bash
git clone https://github.com/shiyindaxiaojie/eden-demo-cola.git
cd eden-demo-cola
```

## 第二步：安装依赖项目

```bash
git clone https://github.com/shiyindaxiaojie/eden-architect.git
cd eden-architect && mvn install -T 4C -DskipTests
```

如果这一步没做，后续构建大概率会直接失败。

## 第三步：构建当前项目

```bash
cd eden-demo-cola && mvn install -T 4C -DskipTests
```

构建成功后，再进入启动模块。

## 第四步：启动应用

```bash
cd eden-demo-cola-start
mvn spring-boot:run
```

也可以直接运行启动类 `ColaApplication.java`。

## 第五步：验证是否启动成功

启动完成后，访问：

[http://localhost:8081/api/users/1](http://localhost:8081/api/users/1)

如果接口可以正常返回，说明最小启动链路已经打通。

## 本地默认配置说明

默认情况下，项目更偏向“先跑起来”的体验：

- 开发环境默认使用 H2 内存数据库
- 不需要先准备完整的外部中间件
- 适合先验证工程结构和基础链路

示例配置如下：

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:db;DB_CLOSE_ON_EXIT=TRUE
    driver-class-name: org.h2.Driver
```

## 如果你想切到 MySQL

修改 `application-dev.yml`：

```yaml
spring:
  datasource:
    username: root
    password: your_password
    url: jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=GMT%2B8
    driver-class-name: com.mysql.cj.jdbc.Driver
```

切到 MySQL 后，建议再结合 [数据源配置](datasource.md) 和 [Liquibase 数据库版本管理](liquibase.md) 一起看。

## 常见问题

1. **构建直接失败**：通常是还没先安装 `eden-architect`
2. **8081 端口被占用**：修改 `server.port` 后再启动
3. **JDK 不兼容**：确认当前分支对应的 JDK 版本
4. **启动后接口报错**：先检查当前配置文件是否指向了不可用的外部服务

## 下一步看什么

启动成功后，通常按下面顺序继续：

1. [Nacos 注册中心与配置中心](nacos.md)
2. [数据源配置](datasource.md)
3. [Redis 缓存](redis.md)
4. 根据需要选择 MQ、监控或任务调度文档

## 使用建议

1. 先用默认配置把项目跑通，再按需接入外部组件
2. 每次只新增一种基础设施，方便定位问题
3. 配置改动后立即做最小验证，不要攒一堆改动一起排查
