package com.cokebook.tools.tikoy.spring;

import com.cokebook.tools.tikoy.support.Config;
import org.springframework.core.env.Environment;

public class EnvironmentConfig implements Config {

    private final Environment environment;

    public EnvironmentConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean contain(String key) {
        return environment.containsProperty(key);
    }

    @Override
    public Object get(String key) {
        return environment.getProperty(key);
    }
}
