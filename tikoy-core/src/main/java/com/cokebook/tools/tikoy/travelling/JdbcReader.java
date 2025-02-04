package com.cokebook.tools.tikoy.travelling;

import com.cokebook.tools.tikoy.travelling.extractor.CacheParamExtractor;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @date 2025/1/23
 */
public class JdbcReader implements Iterator<List<Map<String, Object>>> {

    public static JdbcReader of(NamedParameterJdbcOperations template, String sql, ParamExtractor paramExtractor) {
        return new JdbcReader(template, sql, paramExtractor);
    }

    private final String sql;
    private final CacheParamExtractor paramExtractor;
    private final NamedParameterJdbcOperations template;
    private List<Map<String, Object>> rows = null;


    public JdbcReader(DataSource dataSource, String sql, ParamExtractor paramExtractor) {
        this.sql = sql;
        this.paramExtractor = CacheParamExtractor.of(paramExtractor);
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }


    public JdbcReader(NamedParameterJdbcOperations template, String sql, ParamExtractor paramExtractor) {
        this.sql = sql;
        this.paramExtractor = CacheParamExtractor.of(paramExtractor);
        this.template = template;
    }


    @Override
    public boolean hasNext() {
        return rows == null || !rows.isEmpty();
    }

    @Override
    public List<Map<String, Object>> next() {
        rows = template.
                query(sql, paramExtractor.apply(rows == null ? Collections.emptyList() : rows),
                        new RowMapperResultSetExtractor<Map<String, Object>>(new ColumnMapRowMapper()));
        return rows;
    }

}




