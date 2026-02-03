package com.sia.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security基础配置
 * 注意：此配置为通用配置，各服务可根据需要覆盖
 *
 * @author sia
 * @date 2026/02/02
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密码编码器Bean
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Security过滤器链配置
     * 默认配置：禁用CSRF，无状态Session，允许所有请求
     * 各服务可以通过覆盖此配置来添加自己的安全规则
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF（API服务通常不需要CSRF保护）
                .csrf(AbstractHttpConfigurer::disable)
                // 无状态Session（使用JWT，不需要Session）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 允许所有请求（具体权限控制由网关或各服务自行处理）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }
}
