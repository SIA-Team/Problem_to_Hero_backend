package com.sia.common.core.exception;

import com.sia.common.core.enums.ResultCodeEnum;

/**
 * 业务异常类
 *
 * @author sia
 * @date 2026/02/02
 */
public class BusinessException extends BaseException {

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public BusinessException(String message) {
        super(ResultCodeEnum.BUSINESS_ERROR.getCode(), message);
    }

    /**
     * 构造函数
     *
     * @param resultCodeEnum 响应码枚举
     */
    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum);
    }

    /**
     * 构造函数
     *
     * @param resultCodeEnum 响应码枚举
     * @param message        自定义消息
     */
    public BusinessException(ResultCodeEnum resultCodeEnum, String message) {
        super(resultCodeEnum, message);
    }

    /**
     * 构造函数
     *
     * @param code    响应码
     * @param message 异常消息
     */
    public BusinessException(Integer code, String message) {
        super(code, message);
    }
}
