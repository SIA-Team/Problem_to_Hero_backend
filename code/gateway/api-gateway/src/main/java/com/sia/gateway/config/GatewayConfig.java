package com.sia.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置类
 * 配置路由规则
 *
 * @author sia
 * @date 2026/02/02
 */
@Configuration
public class GatewayConfig {

    /**
     * 路由配置
     *
     * @param builder RouteLocatorBuilder
     * @return RouteLocator
     */
    @Bean
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
