package com.cokebook.tools.tikoy.kafka;


import com.cokebook.tools.tikoy.spring.EnvironmentConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {
})
@TestPropertySource("classpath:application.properties")
@RunWith(SpringRunner.class)
public class KafkaJobTest {

    @Autowired
    private Environment environment;

    @Test
    public void test() {
        EnvironmentConfig config = new EnvironmentConfig(environment);
        JobProps props = JobProps.from(config, "test");
        Assert.assertEquals("test.demo", props.topics());
        Assert.assertEquals("localhost:9092", props.servers());
    }


}
