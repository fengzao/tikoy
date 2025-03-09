package com.cokebook.tools.tikoy.container.snapshot;

import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.mapping.HandlerMapping;
import com.cokebook.tools.tikoy.mapping.Op;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.support.Table;
import com.cokebook.tools.tikoy.travelling.JdbcOperationsFactory;
import com.cokebook.tools.tikoy.travelling.TableTravelling;
import com.cokebook.tools.tikoy.travelling.extractor.IdParamExtractors;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @date 2025/1/27
 */
public class SnapshotTrigger {

    private final Supplier<Map<String, List<TableTravelling>>> idRefTravellingsSupplier;
    private final Supplier<LogDispatcher> binLogDispatcherSupplier;
    private final Supplier<JdbcOperationsFactory> jdbcOperationsFactorySupplier;
    private final HandlerMapping handlerMapping;

    public SnapshotTrigger(HandlerMapping handlerMapping,
                           Supplier<LogDispatcher> binLogDispatcherSupplier,
                           Supplier<JdbcOperationsFactory> jdbcOperationsFactorySupplier) {

        this.handlerMapping = handlerMapping;
        this.binLogDispatcherSupplier = binLogDispatcherSupplier;
        this.jdbcOperationsFactorySupplier = jdbcOperationsFactorySupplier;
        this.idRefTravellingsSupplier = () -> {
            Map<String, List<JobMapping>> groupRefMappings = handlerMapping.schemaMappings().stream()
                    .collect(Collectors.groupingBy(JobMapping::id));

            final Map<String, List<TableTravelling>> jobIdRefTableTravellings = new ConcurrentHashMap<>();

            groupRefMappings.keySet().forEach(group -> {
                jobIdRefTableTravellings.put(group,
                        snapshotTable(group).stream()
                                .map(table -> TableTravelling.builder()
                                        .group(group)
                                        .table(table)
                                        .build()).collect(Collectors.toList()));
            });
            return jobIdRefTableTravellings;
        };

    }

    public List<Table> snapshotTable(String group) {
        return handlerMapping.groupMatcherMap().getOrDefault(group, Collections.emptyList())
                .stream().filter(matcher -> {
                    if (!matcher.getOps().contains(Op.INSERT)) {
                        return false;
                    }
                    return matcher.getDatabases().size() == 1
                            && !matcher.getDatabases().contains(JobMapping.ALL);
                }).map(matcher -> {
                    String database = new ArrayList<>(matcher.getDatabases()).get(0);
                    return matcher.getTables().stream().filter(tableName -> !Objects.equals(tableName, JobMapping.ALL))
                            .map(table -> new Table(database, table))
                            .collect(Collectors.toList());
                }).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void start(SnapshotRunProps props) {

        Map<String, List<TableTravelling>> idRefTravellings = idRefTravellingsSupplier.get();

        idRefTravellings.getOrDefault(props.getGroup(), Collections.emptyList()).forEach(ss -> {
            SnapshotRunProps.Prop prop = props.getProps().get(ss.getTable().getName());
            if (prop == null || !Objects.equals(true, prop.getEnable())) {
                return;
            }
            ss.doTravelling(binLogDispatcherSupplier.get(),
                    jdbcOperationsFactorySupplier.get(),
                    IdParamExtractors.max(() -> prop.getOffset() == null ? 0 : prop.getOffset())
            );

        });

    }

    public void stop(SnapshotRunProps props) {
        Map<String, List<TableTravelling>> groupRefSs = idRefTravellingsSupplier.get();

        groupRefSs.getOrDefault(props.getGroup(), Collections.emptyList()).forEach(ss -> {
            boolean exists = props.getProps().containsKey(ss.getTable().getName());
            if (exists) {
                ss.stopTravelling();
            }
        });
    }
}
