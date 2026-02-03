package com.sia.common.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * JWT Claims实体
 *
 * @author sia
 * @date 2026/02/02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtClaims implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录类型：PASSWORD/TWITTER/FACEBOOK/INSTAGRAM
     */
    private String loginType;

    /**
     * 第三方用户ID（可选）
     */
    private String thirdPartyId;
}
