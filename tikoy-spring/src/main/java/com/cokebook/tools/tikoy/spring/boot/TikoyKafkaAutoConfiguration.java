package com.cokebook.tools.tikoy.spring.boot;

import com.cokebook.tools.tikoy.container.JobContainer;
import com.cokebook.tools.tikoy.container.JobFactory;
import com.cokebook.tools.tikoy.container.JobSnapshotTrigger;
import com.cokebook.tools.tikoy.kafka.KafkaJobFactory;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.function.Function;

/**
 * @date 2024/11/12
 */
@Configuration
@ConditionalOnClass(KafkaJobFactory.class)
public class TikoyKafkaAutoConfiguration {

    @Value("${spring.tikoy.kafka-job-factory.job-props-prefix:}")
    private String jobPropPrefix;

    @Bean
    public KafkaJobFactory kafkaJobFactory(Environment environment) {
        return new KafkaJobFactory(jobPropPrefix, environment::getProperty);
    }

}
