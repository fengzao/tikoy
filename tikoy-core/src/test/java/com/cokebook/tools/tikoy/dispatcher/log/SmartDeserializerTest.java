package com.cokebook.tools.tikoy.dispatcher.log;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.dispatcher.log.debezium.DebeziumLog;
import org.junit.Assert;
import org.junit.Test;

public class SmartDeserializerTest {

    @Test
    public void test() {
        String json = "{\n" +
                "  \"before\": {\n" +
                "    \"id\": 1,\n" +
                "    \"first_name\": \"Johnny\",\n" +
                "    \"last_name\": \"Doe\",\n" +
                "    \"email\": \"johnny.doe@example.com\"\n" +
                "  },\n" +
                "  \"after\": {\n" +
                "    \"id\": 1,\n" +
                "    \"first_name\": \"John\",\n" +
                "    \"last_name\": \"Doe\",\n" +
                "    \"email\": \"john.doe@example.com\"\n" +
                "  },\n" +
                "  \"source\": {\n" +
                "    \"version\": \"1.8.1.Final\",\n" +
                "    \"connector\": \"mysql\",\n" +
                "    \"name\": \"my_mysql_server\",\n" +
                "    \"ts_ms\": 1644785400000,\n" +
                "    \"snapshot\": \"false\",\n" +
                "    \"db\": \"sales\",\n" +
                "    \"table\": \"customers\",\n" +
                "    \"server_id\": 12345,\n" +
                "    \"gtid\": null,\n" +
                "    \"file\": \"binlog.000003\",\n" +
                "    \"pos\": 45678,\n" +
                "    \"row\": 0,\n" +
                "    \"thread\": null,\n" +
                "    \"query\": null\n" +
                "  },\n" +
                "  \"op\": \"u\",\n" +
                "  \"ts_ms\": 1644785401000\n" +
                "}";


        SmartLogDeserializer deserializer = new SmartLogDeserializer();

        Log log = deserializer.apply(json);

        Assert.assertTrue(log instanceof DebeziumLog);

        Assert.assertEquals(Long.valueOf(1644785400000L), log.ts());

    }
}
