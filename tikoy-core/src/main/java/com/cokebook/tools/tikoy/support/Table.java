package com.cokebook.tools.tikoy.support;

import lombok.Data;

/**
 * @date 2025/2/3
 */
@Data
public class Table {
    private String database;
    private String name;

    public Table(String database, String name) {
        this.database = database;
        this.name = name;
    }
}