package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.mapping.annotation.ProcessOn;
import com.cokebook.tools.tikoy.support.TextResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @date 2024/11/11
 */
public class HandlerMappingImpl implements HandlerMapping {

    private final List<MatcherHandler> handlers = new CopyOnWriteArrayList<>();

    private final Map<String, List<Matcher>> groupMatcherMap = new HashMap<>();

    protected final List<JobMapping> jobMappings = new ArrayList<>();

    private final TextResolver textResolver;

    public HandlerMappingImpl(TextResolver textResolver) {
        this.textResolver = textResolver != null ? textResolver : str -> str;
    }

    @Override
    public Handler getHandler(String group, Log record, Handler defaultHandler) {

        List<Handler> targetHandlers = handlers.stream().
                filter(handler -> handler.match(record.database(), record.table(), record.type(), group))
                .collect(Collectors.toList());

        if (targetHandlers.isEmpty()) {
            return defaultHandler;
        }
        return new HandlerChain(targetHandlers);
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
                List<Matcher> groupRefMatcherList = groupMatcherMap.computeIfAbsent(matcher.getGroup(), k -> new ArrayList<>());
                groupRefMatcherList.add(matcher);
                handlers.add(new MatcherHandler(matcher, new MethodInvokeHandler(object, method)));
            }
        });
    }


    @Override
    public Map<String, List<Matcher>> groupMatcherMap() {
        return groupMatcherMap;
    }


    @Override
    public List<JobMapping> schemaMappings() {
        return jobMappings;
    }

    private static class MatcherHandler implements Handler {
        private final Handler target;
        private final Matcher matcher;

        public MatcherHandler(Matcher matcher, Handler target) {
            this.target = target;
            this.matcher = matcher;
        }

        @Override
        public void handle(Log opLog) {
            target.handle(opLog);
        }

        public boolean match(String database, String table, Op op, String group) {
            return matcher.match(database, table, op, group);
        }
    }


    /**
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
            return Collections.emptyList();
        }
        return Arrays.stream(texts).map(this::resolvePlaceholders)
                .collect(Collectors.toList());
    }


}
