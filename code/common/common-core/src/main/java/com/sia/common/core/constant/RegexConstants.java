package com.sia.common.core.constant;

/**
 * 正则表达式常量类
 *
 * @author sia
 * @date 2026/02/02
 */
public class RegexConstants {

    /**
     * 用户名正则：3-20位字母、数字、下划线
     */
    public static final String USERNAME = "^[a-zA-Z0-9_]{3,20}$";

    /**
     * 密码正则：8-20位，至少包含字母和数字
     */
    public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,20}$";

    /**
     * 邮箱正则
     */
    public static final String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /**
     * 手机号正则（11位数字）
     */
    public static final String PHONE = "^1[3-9]\\d{9}$";

    /**
     * URL正则
     */
    public static final String URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    /**
     * IP地址正则
     */
    public static final String IP = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    /**
     * 中文正则
     */
    public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";

    /**
     * 数字正则
     */
    public static final String NUMBER = "^\\d+$";

    /**
     * 正整数正则
     */
    public static final String POSITIVE_INTEGER = "^[1-9]\\d*$";

    /**
     * 非负整数正则（包括0）
     */
    public static final String NON_NEGATIVE_INTEGER = "^\\d+$";

    /**
     * 日期格式正则：yyyy-MM-dd
     */
    public static final String DATE = "^\\d{4}-\\d{2}-\\d{2}$";

    /**
     * 日期时间格式正则：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
}
