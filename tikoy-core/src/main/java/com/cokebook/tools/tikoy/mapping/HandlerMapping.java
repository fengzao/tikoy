package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.support.Table;

import java.util.List;

/**
 * @date 2024/11/11
 */
public interface HandlerMapping {

    Handler getHandler(String group, Log record, Handler defaultHandler);

    void register(Object object);

    List<JobMapping> schemaMappings();

    List<Table> snapshotTable(String group);

}
