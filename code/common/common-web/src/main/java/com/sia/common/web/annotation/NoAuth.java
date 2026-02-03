package com.sia.common.web.annotation;

import java.lang.annotation.*;

/**
 * 无需认证注解
 * 标记在Controller类或方法上，表示该接口不需要JWT认证
 *
 * @author sia
 * @date 2026/02/02
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuth {
    /**
     * 说明
     *
     * @return 说明
     */
    String value() default "";
}
