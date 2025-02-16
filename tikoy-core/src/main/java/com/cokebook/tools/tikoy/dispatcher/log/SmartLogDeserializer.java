package com.cokebook.tools.tikoy.dispatcher.log;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.dispatcher.log.Deserializer;
import com.cokebook.tools.tikoy.dispatcher.log.debezium.DebeziumLog;
import com.cokebook.tools.tikoy.dispatcher.log.maxwell.MaxwellLog;

import java.util.Map;

public class SmartLogDeserializer implements Deserializer<Log> {


    @Override
    public Log apply(String s) {
        JSONObject object = JSON.parseObject(s, JSONObject.class);
        if (isMaxwellStyle(object)) {
            return object.to(MaxwellLog.class);
        } else if (isDebeziumStyle(object)) {
            return object.to(DebeziumLog.class);
        }
        throw new IllegalArgumentException("unsupported log struct , you can replace the deserializer.");
    }

    protected boolean isMaxwellStyle(Map<String, Object> json) {
        return json.containsKey("data");
    }

    protected boolean isDebeziumStyle(Map<String, Object> json) {
        return json.containsKey("before") || json.containsKey("after");
    }

}
