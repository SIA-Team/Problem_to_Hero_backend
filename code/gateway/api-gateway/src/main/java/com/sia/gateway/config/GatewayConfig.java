package com.sia.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置类
 * 配置路由规则
 * 
 * 注意：路由配置主要在 application-dev_template.yml 中通过配置文件方式配置
 * 此处的 Java 配置作为备用方案，如果配置文件中的路由配置不生效，可以使用此配置
 * 
 * @author sia
 * @date 2026/02/02
 */
@Configuration
public class GatewayConfig {

    /**
     * 路由配置（备用方案）
     * 
     * 主要路由配置在 application-dev_template.yml 中
     * 启用服务发现后，网关会自动从 Nacos 发现服务并路由
     * 
     * @param builder RouteLocatorBuilder
     * @return RouteLocator
     */
    // @Bean  // 注释掉，使用配置文件中的路由配置
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 用户服务路由
                .route("user-service", r -> r
                        .path("/api/user/**")
                        .uri("lb://user-service"))
                // 内容服务路由
                .route("content-service", r -> r
                        .path("/api/content/**")
                        .uri("lb://content-service"))
                // 活动服务路由
                .route("activity-service", r -> r
                        .path("/api/activity/**")
                        .uri("lb://activity-service"))
                // 钱包服务路由
                .route("wallet-service", r -> r
                        .path("/api/wallet/**")
                        .uri("lb://wallet-service"))
                // 搜索服务路由
                .route("search-service", r -> r
                        .path("/api/search/**")
                        .uri("lb://search-service"))
                // 通知服务路由
                .route("notify-service", r -> r
                        .path("/api/notify/**")
                        .uri("lb://notify-service"))
                // 互动服务路由
                .route("interaction-service", r -> r
                        .path("/api/interaction/**")
                        .uri("lb://interaction-service"))
                .build();
    }
}
