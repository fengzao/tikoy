package com.cokebook.tools.tikoy.kafka.spring;

import com.cokebook.tools.tikoy.kafka.KafkaJobFactory;
import com.cokebook.tools.tikoy.spring.EnvironmentConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @date 2024/11/12
 */
@Configuration
public class TikoyKafkaAutoConfiguration {

    @Value("${spring.tikoy.kafka-job-factory.job-props-prefix:}")
    private String jobPropPrefix;

    @Bean
    public KafkaJobFactory kafkaJobFactory(Environment environment) {
        return new KafkaJobFactory(jobPropPrefix, new EnvironmentConfig(environment));
    }

}
