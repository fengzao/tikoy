package com.cokebook.tools.tikoy.support;

public interface Config {

    boolean contain(String key);

    Object get(String key);
}
