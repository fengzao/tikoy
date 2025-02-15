package com.cokebook.tools.tikoy.dispatcher.log.maxwell;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.mapping.Op;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class MaxwellLog implements Log {

    private static final Map<String, Op> opMap = new HashMap<>();

    static {
        opMap.put("insert", Op.INSERT);
        opMap.put("update", Op.UPDATE);
        opMap.put("delete", Op.DELETE);
        opMap.put("bootstrap", Op.INSERT);
    }


    private String database;

    private String table;

    private String type;

    private Long ts;

    private Map<String, Object> data;

    private Map<String, Object> old;


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
        return Objects.requireNonNull(opMap.get(type));
    }

    @Override
    public Map<String, Object> data() {
        return data == null ? Collections.emptyMap() : data;
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
