package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;

import java.util.*;

/**
 * @date 2024/11/11
 */
public interface HandlerMapping {

    Handler getHandler(String group, Log record, Handler defaultHandler);

    void register(Object object);

    List<JobMapping> schemaMappings();

    Map<String, List<Matcher>> groupMatcherMap();

}
