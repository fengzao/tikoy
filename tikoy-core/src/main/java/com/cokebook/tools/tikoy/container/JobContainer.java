package com.cokebook.tools.tikoy.container;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;
import com.cokebook.tools.tikoy.dispatcher.LogDispatcherImpl;
import com.cokebook.tools.tikoy.mapping.Handler;
import com.cokebook.tools.tikoy.mapping.HandlerMapping;
import com.cokebook.tools.tikoy.mapping.HandlerMappingImpl;
import com.cokebook.tools.tikoy.mapping.Matcher;
import com.cokebook.tools.tikoy.support.TextResolver;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.support.Lifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @date 2025/1/26
 */
public class JobContainer implements HandlerMapping, Lifecycle {
    private final LogDispatcher dispatcher;
    private final HandlerMappingImpl handlerMapping;
    public Function<Class<? extends JobFactory>, ? extends JobFactory> factoryBuilder;
    private final List<Job> jobs = new ArrayList<>();

    public JobContainer(TextResolver textResolver,
                        Function<Class<? extends JobFactory>, ? extends JobFactory> factoryBuilder) {
        this.factoryBuilder = factoryBuilder;
        this.handlerMapping = new HandlerMappingImpl(textResolver);
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

    public LogDispatcher getLogDispatcher() {
        return this.dispatcher;
    }

    @Override
    public void init() {
        Map<String, List<JobMapping>> groupRefMappings = handlerMapping.schemaMappings().stream()
                .collect(Collectors.groupingBy(JobMapping::id));

        for (String group : groupRefMappings.keySet()) {
            Set<Class<? extends JobFactory>> factories = groupRefMappings.get(group).stream()
                    .map(JobMapping::factory)
                    .filter(clazz -> clazz != JobFactory.class)
                    .collect(Collectors.toSet());
            if (factories.size() > 1) {
                throw new IllegalStateException("schema mapping group = '" + group + "' has more than one job factory");
            }
            if (factories.isEmpty()) {
                continue;
            }
            Class<? extends JobFactory> factoryClazz = factories.stream().findFirst().get();
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


    @Override
    public Map<String, List<Matcher>> groupMatcherMap() {
        return handlerMapping.groupMatcherMap();
    }
}
