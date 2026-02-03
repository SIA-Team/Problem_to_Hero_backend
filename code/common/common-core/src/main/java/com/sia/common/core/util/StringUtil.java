package com.sia.common.core.util;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 *
 * @author sia
 * @date 2026/02/02
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return StrUtil.isBlank(str);
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return StrUtil.isNotBlank(str);
    }

    /**
     * 去除字符串首尾空白
     *
     * @param str 字符串
     * @return 去除空白后的字符串
     */
    public static String trim(String str) {
        return StrUtil.trim(str);
    }

    /**
     * 去除字符串首尾空白，如果为空则返回null
     *
     * @param str 字符串
     * @return 去除空白后的字符串
     */
    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }
        String trimmed = trim(str);
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * 去除字符串首尾空白，如果为空则返回空字符串
     *
     * @param str 字符串
     * @return 去除空白后的字符串
     */
    public static String trimToEmpty(String str) {
        if (str == null) {
            return "";
        }
        return trim(str);
    }

    /**
     * 字符串首字母大写
     *
     * @param str 字符串
     * @return 首字母大写后的字符串
     */
    public static String capitalize(String str) {
        return StrUtil.upperFirst(str);
    }

    /**
     * 字符串首字母小写
     *
     * @param str 字符串
     * @return 首字母小写后的字符串
     */
    public static String uncapitalize(String str) {
        return StrUtil.lowerFirst(str);
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param camelCase 驼峰命名
     * @return 下划线命名
     */
    public static String camelToUnderline(String camelCase) {
        return StrUtil.toUnderlineCase(camelCase);
    }

    /**
     * 下划线命名转驼峰命名
     *
     * @param underline 下划线命名
     * @return 驼峰命名
     */
    public static String underlineToCamel(String underline) {
        return StrUtil.toCamelCase(underline);
    }

    /**
     * 字符串脱敏处理（保留前n位和后n位，中间用*代替）
     *
     * @param str       字符串
     * @param prefixLen 前缀长度
     * @param suffixLen 后缀长度
     * @return 脱敏后的字符串
     */
    public static String mask(String str, int prefixLen, int suffixLen) {
        if (isEmpty(str)) {
            return str;
        }
        int len = str.length();
        if (len <= prefixLen + suffixLen) {
            return str;
        }
        String prefix = str.substring(0, prefixLen);
        String suffix = str.substring(len - suffixLen);
        int maskLen = len - prefixLen - suffixLen;
        String mask = "*".repeat(maskLen);
        return prefix + mask + suffix;
    }

    /**
     * 手机号脱敏（保留前3位和后4位）
     *
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public static String maskPhone(String phone) {
        return mask(phone, 3, 4);
    }

    /**
     * 邮箱脱敏（保留@前1位和@后全部）
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 1) {
            return email;
        }
        String prefix = email.substring(0, 1);
        String suffix = email.substring(atIndex);
        return prefix + "***" + suffix;
    }
}
