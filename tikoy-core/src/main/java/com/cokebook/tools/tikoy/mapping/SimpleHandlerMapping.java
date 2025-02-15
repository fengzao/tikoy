package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.mapping.annotation.ProcessOn;
import com.cokebook.tools.tikoy.support.Table;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @date 2024/11/11
 */
public class SimpleHandlerMapping implements HandlerMapping {

    private final List<MatcherHandler> handlers = new CopyOnWriteArrayList();

    private final Map<String, List<Matcher>> groupMatcherMap = new HashMap<>();

    protected final List<JobMapping> jobMappings = new ArrayList<>();

    private Function<String, String> textResolver;

    public SimpleHandlerMapping(Function<String, String> textResolver) {
        this.textResolver = textResolver != null ? textResolver : Function.identity();
    }

    @Override
    public Handler getHandler(String group, Log record, Handler defaultHandler) {

        List<Handler> targetHandlers = handlers.stream().
                filter(handler -> handler.match(record.database(), record.table(), record.type(), group))
                .collect(Collectors.toList());

        if (targetHandlers.isEmpty()) {
            return defaultHandler;
        }
        return new ChainHandler(targetHandlers);
    }


    @Override
    public void register(Object object) {
        JobMapping jobMapping = AnnotatedElementUtils.findMergedAnnotation(object.getClass(), JobMapping.class);
        jobMappings.add(jobMapping);
        ReflectionUtils.doWithMethods(ClassUtils.getUserClass(object), method -> {
            ProcessOn processOn = AnnotatedElementUtils.findMergedAnnotation(method, ProcessOn.class);
            if (processOn != null && processOn.type().length > 0) {
                Matcher matcher = new Matcher(
                        inter(resolvePlaceholders(jobMapping.db()), resolvePlaceholders(processOn.db())),
                        resolvePlaceholders(processOn.value()),
                        Arrays.asList(processOn.type()),
                        resolvePlaceholders(jobMapping.id())
                );
                List<Matcher> groupRefMatcherList = groupMatcherMap.get(matcher.group);
                if (groupRefMatcherList == null) {
                    groupRefMatcherList = new ArrayList<>();
                    groupMatcherMap.put(matcher.group, groupRefMatcherList);
                }
                groupRefMatcherList.add(matcher);
                handlers.add(new MatcherHandler(matcher, new MethodInvokeHandler(object, method)));
            }
        });
    }

    @Override
    public List<Table> snapshotTable(String group) {
        return groupMatcherMap.getOrDefault(group, Collections.emptyList())
                .stream().filter(matcher -> {
                    if (!matcher.ops.contains(Op.INSERT)) {
                        return false;
                    }
                    return matcher.databases.size() == 1
                            && !matcher.databases.contains(JobMapping.ALL);
                }).map(matcher -> {
                    String database = new ArrayList<>(matcher.databases).get(0);
                    return matcher.tables.stream().filter(tableName -> !Objects.equals(tableName, JobMapping.ALL))
                            .map(table -> new Table(database, table))
                            .collect(Collectors.toList());
                }).flatMap(tables -> tables.stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<JobMapping> schemaMappings() {
        return jobMappings;
    }

    private static class MatcherHandler implements Handler {
        private Handler target;
        private Matcher matcher;

        public MatcherHandler(Matcher matcher, Handler target) {
            this.target = target;
            this.matcher = matcher;
        }

        @Override
        public void handle(Log record) {
            target.handle(record);
        }

        public boolean match(String database, String table, Op op, String group) {
            return matcher.match(database, table, op, group);
        }
    }


    private static class Matcher {


        protected Matcher(Collection<String> databases,
                          Collection<String> tables,
                          Collection<Op> ops,
                          String group
        ) {
            this(new HashSet<>(databases), new HashSet<>(tables), new HashSet<>(ops), group);
        }


        protected Matcher(Set<String> databases, Set<String> tables, Set<Op> ops, String group) {
            this.databases = databases;
            this.tables = tables;
            this.ops = ops;
            this.group = group;
        }

        private String group;
        private Set<String> databases;
        private Set<String> tables;
        private Set<Op> ops;


        public boolean match(String database, String table, Op op, String group) {
            return (this.group == JobMapping.ALL || Objects.equals(this.group, group)) &&
                    (this.databases.contains(JobMapping.ALL) || this.databases.contains(database))
                    && (this.tables.contains(JobMapping.ALL) || this.tables.contains(table))
                    && (this.ops.contains(op));
        }

    }

    /**
     * 求集合交集
     *
     * @param c1
     * @param c2
     * @return
     */
    private static <T> Collection<T> inter(Collection<T> c1, Collection<T> c2) {

        if (c1 == null || c1.isEmpty() || c2 == null || c2.isEmpty()) {
            return Collections.emptySet();
        }
        if (c1.contains(JobMapping.ALL)) {
            return new HashSet<>(c2);
        }
        if (c2.contains(JobMapping.ALL)) {
            return new HashSet<>(c1);
        }
        return c1.stream().filter(c2::contains).collect(Collectors.toSet());

    }

    protected String resolvePlaceholders(String text) {
        return text == null ? null : textResolver.apply(text);
    }

    protected List<String> resolvePlaceholders(String[] texts) {
        if (texts == null || texts.length == 0) {
            Collections.emptyList();
        }
        return Arrays.stream(texts).map(text -> resolvePlaceholders(text))
                .collect(Collectors.toList());
    }


}
