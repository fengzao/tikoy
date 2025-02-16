package com.cokebook.tools.tikoy.dispatcher;

import com.cokebook.tools.tikoy.mapping.Op;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2024/12/4
 */
@Data
public class SimpleLog implements Log {

    private String database;
    private String table;
    private Op type;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> old = new HashMap<>();
    private Long ts;

    @Override
    public String database() {
        return database;
    }

    @Override
    public String table() {
        return table;
    }

    @Override
    public Op type() {
        return type;
    }

    @Override
    public Map<String, Object> data() {
        return data;
    }

    @Override
    public Map<String, Object> old() {
        return old == null ? Collections.emptyMap() : old;
    }


    @Override
    public Long ts() {
        return ts;
    }
}
