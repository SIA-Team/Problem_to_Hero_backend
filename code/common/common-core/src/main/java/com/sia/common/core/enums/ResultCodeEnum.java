package com.sia.common.core.enums;

import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author sia
 * @date 2026/02/02
 */
@Getter
public enum ResultCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 方法不允许
     */
    METHOD_NOT_ALLOWED(405, "方法不允许"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(1000, "业务异常"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1001, "用户不存在"),

    /**
     * 用户已存在
     */
    USER_ALREADY_EXISTS(1002, "用户已存在"),

    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(1003, "用户名或密码错误"),

    /**
     * Token无效或已过期
     */
    TOKEN_INVALID(1004, "Token无效或已过期"),

    /**
     * Token缺失
     */
    TOKEN_MISSING(1005, "Token缺失"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(1006, "权限不足"),

    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(2001, "数据不存在"),

    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTS(2002, "数据已存在"),

    /**
     * 操作失败
     */
    OPERATION_FAILED(2003, "操作失败");

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据响应码获取枚举
     *
     * @param code 响应码
     * @return 枚举
     */
    public static ResultCodeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ResultCodeEnum resultCodeEnum : values()) {
            if (resultCodeEnum.getCode().equals(code)) {
                return resultCodeEnum;
            }
        }
        return null;
    }
}
