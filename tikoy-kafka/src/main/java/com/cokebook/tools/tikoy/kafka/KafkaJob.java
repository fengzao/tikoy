package com.cokebook.tools.tikoy.kafka;

import com.cokebook.tools.tikoy.container.Job;
import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.dispatcher.log.SmartLogDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.utils.ThreadUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @date 2025/2/3
 */
@Slf4j
public class KafkaJob implements Job {


    private final String id;
    private final JobProps props;
    private final LogDispatcher logDispatcher;
    private KafkaConsumer<String, Log> consumer;
    private final ExecutorService pool;
    private final AtomicBoolean closed = new AtomicBoolean(false);


    public KafkaJob(String id, JobProps props, LogDispatcher logDispatcher) {
        this.id = id;
        this.props = props;
        this.logDispatcher = logDispatcher;
        this.pool = Executors.newSingleThreadExecutor(ThreadUtils.createThreadFactory("kafka-based-job:" + id + ":%d", false));
    }

    @Override
    public void init() {

        if (props.valueDeserializer() == null) {
            props.valueDeserializer(DefaultDeserializer.class.getName());
        }
        if (props.id() == null) {
            props.id(this.id);
        }
        String topics = props.topics();
        if (topics == null || topics.trim().isEmpty()) {
            log.warn("the kafka consumer config  topics is null or empty , ignore this job.");
            return;
        }
        this.consumer = new KafkaConsumer<String, Log>(props);
        this.consumer.subscribe(Arrays.stream(topics.split(",")).map(String::trim).filter(str -> !str.isEmpty()).collect(Collectors.toList()));

    }

    @Override
    public void start() {
        pool.execute(this);
    }

    @Override
    public void run() {
        while (!closed.get()) {
            try {
                ConsumerRecords<String, Log> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Log> record : records) {
                    try {
                        logDispatcher.handle(id, record.value());
                    } catch (Exception e) {
                        log.error("kafka job process failed ignore it and continue to next , you need process it at you job .", e);
                    }
                }
                consumer.commitSync();
            } catch (CommitFailedException e) {
                log.error("kafka job commit failed due to ", e);
            }
        }
        consumer.close();
    }

    @Override
    public void stop() {
        closed.set(true);
        this.pool.shutdown();
    }


    public static class DefaultDeserializer extends SmartLogDeserializer implements Deserializer<Log> {

        @Override
        public Log deserialize(String topic, byte[] data) {
            return apply(new String(data));
        }
    }

}