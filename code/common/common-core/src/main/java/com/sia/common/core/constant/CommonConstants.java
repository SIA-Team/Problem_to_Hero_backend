package com.sia.common.core.constant;

/**
 * 通用常量类
 *
 * @author sia
 * @date 2026/02/02
 */
public class CommonConstants {

    /**
     * 默认页码
     */
    public static final Integer DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大每页大小
     */
    public static final Integer MAX_PAGE_SIZE = 100;

    /**
     * 成功状态码
     */
    public static final Integer SUCCESS_CODE = 200;

    /**
     * 失败状态码
     */
    public static final Integer ERROR_CODE = 500;

    /**
     * 成功消息
     */
    public static final String SUCCESS_MESSAGE = "操作成功";

    /**
     * 失败消息
     */
    public static final String ERROR_MESSAGE = "操作失败";

    /**
     * 用户ID请求头
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 用户名请求头
     */
    public static final String USERNAME_HEADER = "X-Username";

    /**
     * Token请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 删除标记：未删除
     */
    public static final Integer NOT_DELETED = 0;

    /**
     * 删除标记：已删除
     */
    public static final Integer DELETED = 1;

    /**
     * 启用状态
     */
    public static final Integer STATUS_ENABLED = 1;

    /**
     * 禁用状态
     */
    public static final Integer STATUS_DISABLED = 0;
}
