package com.sia.common.web.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 * 标记在Controller方法上，表示该接口需要限流
 *
 * @author sia
 * @date 2026/02/02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * 限流key（支持SpEL表达式）
     *
     * @return 限流key
     */
    String key() default "";

    /**
     * 限流次数
     *
     * @return 限流次数
     */
    int count() default 100;

    /**
     * 时间窗口（秒）
     *
     * @return 时间窗口
     */
    int window() default 60;
}
