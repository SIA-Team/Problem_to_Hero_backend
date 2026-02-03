package com.sia.common.core.enums;

import lombok.Getter;

/**
 * 是否枚举
 *
 * @author sia
 * @date 2026/02/02
 */
@Getter
public enum YesNoEnum {

    /**
     * 否
     */
    NO(0, "否"),

    /**
     * 是
     */
    YES(1, "是");

    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    YesNoEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static YesNoEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (YesNoEnum yesNoEnum : values()) {
            if (yesNoEnum.getValue().equals(value)) {
                return yesNoEnum;
            }
        }
        return null;
    }

    /**
     * 判断是否为是
     *
     * @param value 值
     * @return 是否为是
     */
    public static boolean isYes(Integer value) {
        return YES.getValue().equals(value);
    }

    /**
     * 判断是否为否
     *
     * @param value 值
     * @return 是否为否
     */
    public static boolean isNo(Integer value) {
        return NO.getValue().equals(value);
    }
}
