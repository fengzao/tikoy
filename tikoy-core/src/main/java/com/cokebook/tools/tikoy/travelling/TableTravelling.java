package com.cokebook.tools.tikoy.travelling;

import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.dispatcher.log.SimpleLog;
import com.cokebook.tools.tikoy.mapping.Op;
import com.cokebook.tools.tikoy.support.Table;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @date 2025/1/31
 */
@Builder
@Data
public class TableTravelling {

    private String group;
    private Table table;

    private final AtomicBoolean running = new AtomicBoolean(false);


    public void doTravelling(LogDispatcher dispatcher, JdbcOperationsFactory factory, ParamExtractor paramExtractor) {

        if (running.get()) {
            return;
        }
        try {
            running.set(true);
            JdbcTravelling travelling = JdbcTravelling.builder()
                    .database(table.getDatabase())
                    .table(table.getName())
                    .sql("select * from " + table.getName() + " where id >:id  order by id asc limit 1000")
                    .paramExtractor(paramExtractor)
                    .factory(factory)
                    .build();
            travelling.run((database, table1, rows) -> {
                rows.forEach(row -> {
                    SimpleLog record = new SimpleLog();
                    record.setDatabase(database);
                    record.setTable(table1);
                    record.setData(row);
                    record.setOld(Collections.emptyMap());
                    record.setType(Op.INSERT);
                    record.setTs(System.currentTimeMillis());

                    if (running.get() == false) {
                        throw new JdbcTravelling.TravellingStopFlagException();
                    }

                    dispatcher.handle(group, record);
                });


            });
        } finally {
            running.set(false);
        }

    }

    public void stopTravelling() {
        running.set(false);
    }


}