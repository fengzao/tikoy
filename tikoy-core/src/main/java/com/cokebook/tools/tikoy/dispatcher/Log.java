package com.cokebook.tools.tikoy.dispatcher;

import com.cokebook.tools.tikoy.mapping.Op;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * database table/collection op log
 *
 * @date 2024/11/12
 */
public interface Log {

    /**
     * database name
     *
     * @return database name
     */
    String database();

    /**
     * table name
     *
     * @return table name
     */
    String table();

    /**
     * op type
     *
     * @return {@link Op}
     */
    Op type();

    /**
     * the table record value after  op
     *
     * @return 变更后的表数据
     */
    Map<String, Object> data();

    /**
     * the old column value before op
     *
     * @return the old column value before op
     */
    Map<String, Object> old();


    /**
     * transaction ts
     *
     * @return transaction ts
     */
    Long ts();

    /**
     * the table record value after op
     *
     * @return the table record value after op
     */
    default Map<String, Object> after() {
        if (Op.DELETE == type()) {
            return Collections.emptyMap();
        }
        return data();
    }

    /**
     * the table record value before op
     *
     * @return the table record value before op
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
