package com.cokebook.tools.tikoy.dispatcher.log.debezium;

import com.alibaba.fastjson2.JSON;
import com.cokebook.tools.tikoy.mapping.Op;
import org.junit.Assert;
import org.junit.Test;

public class DebeziumLogTest {

    @Test
    public void test_update() {
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


        DebeziumLog log = JSON.parseObject(json, DebeziumLog.class);
        Assert.assertEquals("Johnny", log.before().get("first_name"));
        Assert.assertEquals("Doe", log.before().get("last_name"));
        Assert.assertEquals("johnny.doe@example.com", log.before().get("email"));
        Assert.assertEquals(1, log.before().get("id"));
        Assert.assertEquals(Op.UPDATE, log.type());


        Assert.assertEquals("John", log.after().get("first_name"));
        Assert.assertEquals("Doe", log.after().get("last_name"));
        Assert.assertEquals("john.doe@example.com", log.after().get("email"));
        Assert.assertEquals(1, log.after().get("id"));


        Assert.assertEquals(2, log.old().size());
        Assert.assertEquals("Johnny", log.old().get("first_name"));
        Assert.assertEquals("johnny.doe@example.com", log.old().get("email"));
        Assert.assertEquals(4, log.data().size());

        Assert.assertEquals("John", log.data().get("first_name"));
        Assert.assertEquals("Doe", log.data().get("last_name"));
        Assert.assertEquals("john.doe@example.com", log.data().get("email"));
        Assert.assertEquals(1, log.data().get("id"));

        Assert.assertEquals(Long.valueOf(1644785400000L), log.ts());

    }

    @Test
    public void test_insert() {
        String json = "{\n" +
                "  \"before\": null,\n" +
                "  \"after\": {\n" +
                "    \"id\": 2,\n" +
                "    \"first_name\": \"Jane\",\n" +
                "    \"last_name\": \"Smith\",\n" +
                "    \"email\": \"jane.smith@example.com\"\n" +
                "  },\n" +
                "  \"source\": {\n" +
                "    \"version\": \"1.8.1.Final\",\n" +
                "    \"connector\": \"mysql\",\n" +
                "    \"name\": \"my_mysql_server\",\n" +
                "    \"ts_ms\": 1644785500000,\n" +
                "    \"snapshot\": \"false\",\n" +
                "    \"db\": \"sales\",\n" +
                "    \"table\": \"customers\",\n" +
                "    \"server_id\": 12345,\n" +
                "    \"gtid\": null,\n" +
                "    \"file\": \"binlog.000003\",\n" +
                "    \"pos\": 56789,\n" +
                "    \"row\": 0,\n" +
                "    \"thread\": null,\n" +
                "    \"query\": null\n" +
                "  },\n" +
                "  \"op\": \"c\",\n" +
                "  \"ts_ms\": 1644785501000\n" +
                "}";

        DebeziumLog log = JSON.parseObject(json, DebeziumLog.class);

        Assert.assertEquals("Jane", log.after().get("first_name"));
        Assert.assertEquals("Smith", log.after().get("last_name"));
        Assert.assertEquals("jane.smith@example.com", log.after().get("email"));
        Assert.assertEquals(2, log.after().get("id"));


        Assert.assertEquals("Jane", log.data().get("first_name"));
        Assert.assertEquals("Smith", log.data().get("last_name"));
        Assert.assertEquals("jane.smith@example.com", log.data().get("email"));
        Assert.assertEquals(2, log.data().get("id"));


        Assert.assertTrue(log.old().isEmpty());

        Assert.assertTrue(log.before().isEmpty());

        Assert.assertEquals(Long.valueOf(1644785500000L), log.ts());

    }

}
