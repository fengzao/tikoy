package com.cokebook.tools.tikoy.dispatcher.log.debezium;


import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.mapping.Op;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class DebeziumLog implements Log {

    private static final Map<String, Op> opMap = new HashMap<>();

    static {
        opMap.put("u", Op.UPDATE);
        opMap.put("c", Op.INSERT);
        opMap.put("r", Op.INSERT);
        opMap.put("d", Op.DELETE);
    }

    private Map<String, Object> after;
    private Map<String, Object> before;
    private String op;
    private Source source;

    @Override
    public String database() {
        return source.db;
    }

    @Override
    public String table() {
        return source.table;
    }

    @Override
    public Op type() {
        return Objects.requireNonNull(opMap.get(op));
    }

    @Override
    public Map<String, Object> data() {
        if (Op.DELETE == type()) {
            return before();
        } else {
            return after();
        }
    }

    @Override
    public Map<String, Object> old() {
        if (Op.INSERT == type()) {
            return Collections.emptyMap();
        } else if (Op.DELETE == type()) {
            return before();
        }
        final Map<String, Object> old = new HashMap<>();
        for (String key : before().keySet()) {
            Object bV = before().get(key);
            Object aV = after().get(key);
            if (Objects.equals(bV, aV)) {
                old.put(key, bV);
            }
        }

        return old;
    }

    @Override
    public Map<String, Object> after() {
        return after == null ? Collections.emptyMap() : after;
    }

    @Override
    public Map<String, Object> before() {
        return before == null ? Collections.emptyMap() : before;
    }

    @Override
    public Long ts() {
        return source.ts_ms;
    }


    @Data
    public static class Source {

        private String db;
        private String table;
        private Long ts_ms;

        public void setCollection(String collection) {
            this.table = collection;
        }

        public void setColl(String collection) {
            this.table = collection;
        }


    }

}
