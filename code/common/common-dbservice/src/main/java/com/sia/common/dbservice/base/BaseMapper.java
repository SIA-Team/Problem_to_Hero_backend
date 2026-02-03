package com.sia.common.dbservice.base;

/**
 * 基础Mapper接口
 * 扩展MyBatis-Plus的BaseMapper，提供通用CRUD方法
 *
 * @param <T> 实体类型
 * @author sia
 * @date 2026/02/02
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    // 可以在这里添加自定义的通用方法
    // 例如：批量插入、批量更新等
}
