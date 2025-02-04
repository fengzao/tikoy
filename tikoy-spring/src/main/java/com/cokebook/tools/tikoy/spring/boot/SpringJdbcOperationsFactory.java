package com.cokebook.tools.tikoy.spring.boot;

import com.cokebook.tools.tikoy.support.JdbcOperationsFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @date 2024/11/14
 */
public class SpringJdbcOperationsFactory implements JdbcOperationsFactory {

    public static final Function<String, List<String>> CANDIDATE_DS_GENERATOR = (dbName)
            -> Arrays.asList("", "ds", "-ds", "datasource", "-datasource").stream().map(suffix -> dbName + suffix).collect(Collectors.toList());

    public static final ConcurrentMap<String, NamedParameterJdbcOperations> dbRefOperations = new ConcurrentHashMap<>();

    ApplicationContext applicationContext;

    public SpringJdbcOperationsFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public NamedParameterJdbcOperations get(String db) {

        Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(DataSource.class);

        Function<String, String> candidateBeanNameMatcher = (dbName) -> {
            List<String> candidateDataSourceNames = CANDIDATE_DS_GENERATOR.apply(dbName);
            return dataSourceMap.keySet().stream().filter(beanName ->
                    candidateDataSourceNames.stream().filter(cdn -> cdn.equalsIgnoreCase(beanName)).findFirst().orElse(null) != null)
                    .findFirst().orElse(null);
        };

        dbRefOperations.computeIfAbsent(db, (dbName) -> {

            String candidateDataSourceBeanName = candidateBeanNameMatcher.apply(db);
            if (candidateBeanNameMatcher == null) {
                candidateDataSourceBeanName = candidateBeanNameMatcher.apply("default");
            }

            if (candidateDataSourceBeanName != null) {
                DataSource dataSource = dataSourceMap.get(candidateDataSourceBeanName);
                return new NamedParameterJdbcTemplate(dataSource);
            }

            if (dataSourceMap.size() == 1) {
                DataSource dataSource = dataSourceMap.values().stream().findFirst().orElse(null);
                return new NamedParameterJdbcTemplate(dataSource);
            }
            throw new IllegalStateException("No datasource found for db = '" + db + "' , all ds bean names = [" + dataSourceMap.keySet().stream().collect(Collectors.joining(",")) + "]");
        });

        return dbRefOperations.get(db);

    }
}
