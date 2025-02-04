package com.cokebook.tools.tikoy.container;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcherImpl;
import com.cokebook.tools.tikoy.mapping.Handler;
import com.cokebook.tools.tikoy.mapping.HandlerMapping;
import com.cokebook.tools.tikoy.mapping.SimpleHandlerMapping;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.support.Lifecycle;
import com.cokebook.tools.tikoy.support.Table;
import com.cokebook.tools.tikoy.travelling.TableTravelling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @date 2025/1/26
 */
public class JobContainer implements HandlerMapping, Lifecycle {
    private LogDispatcher dispatcher;
    private SimpleHandlerMapping handlerMapping;
    private Function<String, String> textResolver;
    public Function<Class<? extends JobFactory>, ? extends JobFactory> factoryBuilder;

    private final List<Job> jobs = new ArrayList<>();
    private final Map<String, List<TableTravelling>> groupRefSs = new ConcurrentHashMap<>();

    public JobContainer(Function<String, String> textResolver,
                        Function<Class<? extends JobFactory>, ? extends JobFactory> factoryBuilder) {
        this.textResolver = textResolver;
        this.factoryBuilder = factoryBuilder;
        this.handlerMapping = new SimpleHandlerMapping(this.textResolver);
        this.dispatcher = new LogDispatcherImpl(handlerMapping);
    }

    @Override
    public Handler getHandler(String group, Log record, Handler defaultHandler) {
        return handlerMapping.getHandler(group, record, defaultHandler);
    }

    @Override
    public void register(Object object) {
        handlerMapping.register(object);
    }

    @Override
    public List<JobMapping> schemaMappings() {
        return handlerMapping.schemaMappings();
    }

    @Override
    public List<Table> snapshotTable(String group) {
        return handlerMapping.snapshotTable(group);
    }

    public LogDispatcher getLogDispatcher() {
        return this.dispatcher;
    }

    @Override
    public void init() {
        Map<String, List<JobMapping>> groupRefMappings = handlerMapping.schemaMappings().stream()
                .collect(Collectors.groupingBy(JobMapping::id));

        groupRefMappings.keySet().stream().forEach(group -> {

            groupRefSs.put(group,
                    handlerMapping.snapshotTable(group).stream()
                            .map(table -> TableTravelling.builder()
                                    .group(group)
                                    .table(table)
                                    .build()).collect(Collectors.toList()));
        });

        for (String group : groupRefMappings.keySet()) {
            List<Class<? extends JobFactory>> factories = groupRefMappings.get(group).stream()
                    .map(schemaMapping -> schemaMapping.factory())
                    .filter(clazz -> clazz != JobFactory.class)
                    .collect(Collectors.toList());
            if (factories.size() > 1) {
                throw new IllegalStateException("schema mapping group = '" + group + "' has more than one job factory");
            }
            if (factories.isEmpty()) {
                continue;
            }
            Class<? extends JobFactory> factoryClazz = factories.get(0);
            JobFactory factory = factoryBuilder.apply(factoryClazz);
            jobs.add(factory.get(group, dispatcher));
        }

        for (Job job : jobs) {
            job.init();
        }
    }

    @Override
    public void start() {
        jobs.forEach(Job::start);
    }

    @Override
    public void stop() {
        jobs.forEach(Job::stop);
    }


}
