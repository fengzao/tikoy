package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.mapping.annotation.OnInsert;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * @date 2024/11/12
 */
public class SimpHandlerMappingTest {


    @Test
    public void test() {
        XyzHandler xyzHandler = new XyzHandler();
        SimpleHandlerMapping simpleHandlerMapping = new SimpleHandlerMapping(Function.identity());
        simpleHandlerMapping.register(xyzHandler);

        Handler handler = simpleHandlerMapping.getHandler("TT", new SimpleLog(
                "", "tbl_xyz", Op.INSERT, Collections.emptyMap()
        ), null);

        Assert.assertNotNull(handler);

        Handler handler2 = simpleHandlerMapping.getHandler("TTX", new SimpleLog(
                "", "tbl_xyz", Op.INSERT, Collections.emptyMap()
        ), null);

        Assert.assertNull(handler2);

    }


    @JobMapping(id = "TT")
    public static class XyzHandler {
        @OnInsert("tbl_xyz")
        public void handle(Log record) {
        }
    }

    public static class SimpleLog implements Log {
        private String database;
        private String table;
        private Op op;
        private Map<String, Object> data;

        public SimpleLog(String database, String table, Op op, Map<String, Object> data) {
            this.database = database;
            this.table = table;
            this.op = op;
            this.data = data;
        }

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
            return op;
        }

        @Override
        public Map<String, Object> getAfter() {
            return data;
        }

        @Override
        public Map<String, Object> getBefore() {
            return null;
        }
    }

}
