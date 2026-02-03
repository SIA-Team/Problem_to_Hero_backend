package com.sia.common.web.config;

import com.sia.common.web.interceptor.AuthInterceptor;
import com.sia.common.web.interceptor.LogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 注册拦截器
 *
 * @author sia
 * @date 2026/02/02
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志拦截器，拦截所有请求
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**");

        // 认证拦截器，拦截所有请求（具体接口可通过@NoAuth注解跳过）
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/actuator/**",
                        "/error",
                        "/favicon.ico"
                );
    }
}
