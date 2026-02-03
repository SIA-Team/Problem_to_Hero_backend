package com.sia.common.core.util;

import com.sia.common.core.constant.DateConstants;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日期工具类
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
public class DateUtil {

    /**
     * 格式化日期时间为字符串：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime 日期时间
     * @return 格式化后的字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateConstants.DATETIME_FORMATTER);
    }

    /**
     * 格式化日期为字符串：yyyy-MM-dd
     *
     * @param date 日期
     * @return 格式化后的字符串
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateConstants.DATE_FORMATTER);
    }

    /**
     * 格式化时间为字符串：HH:mm:ss
     *
     * @param dateTime 日期时间
     * @return 格式化后的字符串
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateConstants.TIME_FORMATTER);
    }

    /**
     * 格式化日期时间为指定格式的字符串
     *
     * @param dateTime 日期时间
     * @param pattern  格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return dateTime.format(formatter);
        } catch (Exception e) {
            log.error("日期格式化失败: dateTime={}, pattern={}", dateTime, pattern, e);
            return null;
        }
    }

    /**
     * 解析字符串为日期时间：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTimeStr 日期时间字符串
     * @return 日期时间
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateConstants.DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            log.error("日期时间解析失败: dateTimeStr={}", dateTimeStr, e);
            return null;
        }
    }

    /**
     * 解析字符串为日期：yyyy-MM-dd
     *
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateConstants.DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            log.error("日期解析失败: dateStr={}", dateStr, e);
            return null;
        }
    }

    /**
     * 解析字符串为日期时间（指定格式）
     *
     * @param dateTimeStr 日期时间字符串
     * @param pattern     格式
     * @return 日期时间
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.isEmpty() || pattern == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            log.error("日期时间解析失败: dateTimeStr={}, pattern={}", dateTimeStr, pattern, e);
            return null;
        }
    }

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static LocalDate today() {
        return LocalDate.now();
    }
}
