package com.cokebook.tools.tikoy.container;

import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.mapping.HandlerMapping;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.support.JdbcOperationsFactory;
import com.cokebook.tools.tikoy.travelling.TableTravelling;
import com.cokebook.tools.tikoy.travelling.extractor.IdParamExtractors;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @date 2025/1/27
 */
public class JobSnapshotTrigger {

    private Supplier<Map<String, List<TableTravelling>>> idRefTravellingsSupplier;
    private Supplier<LogDispatcher> binLogDispatcherSupplier;
    private Supplier<JdbcOperationsFactory> jdbcOperationsFactorySupplier;

    public JobSnapshotTrigger(HandlerMapping handlerMapping,
                              Supplier<LogDispatcher> binLogDispatcherSupplier,
                              Supplier<JdbcOperationsFactory> jdbcOperationsFactorySupplier) {

        this.binLogDispatcherSupplier = binLogDispatcherSupplier;
        this.jdbcOperationsFactorySupplier = jdbcOperationsFactorySupplier;
        this.idRefTravellingsSupplier = () -> {
            Map<String, List<JobMapping>> groupRefMappings = handlerMapping.schemaMappings().stream()
                    .collect(Collectors.groupingBy(JobMapping::id));

            final Map<String, List<TableTravelling>> jobIdRefTableTravellings = new ConcurrentHashMap<>();

            groupRefMappings.keySet().stream().forEach(group -> {

                jobIdRefTableTravellings.put(group,
                        handlerMapping.snapshotTable(group).stream()
                                .map(table -> TableTravelling.builder()
                                        .group(group)
                                        .table(table)
                                        .build()).collect(Collectors.toList()));
            });
            return jobIdRefTableTravellings;
        };


    }

    public void start(JobSnapshotRunProps props) {

        Map<String, List<TableTravelling>> idRefTravellings = idRefTravellingsSupplier.get();

        idRefTravellings.getOrDefault(props.getGroup(), Collections.emptyList())
                .stream().forEach(ss -> {
            JobSnapshotRunProps.Prop prop = props.getProps().get(ss.getTable().getName());
            if (prop == null || !Objects.equals(true, prop.getEnable())) {
                return;
            }
            ss.doTravelling(binLogDispatcherSupplier.get(),
                    jdbcOperationsFactorySupplier.get(),
                    IdParamExtractors.max(() -> prop.getOffset() == null ? 0 : prop.getOffset())
            );

        });

    }

    public void stop(JobSnapshotRunProps props) {
        Map<String, List<TableTravelling>> groupRefSs = idRefTravellingsSupplier.get();

        groupRefSs.getOrDefault(props.getGroup(), Collections.emptyList())
                .stream().forEach(ss -> {
            Boolean exists = props.getProps().containsKey(ss.getTable().getName());
            if (exists) {
                ss.stopTravelling();
            }
        });
    }
}
