# Problem to Hero Backend

## 项目简介

Problem to Hero是一个问答社区平台的后端服务，采用微服务架构，支持用户注册登录、问题发布、回答、评论、悬赏等功能。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| **Java** | 21 | JDK版本 |
| **Spring Boot** | 3.2.5 | 核心框架，提供自动配置和快速开发能力 |
| **Spring Cloud** | 2023.0.1 | 微服务框架，提供服务治理能力 |
| **Spring Cloud Gateway** | 4.0.9 | API网关，负责路由转发、负载均衡、限流、鉴权 |
| **MySQL** | 8.0+ | 主数据库，支持主从复制、读写分离 |
| **Redis** | 6.x / 7.x | 缓存数据库，用于Session/Token存储、热点数据缓存、分布式锁 |
| **Elasticsearch** | 7.x / 8.x | 全文搜索引擎，用于内容搜索 |
| **RabbitMQ** | 3.11.x | 消息队列，用于异步任务处理 |
| **MyBatis-Plus** | 3.5.5 | ORM框架，简化数据库操作 |
| **HikariCP** | 5.1.0 | 数据库连接池（Spring Boot默认，性能优秀） |
| **Hutool** | 5.8.25 | Java工具类库 |
| **Lombok** | 1.18.30 | 代码简化，减少样板代码 |
| **MapStruct** | 1.5.5.Final | 对象映射工具 |

## 项目结构

详见 `doc/模块与目录结构定义.md`

## 开发规范

详见 `doc/开发规范.md`

## 快速开始

1. 安装JDK 21
2. 安装Maven 3.8+
3. 启动MySQL、Redis、RabbitMQ（可使用docker-compose）
4. 运行各服务

## 文档目录

- `doc/开发方案.md` - 技术架构方案
- `doc/功能需求优先级.md` - 功能需求优先级
- `doc/第一阶段框架搭建计划.md` - 框架搭建计划
- `doc/模块与目录结构定义.md` - 模块与目录结构
- `doc/开发规范.md` - 开发规范
