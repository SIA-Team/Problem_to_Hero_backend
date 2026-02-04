# Nacos 工作流程与使用指南

## 一、Nacos 概述

Nacos（Dynamic Naming and Configuration Service）是阿里巴巴开源的服务注册与配置管理平台，提供以下核心功能：

1. **服务注册与发现**（Service Discovery）
2. **配置管理**（Configuration Management）
3. **服务健康检查**（Health Check）
4. **动态配置刷新**（Dynamic Configuration）

---

## 二、Nacos 工作流程

### 2.1 服务注册与发现流程

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│  服务提供者  │─────────▶│   Nacos     │◀────────│  服务消费者 │
│ (Provider)  │  注册    │   Server    │  发现   │ (Consumer) │
└─────────────┘         └─────────────┘         └─────────────┘
      │                        │                        │
      │  心跳检测              │                        │
      └───────────────────────▶│                        │
                               │                        │
                               │  服务列表推送          │
                               └───────────────────────▶│
```

**详细流程：**

1. **服务启动时注册**
   - 服务启动后，通过 `@EnableDiscoveryClient` 注解启用服务发现
   - 服务向 Nacos Server 发送注册请求（包含服务名、IP、端口等信息）
   - Nacos Server 将服务信息存储到注册中心

2. **服务心跳检测**
   - 服务定期（默认5秒）向 Nacos 发送心跳
   - Nacos 检测到服务心跳超时（默认15秒）后，标记服务为不健康
   - 超过30秒未收到心跳，Nacos 会将该服务从注册中心移除

3. **服务发现**
   - 服务消费者启动时，从 Nacos 获取服务列表
   - 网关通过服务发现自动路由到对应的服务实例
   - 支持负载均衡（默认轮询）

4. **服务下线**
   - 服务正常关闭时，主动向 Nacos 发送注销请求
   - Nacos 从注册中心移除该服务实例

### 2.2 配置中心工作流程

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│  开发者     │─────────▶│   Nacos     │◀────────│  应用服务   │
│             │  发布配置 │   Config    │  拉取配置 │             │
└─────────────┘         └─────────────┘         └─────────────┘
                               │                        │
                               │  配置变更推送          │
                               └───────────────────────▶│
                                                        │
                                                        │  配置刷新
                                                        └─────────▶
```

**详细流程：**

1. **配置发布**
   - 开发者在 Nacos 控制台创建配置（Data ID、Group、Namespace）
   - 配置保存到 MySQL 数据库（持久化）

2. **配置拉取**
   - 应用启动时，从 Nacos 拉取配置
   - 支持配置的实时监听和动态刷新

3. **配置变更**
   - 配置修改后，Nacos 推送给监听该配置的应用
   - 应用自动刷新配置，无需重启

---

## 三、Nacos Server 安装流程

### 3.1 环境要求

| 环境       | 要求说明 |
|------------|----------|
| JDK        | 1.8+（Nacos 2.x 单机/集群）；若使用 Nacos 3.x 需 Java 17+ |
| MySQL      | 5.6.5+（可选，用于持久化配置与元数据；单机可先用内嵌库） |
| 磁盘       | 建议预留 1GB+ 用于日志与数据 |
| 内存       | 单机建议 2GB+；集群每节点 2GB+ |

### 3.2 方式一：Docker 单容器安装

适合本机快速体验，数据默认在容器内（重启后可能丢失，可挂载卷持久化）。

```bash
# 拉取镜像（推荐与项目一致使用 2.3.0）
docker pull nacos/nacos-server:v2.3.0

# 单机模式、内置存储（不持久化）
docker run -d \
  --name nacos-standalone \
  -e MODE=standalone \
  -e NACOS_AUTH_ENABLE=true \
  -e JVM_XMS=256m -e JVM_XMX=256m \
  -p 8848:8848 \
  -p 9848:9848 \
  -p 9849:9849 \
  nacos/nacos-server:v2.3.0

# 使用 MySQL 持久化（需先建库并执行 Nacos 的 mysql-schema.sql）
docker run -d \
  --name nacos-standalone \
  -e MODE=standalone \
  -e SPRING_DATASOURCE_PLATFORM=mysql \
  -e MYSQL_SERVICE_HOST=宿主机或MySQL容器IP \
  -e MYSQL_SERVICE_PORT=3306 \
  -e MYSQL_SERVICE_DB_NAME=nacos_config \
  -e MYSQL_SERVICE_USER=root \
  -e MYSQL_SERVICE_PASSWORD=你的密码 \
  -e NACOS_AUTH_ENABLE=true \
  -p 8848:8848 -p 9848:9848 -p 9849:9849 \
  nacos/nacos-server:v2.3.0
```

### 3.3 方式二：Docker Compose 安装（本项目推荐）

项目根目录已提供 `docker-compose.yml`，包含 Nacos 与 MySQL 等依赖，一键启动即可完成安装与启动。

**步骤：**

1. **确认已安装 Docker 与 Docker Compose**
   ```bash
   docker --version
   docker-compose --version
   ```

2. **创建 Nacos 使用的数据库（若 MySQL 由本 compose 启动）**
   - 项目 `sql/init.sql` 中已包含：`CREATE DATABASE IF NOT EXISTS nacos_config ...`
   - 首次执行 `docker-compose up -d` 时，MySQL 会执行 `init.sql`，自动创建 `nacos_config` 库

3. **初始化 Nacos 表结构（首次必须）**
   - Nacos 2.x 使用 MySQL 时需先执行官方 `mysql-schema.sql`。
   - 从容器中拷贝脚本到本地并导入（在项目根目录执行）：
   ```bash
   # 拷贝 Nacos 提供的 MySQL 建表脚本
   docker run --rm --entrypoint cat nacos/nacos-server:v2.3.0 /home/nacos/conf/mysql-schema.sql > sql/nacos-mysql-schema.sql

   # 导入到已运行的 MySQL（按实际主机、端口、用户、密码修改）
   mysql -h 127.0.0.1 -P 3306 -u root -p nacos_config < sql/nacos-mysql-schema.sql
   ```
   - 若 MySQL 尚未启动，可先 `docker-compose up -d mysql`，等待就绪后再执行上述导入。

4. **启动 Nacos 及依赖**
   ```bash
   docker-compose up -d
   ```
   - 会启动 MySQL、Redis、RabbitMQ、Nacos。Nacos 通过环境变量连接 MySQL（见 `docker-compose.yml` 中 `nacos` 的 `environment`）。

5. **验证安装**
   - 控制台：http://localhost:8848/nacos ，默认账号/密码：`nacos` / `nacos`
   - 健康检查：`curl http://localhost:8848/nacos/v1/console/health/liveness`

### 3.4 方式三：单机版安装包（无 Docker）

适合无法使用 Docker 的服务器。

1. **下载 release 包**
   - 打开：https://github.com/alibaba/nacos/releases
   - 下载 `nacos-server-2.3.0.zip`（或当前推荐 2.x 版本），解压到目录如 `D:\nacos` 或 `/opt/nacos`。

2. **配置 MySQL（可选，生产建议使用）**
   - 创建数据库：`CREATE DATABASE nacos_config DEFAULT CHARACTER SET utf8mb4;`
   - 执行 Nacos 解压目录下 `conf/mysql-schema.sql` 到 `nacos_config` 库。
   - 修改 `conf/application.properties`：
   ```properties
   spring.datasource.platform=mysql
   db.num=1
   db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true
   db.user.0=root
   db.password.0=你的密码
   ```

3. **单机模式启动**
   - Windows：`bin\startup.cmd -m standalone`
   - Linux/macOS：`sh bin/startup.sh -m standalone`

4. **停止**
   - Windows：`bin\shutdown.cmd`
   - Linux/macOS：`sh bin/shutdown.sh`

### 3.5 方式四：集群模式（简要）

生产环境建议多节点集群，保证高可用。

1. **准备**
   - 多台机器或多个容器，每节点部署同一版本 Nacos，并配置相同 MySQL（执行同一份 `mysql-schema.sql`）。

2. **配置集群**
   - 在每台节点 `conf/cluster.conf` 中列出所有节点 IP:8848，例如：
   ```
   192.168.1.1:8848
   192.168.1.2:8848
   192.168.1.3:8848
   ```

3. **启动**
   - 使用 `bin/startup.sh`（不加 `-m standalone`），Nacos 会按 `cluster.conf` 组成集群。
   - 客户端连接任意节点或前面挂载负载均衡（如 Nginx）即可。

### 3.6 安装后必做事项

- **改默认密码**：首次登录控制台后修改 `nacos` 账号密码。
- **端口**：默认 8848（HTTP）、9848/9849（gRPC），防火墙/安全组需放行。
- **持久化**：生产务必使用 MySQL，并定期备份 `nacos_config` 库。

---

## 四、启动 Nacos

### 4.1 使用 Docker Compose 启动（推荐）

项目已配置好 `docker-compose.yml`，直接启动即可：

```bash
# 启动所有服务（包括 Nacos）
docker-compose up -d

# 查看 Nacos 容器状态
docker ps | grep nacos

# 查看 Nacos 日志
docker logs -f problem-to-hero-nacos
```

**启动顺序：**
1. MySQL 先启动（Nacos 依赖 MySQL）
2. Nacos 自动启动并连接 MySQL
3. Nacos 自动初始化数据库表（首次启动）

### 4.2 验证 Nacos 启动

1. **检查容器状态**
   ```bash
   docker ps
   ```
   应该看到 `problem-to-hero-nacos` 容器运行中

2. **访问 Nacos 控制台**
   - 地址：http://localhost:8848/nacos
   - 默认账号：`nacos`
   - 默认密码：`nacos`

3. **检查 Nacos 健康状态**
   ```bash
   curl http://localhost:8848/nacos/v1/console/health/liveness
   ```
   返回 `{"status":"UP"}` 表示正常

### 4.3 Nacos 端口说明

- **8848**：Nacos 主端口（HTTP API 和控制台）
- **9848**：客户端 gRPC 请求服务端端口（用于服务发现）
- **9849**：客户端 gRPC 请求服务端端口（用于配置管理）

---

## 五、Nacos 控制台使用

### 5.1 登录控制台

1. 访问 http://localhost:8848/nacos
2. 输入用户名：`nacos`，密码：`nacos`
3. 首次登录会提示修改密码（建议修改）

### 5.2 服务管理（服务注册与发现）

#### 查看服务列表

1. 点击左侧菜单 **"服务管理" → "服务列表"**
2. 可以看到所有注册到 Nacos 的服务

**服务信息说明：**
- **服务名**：`spring.application.name` 配置的值
- **分组**：默认 `DEFAULT_GROUP`
- **健康实例数**：当前健康的服务实例数量
- **触发保护阈值**：服务保护阈值（防止流量过大）

#### 查看服务详情

1. 点击服务名，查看服务详情
2. 可以看到：
   - **实例列表**：所有服务实例的 IP、端口、健康状态
   - **集群**：服务所属的集群
   - **元数据**：服务的元数据信息

#### 服务实例管理

- **上线/下线**：手动控制服务实例的可用状态
- **删除**：从注册中心移除服务实例
- **编辑**：修改服务实例的元数据

### 5.3 配置管理（配置中心）

#### 创建命名空间（Namespace）

1. 点击左侧菜单 **"命名空间"**
2. 点击 **"新建命名空间"**
3. 填写信息：
   - **命名空间ID**：如 `dev`、`test`、`prod`
   - **命名空间名**：如 `开发环境`、`测试环境`、`生产环境`
   - **描述**：可选

**命名空间用途：**
- 环境隔离：dev、test、prod
- 项目隔离：不同项目使用不同命名空间

#### 创建配置（Data ID）

1. 点击左侧菜单 **"配置管理" → "配置列表"**
2. 选择命名空间（默认 `public`）
3. 点击 **"+"** 或 **"新建配置"**

**配置信息：**
- **Data ID**：配置文件的唯一标识
  - 格式：`{application-name}-{profile}.{file-extension}`
  - 示例：`user-service-dev.yml`、`api-gateway-dev.properties`
- **Group**：配置分组，默认 `DEFAULT_GROUP`
- **配置格式**：YAML、Properties、JSON、XML 等
- **配置内容**：实际的配置内容

**示例配置（user-service-dev.yml）：**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/problem_to_hero
    username: root
    password: root
  redis:
    host: localhost
    port: 6379
```

#### 配置监听与刷新

1. **应用配置 Nacos Config**
   ```yaml
   spring:
     cloud:
       nacos:
         config:
           server-addr: localhost:8848
           namespace: dev  # 命名空间
           group: DEFAULT_GROUP
           file-extension: yml  # 配置文件格式
           shared-configs:  # 共享配置
             - data-id: common-config.yml
               group: DEFAULT_GROUP
               refresh: true
   ```

2. **配置自动刷新**
   - 使用 `@RefreshScope` 注解的 Bean 会自动刷新
   - 配置变更后，应用会自动拉取新配置

---

## 六、项目中的 Nacos 配置

### 6.1 服务注册配置

所有服务的配置文件（`application-dev_template.yml`）中已配置：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}  # Nacos 服务器地址
        namespace: ${NACOS_NAMESPACE:}                    # 命名空间（可选）
        group: ${NACOS_GROUP:DEFAULT_GROUP}              # 分组
        enabled: true                                     # 启用服务发现
```

### 6.2 网关服务发现配置

网关配置（`application.yml`）中已启用服务发现路由：

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true              # 启用服务发现路由
          lower-case-service-id: true # 服务ID转小写
```

**效果：**
- 网关自动从 Nacos 发现服务
- 路由配置中的 `lb://user-service` 会自动解析为实际的服务实例

### 6.3 启动类配置

所有服务的启动类已添加 `@EnableDiscoveryClient` 注解：

```java
@SpringBootApplication
@EnableDiscoveryClient  // 启用服务发现
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

---

## 七、实际操作步骤

### 7.1 启动 Nacos 并验证

```bash
# 1. 启动基础设施
cd e:\github\sia\Problem_to_Hero_backend
docker-compose up -d

# 2. 等待 Nacos 启动（约30秒）
docker logs -f problem-to-hero-nacos

# 3. 访问控制台
# 浏览器打开：http://localhost:8848/nacos
# 账号：nacos / 密码：nacos
```

### 7.2 启动服务并查看注册情况

1. **启动用户服务**
   ```bash
   # 在 IDEA 中启动 UserServiceApplication
   # 或者使用命令行
   cd code/services/user-service
   mvn spring-boot:run
   ```

2. **查看服务注册**
   - 访问 Nacos 控制台：http://localhost:8848/nacos
   - 进入 **"服务管理" → "服务列表"**
   - 应该能看到 `user-service` 服务已注册

3. **查看服务实例**
   - 点击 `user-service` 服务名
   - 查看实例列表，应该能看到：
     - IP：服务运行的 IP 地址
     - 端口：8081
     - 健康状态：健康

### 7.3 创建配置（可选）

如果需要使用配置中心，可以创建配置：

1. **创建命名空间**
   - 命名空间ID：`dev`
   - 命名空间名：`开发环境`

2. **创建配置**
   - Data ID：`user-service-dev.yml`
   - Group：`DEFAULT_GROUP`
   - 配置格式：`YAML`
   - 配置内容：从 `application-dev_template.yml` 复制

3. **应用配置 Nacos Config**
   - 在服务的 `pom.xml` 中添加：
     ```xml
     <dependency>
         <groupId>com.alibaba.cloud</groupId>
         <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
     </dependency>
     ```
   - 在 `bootstrap.yml` 中配置 Nacos Config

---

## 八、常见问题

### 8.1 Nacos 启动失败

**问题：** Nacos 容器启动后立即退出

**解决：**
1. 检查 MySQL 是否启动：`docker ps | grep mysql`
2. 检查 Nacos 日志：`docker logs problem-to-hero-nacos`
3. 确认数据库 `nacos_config` 已创建
4. 如果数据库表未初始化，需要手动执行 SQL 脚本

### 8.2 服务无法注册到 Nacos

**问题：** 服务启动后，在 Nacos 控制台看不到服务

**解决：**
1. 检查 Nacos 是否运行：访问 http://localhost:8848/nacos
2. 检查服务配置：
   ```yaml
   spring.cloud.nacos.discovery.server-addr: localhost:8848
   spring.cloud.nacos.discovery.enabled: true
   ```
3. 检查启动类是否有 `@EnableDiscoveryClient` 注解
4. 查看服务日志，是否有连接 Nacos 的错误

### 8.3 网关无法发现服务

**问题：** 网关启动后，无法路由到后端服务

**解决：**
1. 确认网关已启用服务发现：
   ```yaml
   spring.cloud.gateway.discovery.locator.enabled: true
   ```
2. 确认网关已注册到 Nacos
3. 确认后端服务已注册到 Nacos
4. 检查路由配置中的服务名是否正确

---

## 九、最佳实践

1. **命名空间管理**
   - 使用命名空间隔离不同环境（dev、test、prod）
   - 不同项目使用不同的命名空间

2. **配置管理**
   - 敏感信息（密码、密钥）不要放在配置中心
   - 使用环境变量或密钥管理工具

3. **服务注册**
   - 服务名使用小写和连字符：`user-service`
   - 避免使用特殊字符

4. **健康检查**
   - 配置合理的健康检查间隔
   - 监控服务健康状态

5. **高可用**
   - 生产环境使用 Nacos 集群模式
   - 配置多个 Nacos 节点

---

## 十、参考资源

- **Nacos 官方文档**：https://nacos.io/zh-cn/docs/what-is-nacos.html
- **Spring Cloud Alibaba 文档**：https://github.com/alibaba/spring-cloud-alibaba
- **Nacos GitHub**：https://github.com/alibaba/nacos

---

**最后更新：** 2026-02-04
