package com.cokebook.tools.tikoy.spring;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.mapping.Op;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2025/2/4
 */
public class TestLogDeserializer implements Deserializer<Log> {
    @Override
    public Log deserialize(String topic, byte[] data) {
        String str = new String(data);
        return new Log() {
            @Override
            public String database() {
                return "demo";
            }

            @Override
            public String table() {
                return "tbl_user";
            }

            @Override
            public Op type() {
                return Op.INSERT;
            }

            @Override
            public Map<String, Object> data() {
                Map<String, Object> x = new HashMap<>();
                x.put("text", str);
                return x;
            }

            @Override
            public Map<String, Object> old() {
                return null;
            }
        };
    }
}