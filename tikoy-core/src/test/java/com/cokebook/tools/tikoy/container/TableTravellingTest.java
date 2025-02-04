package com.cokebook.tools.tikoy.container;

import com.alibaba.druid.pool.DruidDataSource;
import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.support.Table;
import com.cokebook.tools.tikoy.travelling.extractor.CacheParamExtractor;
import com.cokebook.tools.tikoy.travelling.extractor.IdParamExtractors;
import com.cokebook.tools.tikoy.travelling.TableTravelling;
import com.cokebook.tools.tikoy.support.JdbcOperationsFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @date 2025/1/31
 */
public class TableTravellingTest {

    @Test
    public void test() throws Exception {


        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.setUrl("jdbc:h2:mem:demo;INIT=RUNSCRIPT FROM 'classpath:schema.sql'");

        druidDataSource.init();

        JdbcOperationsFactory factory = databaseName -> new NamedParameterJdbcTemplate(druidDataSource);


        TableTravelling ss = TableTravelling.builder()
                .group("test")
                .table(new Table("demo", "tbl_user"))
                .build();

        final CacheParamExtractor paramExtractor = CacheParamExtractor.of(IdParamExtractors.max(() -> 0L));

        ss.doTravelling(new LogDispatcher() {
            @Override
            public void handle(String group, Log record) {
                System.out.println("record = " + record);
            }
        }, factory, paramExtractor);

        Assert.assertEquals(50L, paramExtractor.get().get("id"));

    }

}
