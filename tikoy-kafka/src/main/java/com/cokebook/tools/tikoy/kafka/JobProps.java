package com.cokebook.tools.tikoy.kafka;

import com.cokebook.tools.tikoy.support.Props;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @date 2025/2/3
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobProps {

    @Props.Key("group.id")
    private String id;

    @Props.Key("bootstrap.servers")
    private String servers;

    @Props.Key("key.deserializer")
    @Builder.Default
    private String keyDeserializer = StringDeserializer.class.getName();

    @Props.Key("value.deserializer")
    private String valueDeserializer;

    @Props.Key("enable.auto.commit")
    @Builder.Default
    private Boolean autoCommit = false;

    @Props.Key("topics")
    private String topics;


}
