package com.sia.common.core.exception;

import com.sia.common.core.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 基础异常类
 *
 * @author sia
 * @date 2026/02/02
 */
@Getter
public class BaseException extends RuntimeException {

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    响应码
     * @param message 响应消息
     */
    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param resultCodeEnum 响应码枚举
     */
    public BaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    /**
     * 构造函数
     *
     * @param resultCodeEnum 响应码枚举
     * @param message        自定义消息
     */
    public BaseException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.code = resultCodeEnum.getCode();
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param code    响应码
     * @param message 响应消息
     * @param cause   原因
     */
    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
