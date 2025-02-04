package com.cokebook.tools.tikoy.dispatcher;

import com.cokebook.tools.tikoy.mapping.Op;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2024/12/4
 */
@Data
public class MaxwellLog implements Log {

    private String database;
    private String table;
    private String type;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> old = new HashMap<>();
    private Long ts;
    private transient Long xid;


    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public Op getType() {
        return Op.of(type);
    }

    @Override
    public Map<String, Object> getBefore() {
        Map<String, Object> before = new HashMap<>(getAfter());
        for (String key : old.keySet()) {
            before.put(key, old.get(key));
        }
        return before;
    }

    @Override
    public Map<String, Object> getAfter() {
        return data;
    }
}
