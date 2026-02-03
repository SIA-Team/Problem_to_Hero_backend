package com.sia.common.dbservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据源配置类
 * 配置HikariCP连接池
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    /**
     * 数据源配置属性
     *
     * @return DataSourceProperties
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * HikariCP数据源配置
     *
     * @param properties 数据源属性
     * @return DataSource
     */
    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setDriverClassName(properties.getDriverClassName());

        // HikariCP连接池配置
        config.setMinimumIdle(5); // 最小空闲连接数
        config.setMaximumPoolSize(20); // 最大连接数
        config.setConnectionTimeout(30000); // 连接超时时间（毫秒）
        config.setIdleTimeout(600000); // 空闲连接超时时间（毫秒）
        config.setMaxLifetime(1800000); // 连接最大生命周期（毫秒）
        config.setLeakDetectionThreshold(60000); // 连接泄漏检测阈值（毫秒）

        // 连接池名称
        config.setPoolName("HikariCP-Pool");

        log.info("HikariCP数据源配置完成: {}", properties.getUrl());
        return new HikariDataSource(config);
    }
}
