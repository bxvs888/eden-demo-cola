# Quick Start

If your goal is simply to get the project running locally, this page is enough.

## What You Will Get

After finishing the steps below, you will be able to:

- build `eden-demo-cola` locally
- start the demo application
- verify the project with a simple API call
- know which docs to read next

## Before You Begin

### Requirements

- JDK 1.8+ (`2.4.x` branch) / JDK 11+ (`2.7.x` branch) / JDK 17+ (`3.0.x` branch)
- Maven 3.6+
- Git

### Prerequisite Dependency

This project depends on `eden-architect`, so you need to install it locally before the first startup.

## Step 1: Clone the repository

```bash
git clone https://github.com/shiyindaxiaojie/eden-demo-cola.git
cd eden-demo-cola
```

## Step 2: Install the dependency project

```bash
git clone https://github.com/shiyindaxiaojie/eden-architect.git
cd eden-architect && mvn install -T 4C -DskipTests
```

If you skip this step, the build usually fails right away.

## Step 3: Build the current project

```bash
cd eden-demo-cola && mvn install -T 4C -DskipTests
```

Once the build succeeds, move to the startup module.

## Step 4: Start the application

```bash
cd eden-demo-cola-start
mvn spring-boot:run
```

You can also run `ColaApplication.java` directly.

## Step 5: Verify the startup

After the application is up, open:

[http://localhost:8081/api/users/1](http://localhost:8081/api/users/1)

If the endpoint returns successfully, the minimal startup flow is working.

## Default Local Setup

By default, the project is optimized for a fast first run:

- the development profile uses an H2 in-memory database
- you do not need the full external middleware stack upfront
- it is suitable for validating the project structure and base flow first

Example configuration:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:db;DB_CLOSE_ON_EXIT=TRUE
    driver-class-name: org.h2.Driver
```

## If You Want to Switch to MySQL

Update `application-dev.yml`:

```yaml
spring:
  datasource:
    username: root
    password: your_password
    url: jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=GMT%2B8
    driver-class-name: com.mysql.cj.jdbc.Driver
```

If you switch to MySQL, it is a good idea to read [Data Source Configuration](datasource.md) and [Liquibase Database Version Management](liquibase.md) next.

## Common Issues

1. **Build fails immediately**: `eden-architect` is usually not installed yet
2. **Port 8081 is already in use**: change `server.port` and restart
3. **JDK version mismatch**: make sure the current branch matches your JDK version
4. **Startup succeeds but API fails**: check whether your config points to unavailable external services

## What to Read Next

After the app starts successfully, a common reading order is:

1. [Nacos Registry & Configuration Center](nacos.md)
2. [Data Source Configuration](datasource.md)
3. [Redis Cache](redis.md)
4. Then choose MQ, observability, or scheduling docs based on your needs

## Recommendations

1. Get the project running with the default setup before adding external components
2. Add one infrastructure component at a time to make troubleshooting easier
3. Verify each configuration change immediately instead of batching many changes together
