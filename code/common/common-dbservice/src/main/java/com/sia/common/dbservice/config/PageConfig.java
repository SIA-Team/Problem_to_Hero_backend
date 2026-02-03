package com.sia.common.dbservice.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页配置类
 * 提供统一的分页请求和响应格式
 *
 * @param <T> 数据类型
 * @author sia
 * @date 2026/02/02
 */
@Data
public class PageConfig<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码（从1开始）
     */
    private Long current = 1L;

    /**
     * 每页大小
     */
    private Long size = 10L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 创建分页对象
     *
     * @param current 当前页码
     * @param size    每页大小
     * @param <T>     数据类型
     * @return Page对象
     */
    public static <T> Page<T> of(Long current, Long size) {
        Page<T> page = new Page<>(current, size);
        return page;
    }

    /**
     * 从IPage转换为PageConfig
     *
     * @param page IPage对象
     * @param <T>  数据类型
     * @return PageConfig对象
     */
    public static <T> PageConfig<T> from(IPage<T> page) {
        PageConfig<T> pageConfig = new PageConfig<>();
        pageConfig.setCurrent(page.getCurrent());
        pageConfig.setSize(page.getSize());
        pageConfig.setTotal(page.getTotal());
        pageConfig.setPages(page.getPages());
        pageConfig.setRecords(page.getRecords());
        return pageConfig;
    }
}
