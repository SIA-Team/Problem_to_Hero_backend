package com.sia.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 提供Token生成、验证、解析等功能
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    /**
     * 生成Token
     *
     * @param jwtClaims JWT Claims
     * @return Token字符串
     */
    public String generateToken(JwtClaims jwtClaims) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", jwtClaims.getUserId());
        claims.put("username", jwtClaims.getUsername());
        claims.put("loginType", jwtClaims.getLoginType());
        if (jwtClaims.getThirdPartyId() != null) {
            claims.put("thirdPartyId", jwtClaims.getThirdPartyId());
        }

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 生成Refresh Token
     *
     * @param jwtClaims JWT Claims
     * @return Refresh Token字符串
     */
    public String generateRefreshToken(JwtClaims jwtClaims) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", jwtClaims.getUserId());
        claims.put("type", "refresh");

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getRefreshExpiration());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 验证Token
     *
     * @param token Token字符串
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 解析Token获取Claims
     *
     * @param token Token字符串
     * @return Claims对象
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从Token中提取JwtClaims
     *
     * @param token Token字符串
     * @return JwtClaims对象
     */
    public JwtClaims extractClaims(String token) {
        Claims claims = parseToken(token);
        return JwtClaims.builder()
                .userId(claims.get("userId", Long.class))
                .username(claims.get("username", String.class))
                .loginType(claims.get("loginType", String.class))
                .thirdPartyId(claims.get("thirdPartyId", String.class))
                .build();
    }

    /**
     * 从Token中提取用户ID
     *
     * @param token Token字符串
     * @return 用户ID
     */
    public Long extractUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 检查Token是否过期
     *
     * @param token Token字符串
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取SecretKey
     *
     * @return SecretKey
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
