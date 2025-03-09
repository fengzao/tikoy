package com.cokebook.tools.tikoy.kafka;

import com.cokebook.tools.tikoy.support.Config;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @date 2025/2/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobProps extends HashMap<String, Object> {
    /**
     * job subscribe kafka topics
     */
    public static final String TOPICS_CONFIG = "topics";

    public static List<String> configNames() {
        return Collections.singletonList(TOPICS_CONFIG);
    }

    public JobProps servers(String servers) {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        return this;
    }

    public String servers() {
        return (String) get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
    }

    public JobProps id(String groupId) {
        put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return this;
    }

    public String id() {
        return (String) get(ConsumerConfig.GROUP_ID_CONFIG);
    }

    public String topics() {
        return (String) get(TOPICS_CONFIG);
    }

    public JobProps topics(String topics) {
        put(TOPICS_CONFIG, topics);
        return this;
    }

    public JobProps valueDeserializer(String valueDeserializer) {
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        return this;
    }

    public String valueDeserializer() {
        return (String) get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);
    }


    public static JobProps from(Config config, String prefix) {
        JobProps props = new JobProps();
        List<String> configNames = new ArrayList<>(ConsumerConfig.configNames());
        configNames.addAll(configNames());
        for (String key : configNames) {
            String nKey = Stream.of(prefix, key).filter(Objects::nonNull)
                    .collect(Collectors.joining("."));
            if (config.contain(nKey)) {
                props.put(key, config.get(nKey));
            }
        }
        return props;
    }

}
