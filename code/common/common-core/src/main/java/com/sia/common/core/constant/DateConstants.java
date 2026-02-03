package com.sia.common.core.constant;

import java.time.format.DateTimeFormatter;

/**
 * 日期常量类
 *
 * @author sia
 * @date 2026/02/02
 */
public class DateConstants {

    /**
     * 标准日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 标准日期格式：yyyy-MM-dd
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 标准时间格式：HH:mm:ss
     */
    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * ISO日期时间格式：yyyy-MM-dd'T'HH:mm:ss
     */
    public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * ISO日期时间格式（带时区）：yyyy-MM-dd'T'HH:mm:ss.SSSZ
     */
    public static final String ISO_DATETIME_WITH_TIMEZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**
     * 日期时间格式化器：yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /**
     * 日期格式化器：yyyy-MM-dd
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * 时间格式化器：HH:mm:ss
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
}
