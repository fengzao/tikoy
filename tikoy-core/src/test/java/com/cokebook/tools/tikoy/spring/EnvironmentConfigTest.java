package com.cokebook.tools.tikoy.spring;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
})
@TestPropertySource("classpath:environment.config.properties")
public class EnvironmentConfigTest {

    @Autowired
    private Environment environment;


    @Test
    public void test() {
        EnvironmentConfig config = new EnvironmentConfig(environment);
        Assert.assertTrue(config.contain("spring.application.name"));
        Assert.assertEquals("xyz", config.get("spring.application.name"));
    }

}
