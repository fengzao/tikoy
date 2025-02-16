package com.cokebook.tools.tikoy.dispatcher.log;

import java.util.function.Function;


public interface Deserializer<T> extends Function<String, T> {

}
