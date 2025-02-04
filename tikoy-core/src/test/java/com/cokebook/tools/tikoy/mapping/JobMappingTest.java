package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.mapping.annotation.ProcessOn;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import org.junit.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @date 2024/11/1
 */
public class JobMappingTest {

    @Test
    public void test() {

        final Map<String, Method> pathMapping = new HashMap<>();

        JobMapping jobMapping = AnnotationUtils.findAnnotation(Handler.class, JobMapping.class);
        if (jobMapping == null) {
            return;
        }

        ReflectionUtils.doWithLocalMethods(Handler.class, method -> {
            ProcessOn processOn = AnnotatedElementUtils.findMergedAnnotation(method, ProcessOn.class);
            if (processOn != null) {
                String[] mappingDatabases = jobMapping.db().length == 0 ? new String[]{""} : jobMapping.db();
                String[] mappingTables = processOn.value().length == 0 ? new String[]{""} : processOn.value();
                System.out.println("method = " + method.getName() + ",  value = " + Arrays.stream(processOn.value()).collect(Collectors.joining(",")));

                for (String path : mappingDatabases) {
                    for (String mPath : mappingTables) {
                        Op[] supportedOps = processOn.type();
                        for (Op type : supportedOps) {
                            pathMapping.put(type.name() + ":" + path + mPath, method);
                        }
                    }
                }

            }
        });

        for (String key : pathMapping.keySet()) {
            System.out.println(key + " ----- " + pathMapping.get(key));
        }
    }

    public static class PathMapping {
        private Op ops;
        private String path;
        private Method method;
    }


}
