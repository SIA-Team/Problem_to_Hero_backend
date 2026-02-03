package com.sia.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 限流配置
 *
 * @author sia
 * @date 2026/02/02
 */
@Configuration
public class RateLimitConfig {

    /**
     * 限流键解析器
     * 根据用户IP进行限流
     *
     * @return KeyResolver
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";
            return Mono.just(ip);
        };
    }

    /**
     * 根据用户ID进行限流
     * 需要从JWT Token中提取用户ID
     *
     * @return KeyResolver
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            // 从请求头中获取用户ID（由JWT过滤器设置）
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            return Mono.just(userId != null ? userId : "anonymous");
        };
    }
}
