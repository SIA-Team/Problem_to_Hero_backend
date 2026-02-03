package com.sia.common.web.result;

import java.util.function.Supplier;

/**
 * 统一响应构建器
 *
 * @author sia
 * @date 2026/02/02
 */
public class ResultBuilder {

    /**
     * 成功响应构建器
     *
     * @param <T> 数据类型
     * @return ResultBuilder实例
     */
    public static <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 成功响应构建器（带数据）
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return ResultBuilder实例
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 成功响应构建器（自定义消息）
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return ResultBuilder实例
     */
    public static <T> Result<T> success(String message) {
        return Result.success(message);
    }

    /**
     * 成功响应构建器（自定义消息和数据）
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return ResultBuilder实例
     */
    public static <T> Result<T> success(String message, T data) {
        return Result.success(message, data);
    }

    /**
     * 失败响应构建器
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return ResultBuilder实例
     */
    public static <T> Result<T> error(String message) {
        return Result.error(message);
    }

    /**
     * 失败响应构建器（自定义响应码）
     *
     * @param code    响应码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return ResultBuilder实例
     */
    public static <T> Result<T> error(Integer code, String message) {
        return Result.error(code, message);
    }

    /**
     * 执行操作并返回结果
     * 如果操作成功，返回成功响应；如果抛出异常，返回失败响应
     *
     * @param supplier 操作
     * @param <T>      数据类型
     * @return Result
     */
    public static <T> Result<T> execute(Supplier<T> supplier) {
        try {
            T data = supplier.get();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
