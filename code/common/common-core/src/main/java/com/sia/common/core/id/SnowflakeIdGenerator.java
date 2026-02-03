package com.sia.common.core.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 雪花算法ID生成器
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
public class SnowflakeIdGenerator implements IdGenerator {

    private final Snowflake snowflake;

    /**
     * 构造函数，使用默认的workerId和datacenterId
     */
    public SnowflakeIdGenerator() {
        // 默认使用机器ID和数据中心ID都为1
        // 实际生产环境应该从配置文件或环境变量读取
        this.snowflake = IdUtil.getSnowflake(1, 1);
    }

    /**
     * 构造函数，指定workerId和datacenterId
     *
     * @param workerId     机器ID
     * @param datacenterId 数据中心ID
     */
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        this.snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }

    @Override
    public Long nextId() {
        return snowflake.nextId();
    }
}
