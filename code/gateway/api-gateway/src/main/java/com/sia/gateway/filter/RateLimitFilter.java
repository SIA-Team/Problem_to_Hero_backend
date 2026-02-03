package com.sia.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;

/**
 * 限流过滤器
 * 基于Redis实现限流
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    // 限流配置：每秒允许100个请求
    private static final int RATE_LIMIT = 100;
    private static final Duration WINDOW = Duration.ofSeconds(1);

    public RateLimitFilter(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String key = getRateLimitKey(exchange);
        String limitKey = "rate_limit:" + key;

        // 使用Redis Lua脚本实现滑动窗口限流
        String luaScript = "local current = redis.call('incr', KEYS[1]) " +
                "if current == 1 then " +
                "redis.call('expire', KEYS[1], ARGV[1]) " +
                "end " +
                "return current";

        return redisTemplate.execute(RedisScript.of(luaScript, Long.class),
                        Collections.singletonList(limitKey),
                        Collections.singletonList(String.valueOf(WINDOW.getSeconds())))
                )
                .flatMap(count -> {
                    if (count > RATE_LIMIT) {
                        log.warn("限流触发: key={}, count={}", key, count);
                        return tooManyRequests(exchange.getResponse());
                    }
                    return chain.filter(exchange);
                })
                .onErrorResume(e -> {
                    log.error("限流检查失败: {}", e.getMessage());
                    // 限流检查失败时，允许请求通过（降级策略）
                    return chain.filter(exchange);
                });
    }

    /**
     * 获取限流键
     *
     * @param exchange ServerWebExchange
     * @return 限流键
     */
    private String getRateLimitKey(ServerWebExchange exchange) {
        String ip = exchange.getRequest().getRemoteAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown";
        String path = exchange.getRequest().getURI().getPath();
        return ip + ":" + path;
    }

    /**
     * 返回429 Too Many Requests响应
     *
     * @param response 响应
     * @return Mono
     */
    private Mono<Void> tooManyRequests(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String body = "{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\"}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -50; // 在JWT认证之后执行
    }
}
