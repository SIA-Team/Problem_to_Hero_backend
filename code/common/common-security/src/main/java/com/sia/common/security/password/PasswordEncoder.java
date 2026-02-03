package com.sia.common.security.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密工具类
 * 使用BCrypt算法进行密码加密和验证
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Component
public class PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoder() {
        // BCrypt强度设置为10（默认值，平衡安全性和性能）
        this.encoder = new BCryptPasswordEncoder(10);
    }

    /**
     * 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 检查密码是否需要重新加密
     * BCrypt会自动处理，但可以用于检查加密强度
     *
     * @param encodedPassword 加密后的密码
     * @return 是否需要重新加密
     */
    public boolean needsRehash(String encodedPassword) {
        // BCrypt格式：$2a$10$... 或 $2b$10$...
        // 这里简单检查，实际应用中可以根据强度要求判断
        return encodedPassword == null || !encodedPassword.startsWith("$2");
    }
}
