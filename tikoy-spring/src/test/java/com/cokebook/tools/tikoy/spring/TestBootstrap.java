package com.cokebook.tools.tikoy.spring;

import com.alibaba.druid.pool.DruidDataSource;
import com.cokebook.tools.tikoy.container.JobSnapshotRunProps;
import com.cokebook.tools.tikoy.kafka.KafkaJobFactory;
import com.cokebook.tools.tikoy.spring.controller.JobSnapshotTriggerController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2025/2/4
 */
@SpringBootApplication
@RequestMapping
public class TestBootstrap {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TestBootstrap.class, args);
        JobSnapshotTriggerController controller = ctx.getBean(JobSnapshotTriggerController.class);

        try {
            Map<String, JobSnapshotRunProps.Prop> props = new HashMap<>();
            props.put("tbl_user", JobSnapshotRunProps.Prop.builder()
                    .enable(true)
                    .offset(0L)
                    .build());
            controller.start(JobSnapshotRunProps.builder()
                    .group("test")
                    .props(props)
                    .build());

            System.out.println("count = " + TestJob.count.get());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

//        System.exit(1);

    }

    @Bean(initMethod = "init")
    public DruidDataSource dataSource() throws Exception {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:h2:mem:demo;INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
        return druidDataSource;
    }


    @Bean
    public KafkaJobFactory kafkaJobFactory(Environment env) {

        return new KafkaJobFactory("", key -> env.getProperty(key));
    }

}
