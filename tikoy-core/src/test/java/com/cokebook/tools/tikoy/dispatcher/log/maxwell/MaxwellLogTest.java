package com.cokebook.tools.tikoy.dispatcher.log.maxwell;

import com.alibaba.fastjson2.JSON;
import com.cokebook.tools.tikoy.mapping.Op;
import org.junit.Assert;
import org.junit.Test;

public class MaxwellLogTest {

    @Test
    public void test() {

        String json = "{\n" +
                "  \"database\": \"sales\",\n" +
                "  \"table\": \"customers\",\n" +
                "  \"type\": \"update\",\n" +
                "  \"ts\": 1644785400000" +
                "  \"xid\": 12345,\n" +
                "  \"commit\": true,\n" +
                "  \"data\": {\n" +
                "    \"id\": 1,\n" +
                "    \"first_name\": \"John\", \n" +
                "    \"last_name\": \"Doe\",\n" +
                "    \"email\": \"john.doe@example.com\"\n" +
                "  },\n" +
                "  \"old\": {\n" +
                "    \"first_name\": \"Johnny\", \n" +
                "    \"email\": \"johnny.doe@example.com\"\n" +
                "  }\n" +
                "}";

        MaxwellLog log = JSON.parseObject(json, MaxwellLog.class);


        Assert.assertEquals(4, log.before().size());

        Assert.assertEquals("Johnny", log.before().get("first_name"));
        Assert.assertEquals("Doe", log.before().get("last_name"));
        Assert.assertEquals("johnny.doe@example.com", log.before().get("email"));
        Assert.assertEquals(1, log.before().get("id"));
        Assert.assertEquals(Op.UPDATE, log.type());


        Assert.assertEquals("John", log.after().get("first_name"));
        Assert.assertEquals("Doe", log.after().get("last_name"));
        Assert.assertEquals("john.doe@example.com", log.after().get("email"));
        Assert.assertEquals(1, log.after().get("id"));


        Assert.assertEquals("sales", log.database());
        Assert.assertEquals("customers", log.table());


    }

}
