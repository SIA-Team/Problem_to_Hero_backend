package com.sia.common.core.id;

/**
 * ID生成器接口
 *
 * @author sia
 * @date 2026/02/02
 */
public interface IdGenerator {

    /**
     * 生成下一个ID
     *
     * @return ID
     */
    Long nextId();
}
