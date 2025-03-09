package com.cokebook.tools.tikoy.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.Test;

public class KafkaJobFactoryTest {


    @Test
    public void test() {
        for (String str : ConsumerConfig.configNames()) {
            System.out.println(str);
        }
    }


}
