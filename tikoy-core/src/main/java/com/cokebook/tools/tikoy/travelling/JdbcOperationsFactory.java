package com.cokebook.tools.tikoy.travelling;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/**
 * @date 2025/1/25
 */
public interface JdbcOperationsFactory {

    /**
     * 获取  NamedParameterJdbcOperations
     *
     * @param databaseName
     * @return
     */
    public NamedParameterJdbcOperations get(String databaseName);

}
