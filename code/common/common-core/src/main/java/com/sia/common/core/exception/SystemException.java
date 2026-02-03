package com.sia.common.core.exception;

import com.sia.common.core.enum.ResultCodeEnum;

/**
 * 系统异常类
 *
 * @author sia
 * @date 2026/02/02
 */
public class SystemException extends BaseException {

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public SystemException(String message) {
        super(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause   原因
     */
    public SystemException(String message, Throwable cause) {
        super(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), message, cause);
    }

    /**
     * 构造函数
     *
     * @param resultCodeEnum 响应码枚举
     */
    public SystemException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum);
    }

    /**
     * 构造函数
     *
     * @param resultCodeEnum 响应码枚举
     * @param message        自定义消息
     */
    public SystemException(ResultCodeEnum resultCodeEnum, String message) {
        super(resultCodeEnum, message);
    }

    /**
     * 构造函数
     *
     * @param code    响应码
     * @param message 异常消息
     */
    public SystemException(Integer code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     *
     * @param code    响应码
     * @param message 异常消息
     * @param cause   原因
     */
    public SystemException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
