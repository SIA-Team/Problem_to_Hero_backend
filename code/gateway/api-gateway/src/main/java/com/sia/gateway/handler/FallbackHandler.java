package com.sia.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务降级处理器
 * 当后端服务不可用时，返回降级响应
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Component
public class FallbackHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 服务降级处理
     *
     * @param exchange ServerWebExchange
     * @param message  降级消息
     * @return Mono
     */
    public Mono<Void> fallback(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("message", message != null ? message : "服务暂时不可用，请稍后重试");

        try {
            String json = objectMapper.writeValueAsString(result);
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("序列化降级响应失败", e);
            return Mono.error(e);
        }
    }
}
