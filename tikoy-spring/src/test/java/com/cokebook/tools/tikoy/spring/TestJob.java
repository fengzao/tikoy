package com.cokebook.tools.tikoy.spring;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.kafka.KafkaJobFactory;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.mapping.annotation.OnInsert;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @date 2025/2/4
 */
@Component
@JobMapping(id = "test", db = "demo", factory = KafkaJobFactory.class)
public class TestJob {

    public static final AtomicLong count = new AtomicLong(0);

    @OnInsert("tbl_user")
    public void process(Log log) {
        System.out.println("log = " + log);
        count.incrementAndGet();
    }

}
