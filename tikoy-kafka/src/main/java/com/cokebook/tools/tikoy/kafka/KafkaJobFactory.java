package com.cokebook.tools.tikoy.kafka;

import com.cokebook.tools.tikoy.container.Job;
import com.cokebook.tools.tikoy.container.JobFactory;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.support.Props;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @date 2025/2/3
 */
@Slf4j
public class KafkaJobFactory implements JobFactory {

    private String prefix;
    private Function<String, String> env;


    public KafkaJobFactory(String prefix, Function<String, String> env) {
        this.prefix = prefix;
        this.env = env != null ? env : text -> null;
    }

    @Override
    public Job get(String id, LogDispatcher dispatcher) {

        JobProps props = Props.of(env,
                Stream.of(prefix, id)
                        .filter(Objects::nonNull)
                        .filter(str -> !str.trim().isEmpty())
                        .collect(Collectors.joining(".")),
                JobProps.class
        );

        return new KafkaJob(id, props, dispatcher);

    }


}
