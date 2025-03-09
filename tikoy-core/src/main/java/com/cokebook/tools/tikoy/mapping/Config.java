package com.cokebook.tools.tikoy.mapping;

public interface Config {

    boolean contain(String key);

    Object get(String key);
}
