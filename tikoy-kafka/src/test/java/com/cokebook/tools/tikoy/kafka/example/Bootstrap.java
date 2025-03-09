package com.cokebook.tools.tikoy.kafka.example;

import com.alibaba.druid.pool.DruidDataSource;
import com.cokebook.tools.tikoy.kafka.KafkaJobFactory;
import com.cokebook.tools.tikoy.spring.EnvironmentConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @date 2025/2/4
 */
@SpringBootApplication
public class Bootstrap {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Bootstrap.class, args);

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
        return new KafkaJobFactory("", new EnvironmentConfig(env));
    }

}
