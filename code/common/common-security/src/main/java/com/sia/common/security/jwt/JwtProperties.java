package com.sia.common.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT配置属性
 *
 * @author sia
 * @date 2026/02/02
 */
@Data
@ConfigurationProperties(prefix = "sia.jwt")
public class JwtProperties {

    /**
     * JWT密钥
     */
    private String secret = "sia-problem-to-hero-backend-jwt-secret-key-2026";

    /**
     * Token过期时间（毫秒），默认24小时
     */
    private Long expiration = 24 * 60 * 60 * 1000L;

    /**
     * Refresh Token过期时间（毫秒），默认7天
     */
    private Long refreshExpiration = 7 * 24 * 60 * 60 * 1000L;

    /**
     * Token前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Token请求头名称
     */
    private String tokenHeader = "Authorization";
}
