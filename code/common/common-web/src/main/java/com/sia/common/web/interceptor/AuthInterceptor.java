package com.sia.common.web.interceptor;

import com.sia.common.core.enums.ResultCodeEnum;
import com.sia.common.core.exception.BaseException;
import com.sia.common.security.jwt.JwtProperties;
import com.sia.common.security.jwt.JwtUtil;
import com.sia.common.web.annotation.NoAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 认证拦截器
 * 验证JWT Token，提取用户信息
 * 注意：此拦截器为通用实现，各服务可根据需要自定义
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是Controller方法，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        // 检查是否有@NoAuth注解，如果有则跳过认证
        if (handlerMethod.hasMethodAnnotation(NoAuth.class) ||
            handlerMethod.getBeanType().isAnnotationPresent(NoAuth.class)) {
            return true;
        }

        // 从请求头获取Token
        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            throw new BaseException(ResultCodeEnum.TOKEN_MISSING);
        }

        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            throw new BaseException(ResultCodeEnum.TOKEN_INVALID);
        }

        // 将用户信息存储到请求属性中，供后续使用
        try {
            Long userId = jwtUtil.extractUserId(token);
            request.setAttribute("userId", userId);
            request.setAttribute("token", token);
        } catch (Exception e) {
            log.error("提取用户信息失败", e);
            throw new BaseException(ResultCodeEnum.TOKEN_INVALID);
        }

        return true;
    }

    /**
     * 从请求头提取Token
     *
     * @param request HTTP请求
     * @return Token字符串
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getTokenHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getTokenPrefix())) {
            return bearerToken.substring(jwtProperties.getTokenPrefix().length());
        }
        return null;
    }
}
