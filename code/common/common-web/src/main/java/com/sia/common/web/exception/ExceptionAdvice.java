package com.sia.common.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常通知类
 * 用于记录异常日志、发送告警等
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@RestControllerAdvice
@Order(1)
public class ExceptionAdvice {

    // 此类可以用于扩展异常处理功能，如：
    // 1. 异常日志记录到数据库
    // 2. 异常告警通知（邮件、短信、钉钉等）
    // 3. 异常统计分析
    // 4. 异常监控指标上报

    // 当前由GlobalExceptionHandler统一处理异常
    // 如需扩展功能，可以在这里添加@ExceptionHandler方法
}
