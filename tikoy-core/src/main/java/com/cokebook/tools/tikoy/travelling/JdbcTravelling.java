package com.cokebook.tools.tikoy.travelling;

import com.cokebook.tools.tikoy.support.JdbcOperationsFactory;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @date 2025/1/23
 */
@Data
@Builder
@Slf4j
public class JdbcTravelling {

    private String database;
    private String table;
    private String sql;
    private JdbcOperationsFactory factory;
    private ParamExtractor paramExtractor;


    public void run(Consumer consumer) {
        JdbcReader reader = JdbcReader.of(
                factory.get(database),
                sql,
                paramExtractor
        );

        while (reader.hasNext()) {
            try {
                consumer.accept(database, table, reader.next());
            } catch (TravellingStopFlagException e) {
                System.out.println("---------- stop for travelling stop flag exception");
                break;
            }
        }
    }

    public interface Consumer {

        /**
         * consumer data : extension {@link java.util.function.Consumer#accept(Object)}
         *
         * @param database
         * @param table
         * @param rows
         */
        void accept(String database, String table, List<Map<String, Object>> rows);

    }

    public static class TravellingStopFlagException extends RuntimeException {

    }


}
