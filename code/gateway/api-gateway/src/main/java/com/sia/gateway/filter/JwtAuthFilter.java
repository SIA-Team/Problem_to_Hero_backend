package com.sia.gateway.filter;

import com.sia.common.security.jwt.JwtClaims;
import com.sia.common.security.jwt.JwtProperties;
import com.sia.common.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * JWT认证过滤器
 * 全局过滤器，用于验证JWT Token
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 白名单路径（不需要认证的路径）
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/api/user/login",
            "/api/user/register",
            "/api/user/refresh",
            "/actuator/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 检查是否为白名单路径
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 获取Token
        String token = getToken(request);
        if (!StringUtils.hasText(token)) {
            return unauthorized(exchange.getResponse(), "Token不能为空");
        }

        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            return unauthorized(exchange.getResponse(), "Token无效或已过期");
        }

        // 提取用户信息并添加到请求头
        try {
            JwtClaims jwtClaims = jwtUtil.extractClaims(token);
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(jwtClaims.getUserId()))
                    .header("X-Username", jwtClaims.getUsername())
                    .header("X-Login-Type", jwtClaims.getLoginType())
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } catch (Exception e) {
            log.error("解析Token失败: {}", e.getMessage());
            return unauthorized(exchange.getResponse(), "Token解析失败");
        }
    }

    /**
     * 检查是否为白名单路径
     *
     * @param path 请求路径
     * @return 是否为白名单
     */
    private boolean isWhiteList(String path) {
        return WHITE_LIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * 从请求头中获取Token
     *
     * @param request 请求
     * @return Token
     */
    private String getToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(jwtProperties.getTokenHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getTokenPrefix())) {
            return bearerToken.substring(jwtProperties.getTokenPrefix().length());
        }
        return null;
    }

    /**
     * 返回未授权响应
     *
     * @param response 响应
     * @param message  错误消息
     * @return Mono
     */
    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -100; // 优先级较高，在其他过滤器之前执行
    }
}
