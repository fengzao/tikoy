package com.cokebook.tools.tikoy.dispatcher;

import com.cokebook.tools.tikoy.mapping.Op;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库日志格式:
 * Note: 接口整合 maxwell / debezium 数据结构风格  , 开发者可根据喜好自行选择. 最好配对使用,即 after/before,  data/old.
 * <p>
 * Note: 在 maxwell 数据结构风格中 [data/old 方法对] ,  type == Op.DELETE 时 {@link  #data()} 其值为表记录原始值, 请基于场景决定是否需要区分判断
 * Note: 在 maxwell 数据结构风格中 [data/old 方法对] ,  type == update    时 {@link  #old() } 其值仅包含实际变更的值.
 *
 * @date 2024/11/12
 */
public interface Log {

    /**
     * 数据库名
     *
     * @return 数据库名
     */
    String database();

    /**
     * 表名
     *
     * @return 表名
     */
    String table();

    /**
     * 操作类型
     *
     * @return 操作类型  {@link Op}
     */
    Op type();

    /**
     * 变更后的表数据
     * Note:  maxwell 风格数据格式中 当 type == Op.DELETE 时 data 为删除的数据, 请注意判断
     *
     * @return 变更后的表数据
     */
    Map<String, Object> data();

    /**
     * 实际发生变更的字段原始值 Map : insert 时为空Map
     *
     * @return 实际发生变更的字段原始值
     */
    Map<String, Object> old();


    /**
     * 日志时间戳
     *
     * @return 日志时间戳
     */
    Long ts();

    /**
     * 数据库操作变更后的表记录数据
     *
     * @return 数据库操作变更后的表记录数据
     */
    default Map<String, Object> after() {
        if (Op.DELETE == type()) {
            return Collections.emptyMap();
        }
        return data();
    }

    /**
     * 数据库操作变更前的表记录数据
     *
     * @return 数据库操作变更前的表记录数据
     */
    default Map<String, Object> before() {
        if (Op.INSERT == type()) {
            return Collections.emptyMap();
        }
        Map<String, Object> old = new HashMap<>(data());
        old.putAll(old());
        return old;
    }

}
