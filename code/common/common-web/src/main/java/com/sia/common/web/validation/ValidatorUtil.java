package com.sia.common.web.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数验证工具类
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidatorUtil {

    private final Validator validator;

    /**
     * 验证对象
     *
     * @param object 待验证对象
     * @param <T>    对象类型
     * @return 验证错误信息，如果验证通过返回null
     */
    public <T> String validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (violations.isEmpty()) {
            return null;
        }
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }

    /**
     * 验证对象（指定验证组）
     *
     * @param object    待验证对象
     * @param groups    验证组
     * @param <T>       对象类型
     * @return 验证错误信息，如果验证通过返回null
     */
    public <T> String validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (violations.isEmpty()) {
            return null;
        }
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }

    /**
     * 验证对象的属性
     *
     * @param object      待验证对象
     * @param propertyName 属性名
     * @param <T>         对象类型
     * @return 验证错误信息，如果验证通过返回null
     */
    public <T> String validateProperty(T object, String propertyName) {
        Set<ConstraintViolation<T>> violations = validator.validateProperty(object, propertyName);
        if (violations.isEmpty()) {
            return null;
        }
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}
