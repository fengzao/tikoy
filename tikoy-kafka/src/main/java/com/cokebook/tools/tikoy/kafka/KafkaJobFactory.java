package com.cokebook.tools.tikoy.kafka;

import com.cokebook.tools.tikoy.container.Job;
import com.cokebook.tools.tikoy.container.JobFactory;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.support.Config;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @date 2025/2/3
 */
@Slf4j
public class KafkaJobFactory implements JobFactory {

    private final String prefix;
    private final Config config;

    public KafkaJobFactory(String prefix, Config config) {
        this.prefix = prefix;
        this.config = config;
    }

    @Override
    public Job get(String id, LogDispatcher dispatcher) {
        JobProps props = JobProps.from(config,
                Stream.of(prefix, id)
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(".")));
        return new KafkaJob(id, props, dispatcher);
    }

}
