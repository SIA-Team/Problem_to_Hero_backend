# RuoYi-Cloud 集成详细实施流程

## 文档说明

本文档详细描述如何将当前项目迁移到 RuoYi-Cloud 框架，并实现 B端/C端双端分离架构。

**实施方案：** 基于 Git 分支管理，使用 RuoYi-Cloud Spring Boot 3 分支代码初始化 `ruoyi-framework` 分支

**目标架构：** 方案 C - 微服务完全分离（管理端 + 业务端双端架构）

---

## 一、准备工作

### 1.1 环境检查

确保本地环境已安装：

```bash
# 检查 Java 版本（需要 Java 17+）
java -version

# 检查 Maven 版本
mvn -version

# 检查 Git 版本
git --version

# 检查 Docker（用于 Nacos、MySQL、Redis）
docker --version
docker-compose --version
```

### 1.2 启动基础服务

```bash
# 进入项目根目录
cd e:\github\sia\Problem_to_Hero_backend

# 启动 Docker Compose（Nacos、MySQL、Redis、RabbitMQ）
docker-compose up -d

# 验证服务状态
docker-compose ps
```

**验证访问：**
- Nacos: http://localhost:8848/nacos (nacos/nacos)
- MySQL: localhost:3306 (root/root123456)
- Redis: localhost:6379

---

## 二、分支管理与代码初始化

### 2.1 创建并切换到 ruoyi-framework 分支

```bash
# 确保当前在 main 分支，且工作区干净
git status

# 如果有未提交的修改，先提交或暂存
git add .
git commit -m "保存当前工作进度"

# 创建并切换到新分支
git checkout -b ruoyi-framework

# 验证当前分支
git branch
```

### 2.2 清空 code 目录

```bash
# 进入项目根目录
cd e:\github\sia\Problem_to_Hero_backend

# 删除 code 目录下所有内容（保留 code 目录本身）
# Windows PowerShell
Remove-Item -Path ".\code\*" -Recurse -Force

# 或者 Git Bash
rm -rf code/*

# 验证 code 目录为空
ls code
```

### 2.3 克隆 RuoYi-Cloud 代码

```bash
# 进入 code 目录
cd code

# 克隆 RuoYi-Cloud 仓库（使用 SSH 或 HTTPS）
git clone https://gitee.com/y_project/RuoYi-Cloud.git

# 进入克隆的目录
cd RuoYi-Cloud

# 切换到 Spring Boot 3 分支
git checkout springboot3

# 查看当前分支和最新提交
git branch
git log -1
```

### 2.4 移动文件到 code 根目录

```bash
# 当前位置：code/RuoYi-Cloud/

# 移动所有文件到上级目录（code/）
# Windows PowerShell
Move-Item -Path * -Destination ../ -Force

# 或者 Git Bash
mv * ../

# 返回 code 目录
cd ..

# 删除空的 RuoYi-Cloud 目录
Remove-Item -Path RuoYi-Cloud -Recurse -Force
# 或
rmdir RuoYi-Cloud

# 验证文件结构
ls
```

**预期目录结构：**

```
code/
├── pom.xml
├── ruoyi-gateway/
├── ruoyi-auth/
├── ruoyi-modules/
├── ruoyi-common/
├── ruoyi-api/
├── sql/
└── ...
```

### 2.5 清除 Gitee 的 Git 信息

```bash
# 当前位置：code/

# 删除 RuoYi-Cloud 的 .git 目录
Remove-Item -Path .git -Recurse -Force
# 或
rm -rf .git

# 删除 RuoYi-Cloud 的 .gitignore（可选，也可以保留并修改）
Remove-Item -Path .gitignore -Force

# 返回项目根目录
cd ..
```

### 2.6 提交初始化代码到我们的仓库

```bash
# 当前位置：项目根目录

# 查看状态（应该看到 code 目录下的所有文件都被修改）
git status

# 添加所有文件
git add .

# 提交（使用清晰的提交信息）
git commit -m "feat: 使用 RuoYi-Cloud Spring Boot 3 分支初始化 ruoyi-framework

- 克隆自 https://gitee.com/y_project/RuoYi-Cloud.git (springboot3 分支)
- 清除原有 Git 信息
- 后续将基于此代码实现双端分离架构"

# 推送到远程仓库
git push origin ruoyi-framework
```

---

## 三、项目基础配置调整

### 3.1 修改父 POM 配置

**编辑 `code/pom.xml`：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <!-- ⭐ 修改 GroupId 和 ArtifactId -->
    <groupId>com.sia</groupId>
    <artifactId>problem-to-hero-backend</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>

    <!-- ⭐ 修改项目名称和描述 -->
    <name>Problem to Hero Backend</name>
    <description>问答社区微服务平台（基于 RuoYi-Cloud）</description>

    <properties>
        <!-- ⭐ 确认版本号 -->
        <java.version>21</java.version>
        <spring-boot.version>3.2.5</spring-boot.version>
        <spring-cloud.version>2023.0.1</spring-cloud.version>
        <spring-cloud-alibaba.version>2023.0.1.1</spring-cloud-alibaba.version>
        <!-- 保留其他 RuoYi 的配置 -->
    </properties>

    <modules>
        <module>ruoyi-auth</module>
        <module>ruoyi-gateway</module>
        <module>ruoyi-common</module>
        <module>ruoyi-api</module>
        <module>ruoyi-modules</module>
    </modules>

    <!-- 保留 dependencyManagement -->
</project>
```

### 3.2 修改子模块 POM

需要修改所有子模块的 `<parent>` 部分：

```xml
<!-- 所有子模块的 pom.xml -->
<parent>
    <groupId>com.sia</groupId>
    <artifactId>problem-to-hero-backend</artifactId>
    <version>1.0.0</version>
</parent>
```

**批量修改命令（PowerShell）：**

```powershell
# 在 code 目录下执行
Get-ChildItem -Recurse -Filter pom.xml | ForEach-Object {
    (Get-Content $_.FullName) -replace '<groupId>com.ruoyi</groupId>', '<groupId>com.sia</groupId>' | Set-Content $_.FullName
}
```

### 3.3 修改包名（关键步骤）

**方案 A：使用 IDE 批量重构（推荐）**

1. 用 IntelliJ IDEA 打开 `code/pom.xml`
2. 等待 Maven 依赖下载完成
3. 右键 `src/main/java/com/ruoyi` 目录
4. 选择 `Refactor` → `Rename`
5. 输入新包名：`com.sia`
6. 勾选 `Search in comments and strings`
7. 点击 `Refactor`

**方案 B：手动修改（适合小范围调整）**

手动修改需要调整的文件类型：
- Java 文件的 `package` 声明
- MyBatis XML 的 namespace
- `application.yml` 中的包扫描路径
- 配置类中的包引用

### 3.4 提交 POM 和包名修改

```bash
# 查看修改
git status
git diff code/pom.xml

# 提交
git add .
git commit -m "chore: 修改项目 GroupId 和包名

- GroupId: com.ruoyi → com.sia
- 包名: com.ruoyi → com.sia
- 项目名称: RuoYi-Cloud → Problem to Hero Backend"

git push origin ruoyi-framework
```

---

## 四、数据库初始化

### 4.1 导入 RuoYi 基础表

```bash
# 进入项目根目录
cd e:\github\sia\Problem_to_Hero_backend

# 找到 RuoYi 的 SQL 文件
# code/sql/ry_cloud.sql 或 code/sql/ry_20xx.sql
```

**执行 SQL：**

```sql
-- 1. 连接到 MySQL
mysql -h localhost -P 3306 -u root -p

-- 2. 创建数据库
CREATE DATABASE IF NOT EXISTS `ry_cloud` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `ry_cloud`;

-- 3. 导入 RuoYi 基础表
SOURCE e:/github/sia/Problem_to_Hero_backend/code/sql/ry_cloud.sql;

-- 4. 验证表是否创建成功
SHOW TABLES;

-- 应包含：
-- sys_user（管理员表）
-- sys_role（角色表）
-- sys_menu（菜单表）
-- sys_dept（部门表）
-- gen_table（代码生成表）
-- 等...

-- 5. 验证默认管理员账号
SELECT user_id, user_name, nick_name, status FROM sys_user WHERE user_name = 'admin';
```

### 4.2 创建业务用户表（app_user）

**创建 SQL 文件：`sql/app_user.sql`**

```sql
-- 业务用户表（与 sys_user 完全分离）
CREATE TABLE `app_user` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(30) NOT NULL UNIQUE COMMENT '用户名',
  `nickname` VARCHAR(30) NOT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
  
  -- 问答社区特有字段
  `points` INT DEFAULT 0 COMMENT '积分',
  `level` INT DEFAULT 1 COMMENT '等级（1-10级）',
  `reputation` INT DEFAULT 0 COMMENT '声望值',
  `question_count` INT DEFAULT 0 COMMENT '提问数',
  `answer_count` INT DEFAULT 0 COMMENT '回答数',
  `best_answer_count` INT DEFAULT 0 COMMENT '最佳答案数',
  `accepted_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '采纳率（%）',
  
  -- 个人信息
  `bio` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
  `company` VARCHAR(100) DEFAULT NULL COMMENT '公司',
  `position` VARCHAR(50) DEFAULT NULL COMMENT '职位',
  `location` VARCHAR(100) DEFAULT NULL COMMENT '所在地',
  `github_url` VARCHAR(255) DEFAULT NULL COMMENT 'GitHub地址',
  `blog_url` VARCHAR(255) DEFAULT NULL COMMENT '博客地址',
  `website` VARCHAR(255) DEFAULT NULL COMMENT '个人网站',
  
  -- 社交统计
  `follower_count` INT DEFAULT 0 COMMENT '粉丝数',
  `following_count` INT DEFAULT 0 COMMENT '关注数',
  `view_count` INT DEFAULT 0 COMMENT '主页浏览次数',
  
  -- 状态字段
  `status` CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `login_ip` VARCHAR(128) DEFAULT NULL COMMENT '最后登录IP',
  `login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`),
  INDEX `idx_create_time` (`create_time`),
  INDEX `idx_level` (`level`),
  INDEX `idx_reputation` (`reputation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务用户表';

-- 初始化测试用户（密码：admin123）
INSERT INTO `app_user` (`username`, `nickname`, `email`, `phone`, `password`, `bio`) VALUES
('testuser', '测试用户', 'test@example.com', '13800138000', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/T/hD.7ZGwT6', '这是一个测试用户'),
('alice', 'Alice', 'alice@example.com', '13800138001', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/T/hD.7ZGwT6', 'Java 开发者'),
('bob', 'Bob', 'bob@example.com', '13800138002', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/T/hD.7ZGwT6', 'Python 爱好者');
```

**执行 SQL：**

```bash
mysql -h localhost -P 3306 -u root -p ry_cloud < sql/app_user.sql
```

### 4.3 创建问答业务表

**创建 SQL 文件：`sql/qa_tables.sql`**

```sql
USE `ry_cloud`;

-- 问题表
CREATE TABLE `qa_question` (
  `question_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '问题ID',
  `user_id` BIGINT NOT NULL COMMENT '提问用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '问题标题',
  `content` TEXT NOT NULL COMMENT '问题内容（Markdown）',
  `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签（JSON数组）',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `answer_count` INT DEFAULT 0 COMMENT '回答数',
  `vote_count` INT DEFAULT 0 COMMENT '投票数',
  `is_solved` TINYINT(1) DEFAULT 0 COMMENT '是否已解决',
  `best_answer_id` BIGINT DEFAULT NULL COMMENT '最佳答案ID',
  `status` CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1关闭 2审核中）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`question_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_create_time` (`create_time`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题表';

-- 答案表
CREATE TABLE `qa_answer` (
  `answer_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '答案ID',
  `question_id` BIGINT NOT NULL COMMENT '问题ID',
  `user_id` BIGINT NOT NULL COMMENT '回答用户ID',
  `content` TEXT NOT NULL COMMENT '答案内容（Markdown）',
  `vote_count` INT DEFAULT 0 COMMENT '投票数',
  `is_best` TINYINT(1) DEFAULT 0 COMMENT '是否为最佳答案',
  `status` CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1删除）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`answer_id`),
  INDEX `idx_question_id` (`question_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答案表';

-- 评论表
CREATE TABLE `qa_comment` (
  `comment_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `target_type` TINYINT NOT NULL COMMENT '评论目标类型（1问题 2答案）',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
  `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
  `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID（回复）',
  `status` CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1删除）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`comment_id`),
  INDEX `idx_target` (`target_type`, `target_id`),
  INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 标签表
CREATE TABLE `qa_tag` (
  `tag_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
  `tag_desc` VARCHAR(200) DEFAULT NULL COMMENT '标签描述',
  `question_count` INT DEFAULT 0 COMMENT '问题数量',
  `follower_count` INT DEFAULT 0 COMMENT '关注数',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `uk_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';
```

**执行 SQL：**

```bash
mysql -h localhost -P 3306 -u root -p ry_cloud < sql/qa_tables.sql
```

### 4.4 提交数据库脚本

```bash
git add sql/
git commit -m "feat: 添加业务用户表和问答业务表

- app_user: 业务用户表
- qa_question: 问题表
- qa_answer: 答案表
- qa_comment: 评论表
- qa_tag: 标签表"

git push origin ruoyi-framework
```

---

## 五、双端架构实施

### 5.1 创建业务端认证服务（ruoyi-auth-app）

#### 5.1.1 创建模块目录

```bash
cd code/ruoyi-modules

# 创建目录结构
mkdir -p ruoyi-auth-app/src/main/java/com/sia/auth/app/controller
mkdir -p ruoyi-auth-app/src/main/java/com/sia/auth/app/service
mkdir -p ruoyi-auth-app/src/main/java/com/sia/auth/app/domain
mkdir -p ruoyi-auth-app/src/main/java/com/sia/auth/app/mapper
mkdir -p ruoyi-auth-app/src/main/resources/mapper
```

#### 5.1.2 创建 POM 文件

**文件：`code/ruoyi-modules/ruoyi-auth-app/pom.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <parent>
        <artifactId>ruoyi-modules</artifactId>
        <groupId>com.sia</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>ruoyi-auth-app</artifactId>
    <description>业务端认证服务（C端用户登录注册）</description>

    <dependencies>
        <!-- Spring Cloud Bootstrap -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
        </dependency>

        <!-- Nacos Discovery -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <!-- Nacos Config -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>

        <!-- RuoYi Common Core -->
        <dependency>
            <groupId>com.sia</groupId>
            <artifactId>ruoyi-common-core</artifactId>
        </dependency>

        <!-- RuoYi Common Security -->
        <dependency>
            <groupId>com.sia</groupId>
            <artifactId>ruoyi-common-security</artifactId>
        </dependency>

        <!-- RuoYi Common Redis -->
        <dependency>
            <groupId>com.sia</groupId>
            <artifactId>ruoyi-common-redis</artifactId>
        </dependency>

        <!-- RuoYi Common DataSource -->
        <dependency>
            <groupId>com.sia</groupId>
            <artifactId>ruoyi-common-datasource</artifactId>
        </dependency>

        <!-- MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency>

        <!-- MyBatis Plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>

        <!-- BCrypt 密码加密 -->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-crypto</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 5.1.3 创建启动类

**文件：`code/ruoyi-modules/ruoyi-auth-app/src/main/java/com/sia/auth/app/AuthAppApplication.java`**

```java
package com.sia.auth.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 业务端认证服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthAppApplication.java, args);
        System.out.println("(♥◠‿◠)ノﾞ  业务端认证服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
```

#### 5.1.4 创建配置文件

**文件：`code/ruoyi-modules/ruoyi-auth-app/src/main/resources/bootstrap.yml`**

```yaml
server:
  port: 9201

spring:
  application:
    name: ruoyi-auth-app
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
        namespace: ${NACOS_NAMESPACE:dev}
        group: DEFAULT_GROUP
      config:
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
        namespace: ${NACOS_NAMESPACE:dev}
        file-extension: yml
        group: DEFAULT_GROUP
```

**文件：`code/ruoyi-modules/ruoyi-auth-app/src/main/resources/application.yml`**

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ry_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: root123456

mybatis-plus:
  mapper-locations: classpath*:mapper/**/*.xml
  type-aliases-package: com.sia.auth.app.domain
  configuration:
    map-underscore-to-camel-case: true

# Redis 配置
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
```

#### 5.1.5 创建实体类

**文件：`code/ruoyi-modules/ruoyi-auth-app/src/main/java/com/sia/auth/app/domain/AppUser.java`**

```java
package com.sia.auth.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 业务用户实体
 */
@Data
@TableName("app_user")
public class AppUser {
    
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private String password;
    
    // 问答社区字段
    private Integer points;
    private Integer level;
    private Integer reputation;
    private Integer questionCount;
    private Integer answerCount;
    private Integer bestAnswerCount;
    
    // 个人信息
    private String bio;
    private String company;
    private String position;
    private String location;
    private String githubUrl;
    private String blogUrl;
    
    // 状态字段
    private String status;
    private String delFlag;
    private String loginIp;
    private LocalDateTime loginTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

#### 5.1.6 创建 Mapper

**文件：`code/ruoyi-modules/ruoyi-auth-app/src/main/java/com/sia/auth/app/mapper/AppUserMapper.java`**

```java
package com.sia.auth.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sia.auth.app.domain.AppUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务用户 Mapper
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {
}
```

#### 5.1.7 创建 Service

**文件：`code/ruoyi-modules/ruoyi-auth-app/src/main/java/com/sia/auth/app/service/AppAuthService.java`**

```java
package com.sia.auth.app.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sia.auth.app.domain.AppUser;
import com.sia.auth.app.mapper.AppUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 业务端认证服务
 */
@Slf4j
@Service
public class AppAuthService {
    
    @Autowired
    private AppUserMapper appUserMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String APP_TOKEN_PREFIX = "app:token:";
    private static final int TOKEN_EXPIRE_SECONDS = 7200; // 2小时
    
    /**
     * 用户登录
     */
    public String login(String username, String password) {
        // 1. 查询用户
        LambdaQueryWrapper<AppUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppUser::getUsername, username);
        AppUser user = appUserMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 2. 验证密码
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 3. 检查账号状态
        if ("1".equals(user.getStatus())) {
            throw new RuntimeException("账号已被停用");
        }
        
        // 4. 生成 Token
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 5. 存储到 Redis
        String redisKey = APP_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(redisKey, user, TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
        
        // 6. 更新登录时间
        user.setLoginTime(LocalDateTime.now());
        appUserMapper.updateById(user);
        
        log.info("用户登录成功: {}", username);
        return token;
    }
    
    /**
     * 用户注册
     */
    public void register(String username, String nickname, String password, String email, String phone) {
        // 1. 检查用户名是否存在
        long count = appUserMapper.selectCount(
            new LambdaQueryWrapper<AppUser>().eq(AppUser::getUsername, username)
        );
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 2. 创建用户
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setStatus("0");
        user.setPoints(0);
        user.setLevel(1);
        user.setReputation(0);
        
        appUserMapper.insert(user);
        log.info("用户注册成功: {}", username);
    }
    
    /**
     * 登出
     */
    public void logout(String token) {
        String redisKey = APP_TOKEN_PREFIX + token;
        redisTemplate.delete(redisKey);
        log.info("用户登出成功");
    }
    
    /**
     * 获取用户信息
     */
    public AppUser getUserInfo(String token) {
        String redisKey = APP_TOKEN_PREFIX + token;
        return (AppUser) redisTemplate.opsForValue().get(redisKey);
    }
}
```

#### 5.1.8 创建 Controller

**文件：`code/ruoyi-modules/ruoyi-auth-app/src/main/java/com/sia/auth/app/controller/AppAuthController.java`**

```java
package com.sia.auth.app.controller;

import com.sia.auth.app.domain.AppUser;
import com.sia.auth.app.service.AppAuthService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务端认证 Controller
 * 路径：/app/api/auth/** （网关会去掉 /app/api）
 */
@RestController
@RequestMapping("/auth")
public class AppAuthController {
    
    @Autowired
    private AppAuthService appAuthService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDTO loginDTO) {
        String token = appAuthService.login(loginDTO.getUsername(), loginDTO.getPassword());
        AppUser user = appAuthService.getUserInfo(token);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "登录成功");
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", user);
        data.put("expiresIn", 7200);
        result.put("data", data);
        
        return result;
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterDTO registerDTO) {
        appAuthService.register(
            registerDTO.getUsername(),
            registerDTO.getNickname(),
            registerDTO.getPassword(),
            registerDTO.getEmail(),
            registerDTO.getPhone()
        );
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "注册成功");
        return result;
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestHeader("App-Token") String token) {
        appAuthService.logout(token);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "登出成功");
        return result;
    }
    
    @Data
    static class LoginDTO {
        private String username;
        private String password;
    }
    
    @Data
    static class RegisterDTO {
        private String username;
        private String nickname;
        private String password;
        private String email;
        private String phone;
    }
}
```

#### 5.1.9 更新父模块 POM

**编辑 `code/ruoyi-modules/pom.xml`，添加新模块：**

```xml
<modules>
    <module>ruoyi-system</module>
    <module>ruoyi-gen</module>
    <module>ruoyi-job</module>
    <module>ruoyi-file</module>
    <module>ruoyi-auth-app</module>  <!-- ⭐ 新增 -->
</modules>
```

### 5.2 配置网关双路由

#### 5.2.1 修改网关配置

**编辑 `code/ruoyi-gateway/src/main/resources/application.yml`：**

```yaml
spring:
  cloud:
    gateway:
      routes:
        # ==================== 管理端路由 ====================
        
        # 管理端认证
        - id: admin-auth
          uri: lb://ruoyi-auth
          predicates:
            - Path=/admin/api/auth/**
          filters:
            - StripPrefix=2

        # 管理端系统服务
        - id: admin-system
          uri: lb://ruoyi-system
          predicates:
            - Path=/admin/api/system/**
          filters:
            - StripPrefix=2

        # 管理端代码生成
        - id: admin-gen
          uri: lb://ruoyi-gen
          predicates:
            - Path=/admin/api/code/**
          filters:
            - StripPrefix=2

        # ==================== 业务端路由 ====================
        
        # 业务端认证
        - id: app-auth
          uri: lb://ruoyi-auth-app
          predicates:
            - Path=/app/api/auth/**
          filters:
            - StripPrefix=2
```

#### 5.2.2 创建双鉴权过滤器

**文件：`code/ruoyi-gateway/src/main/java/com/sia/gateway/filter/AdminAuthFilter.java`**

```java
package com.sia.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 管理端鉴权过滤器
 */
@Component
public class AdminAuthFilter implements GlobalFilter, Ordered {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String ADMIN_TOKEN_PREFIX = "admin:token:";
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // 只处理管理端路径
        if (!path.startsWith("/admin/api/")) {
            return chain.filter(exchange);
        }
        
        // 登录接口放行
        if (path.contains("/auth/login")) {
            return chain.filter(exchange);
        }
        
        // 验证 Token
        String token = exchange.getRequest().getHeaders().getFirst("Admin-Token");
        if (!StringUtils.hasText(token)) {
            return unauthorizedResponse(exchange, "未提供管理员Token");
        }
        
        String redisKey = ADMIN_TOKEN_PREFIX + token;
        Object userObj = redisTemplate.opsForValue().get(redisKey);
        if (userObj == null) {
            return unauthorizedResponse(exchange, "Token已过期或无效");
        }
        
        // 传递用户信息到下游
        ServerHttpRequest request = exchange.getRequest().mutate()
            .header("X-User-Type", "admin")
            .build();
        
        return chain.filter(exchange.mutate().request(request).build());
    }
    
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        String body = String.format("{\"code\":401,\"msg\":\"%s\"}", message);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    
    @Override
    public int getOrder() {
        return -100;
    }
}
```

**文件：`code/ruoyi-gateway/src/main/java/com/sia/gateway/filter/AppAuthFilter.java`**

```java
package com.sia.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 业务端鉴权过滤器
 */
@Component
public class AppAuthFilter implements GlobalFilter, Ordered {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String APP_TOKEN_PREFIX = "app:token:";
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // 只处理业务端路径
        if (!path.startsWith("/app/api/")) {
            return chain.filter(exchange);
        }
        
        // 登录、注册接口放行
        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            return chain.filter(exchange);
        }
        
        // 验证 Token
        String token = exchange.getRequest().getHeaders().getFirst("App-Token");
        if (!StringUtils.hasText(token)) {
            return unauthorizedResponse(exchange, "请先登录");
        }
        
        String redisKey = APP_TOKEN_PREFIX + token;
        Object userObj = redisTemplate.opsForValue().get(redisKey);
        if (userObj == null) {
            return unauthorizedResponse(exchange, "登录已过期，请重新登录");
        }
        
        // 传递用户信息到下游
        ServerHttpRequest request = exchange.getRequest().mutate()
            .header("X-User-Type", "app")
            .build();
        
        return chain.filter(exchange.mutate().request(request).build());
    }
    
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        String body = String.format("{\"code\":401,\"msg\":\"%s\"}", message);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    
    @Override
    public int getOrder() {
        return -100;
    }
}
```

### 5.3 提交双端架构代码

```bash
git add .
git commit -m "feat: 实现双端分离架构

- 新增 ruoyi-auth-app 业务端认证服务
- 创建 app_user 业务用户表
- 配置网关双路由（/admin/api、/app/api）
- 实现双鉴权过滤器（AdminAuthFilter、AppAuthFilter）
- 支持双 Token 策略（Admin-Token、App-Token）"

git push origin ruoyi-framework
```

---

## 六、服务启动与验证

### 6.1 启动顺序

```bash
# 1. 确保基础服务已启动（Nacos、MySQL、Redis）
docker-compose ps

# 2. 进入 code 目录
cd code

# 3. 编译整个项目
mvn clean install -DskipTests

# 4. 按顺序启动服务
# 4.1 启动网关
cd ruoyi-gateway
mvn spring-boot:run

# 4.2 启动管理端认证
cd ../ruoyi-auth
mvn spring-boot:run

# 4.3 启动业务端认证
cd ../ruoyi-modules/ruoyi-auth-app
mvn spring-boot:run

# 4.4 启动系统服务
cd ../ruoyi-system
mvn spring-boot:run
```

### 6.2 验证服务注册

访问 Nacos 控制台：http://localhost:8848/nacos (nacos/nacos)

检查服务列表：
- ✅ ruoyi-gateway
- ✅ ruoyi-auth（管理端认证）
- ✅ ruoyi-auth-app（业务端认证）
- ✅ ruoyi-system

### 6.3 测试管理端接口

```bash
# 管理员登录
curl -X POST http://localhost:8080/admin/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# 响应示例：
# {"code":200,"msg":"操作成功","data":{"access_token":"xxx","expires_in":7200}}
```

### 6.4 测试业务端接口

```bash
# 用户注册
curl -X POST http://localhost:8080/app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "nickname": "测试用户",
    "password": "Test123456",
    "email": "test@example.com",
    "phone": "13800138000"
  }'

# 用户登录
curl -X POST http://localhost:8080/app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test123456"
  }'

# 响应示例：
# {"code":200,"msg":"登录成功","data":{"token":"xyz789...","userInfo":{...},"expiresIn":7200}}
```

### 6.5 验证双端隔离

```bash
# 用管理员 Token 访问业务端接口（应该失败）
curl -X POST http://localhost:8080/app/api/content/question \
  -H "Admin-Token: abc123..."

# 预期响应：401 Unauthorized

# 用用户 Token 访问管理端接口（应该失败）
curl -X GET http://localhost:8080/admin/api/system/user/list \
  -H "App-Token: xyz789..."

# 预期响应：401 Unauthorized
```

---

## 七、后续开发指南

### 7.1 创建业务服务（以 content-service 为例）

```bash
# 1. 创建模块
cd code/ruoyi-modules
mkdir -p ruoyi-content/src/main/java/com/sia/content/controller/admin
mkdir -p ruoyi-content/src/main/java/com/sia/content/controller/app

# 2. 使用代码生成器
# 访问：http://localhost:8083/code
# 导入 qa_question、qa_answer 表
# 生成代码

# 3. 区分管理端和业务端 Controller
# - controller/admin/：管理端接口（问题审核）
# - controller/app/：业务端接口（问题发布）
```

### 7.2 网关路由配置规范

每个新服务都需要在网关配置双路由：

```yaml
# 管理端路由
- id: admin-content
  uri: lb://ruoyi-content
  predicates:
    - Path=/admin/api/content/**
  filters:
    - StripPrefix=2

# 业务端路由
- id: app-content
  uri: lb://ruoyi-content
  predicates:
    - Path=/app/api/content/**
  filters:
    - StripPrefix=2
```

### 7.3 开发规范

| 规范项 | 说明 |
|-------|------|
| **包名** | com.sia.{module} |
| **API 路径** | 管理端：/admin/api/{module}/**<br>业务端：/app/api/{module}/** |
| **Token 验证** | 管理端：Admin-Token<br>业务端：App-Token |
| **响应格式** | 统一使用 Result<T> |
| **异常处理** | 全局异常处理器 |

---

## 八、常见问题

### Q1: Maven 编译失败？

**解决方案：**
```bash
# 清理并重新编译
mvn clean install -DskipTests

# 如果依赖下载失败，检查 Maven 镜像配置
cat ~/.m2/settings.xml
```

### Q2: 服务启动失败？

**检查清单：**
1. Nacos 是否正常运行？
2. MySQL 数据库是否可连接？
3. Redis 是否正常运行？
4. 端口是否被占用？

### Q3: 跨端访问没有返回 401？

**原因：**
- 鉴权过滤器可能没有生效

**解决方案：**
```java
// 检查 @Component 注解是否添加
@Component
public class AdminAuthFilter implements GlobalFilter, Ordered {
    // ...
}
```

---

## 九、总结

### 完成的工作

- ✅ 基于 Git 分支管理，创建 ruoyi-framework 分支
- ✅ 使用 RuoYi-Cloud Spring Boot 3 代码初始化项目
- ✅ 修改包名、POM 配置
- ✅ 创建双用户表（sys_user、app_user）
- ✅ 实现双认证服务（ruoyi-auth、ruoyi-auth-app）
- ✅ 配置网关双路由（/admin/api、/app/api）
- ✅ 实现双鉴权过滤器（AdminAuthFilter、AppAuthFilter）
- ✅ 验证双端隔离

### 后续任务

- [ ] 使用代码生成器开发问答业务模块
- [ ] 开发其他业务服务（activity、wallet、search、notify、interaction）
- [ ] 集成前端（管理端 Vue3、业务端前端）
- [ ] 性能优化和压力测试
- [ ] 部署到生产环境

---

**文档版本：** v1.0  
**最后更新：** 2026-02-04  
**维护人：** 开发团队
