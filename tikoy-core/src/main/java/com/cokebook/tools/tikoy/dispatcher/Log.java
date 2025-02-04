package com.cokebook.tools.tikoy.dispatcher;

import com.cokebook.tools.tikoy.mapping.Op;

import java.util.Map;

/**
 * @date 2024/11/12
 */
public interface Log {

    /**
     * 数据库名
     *
     * @return
     */
    String getDatabase();

    /**
     * 表名
     *
     * @return
     */
    String getTable();

    /**
     * 操作类型
     *
     * @return
     */
    Op getType();

    /**
     * 变更后的表数据
     *
     * @return
     */
    Map<String, Object> getAfter();

    /**
     * 变更的部分数据
     *
     * @return
     */
    Map<String, Object> getBefore();

}
