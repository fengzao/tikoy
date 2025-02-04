package com.cokebook.tools.tikoy.support;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @date 2025/2/3
 */
public class Props {

    static Map<Class, Map<String, String>> clazzFieldRefPropKeyMap = new ConcurrentHashMap<>();


    static Map<String, String> get(Class clazz) {
        final Class userClass = ClassUtils.getUserClass(clazz);
        clazzFieldRefPropKeyMap.computeIfAbsent(userClass, (tClazz) -> {
            Map<String, String> fieldRefKeyMap = new HashMap<>(16);
            ReflectionUtils.doWithFields(tClazz, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    Key key = field.getAnnotation(Key.class);
                    if (key != null && key.value() != null) {
                        fieldRefKeyMap.put(field.getName(), key.value());
                    }
                }
            });
            return fieldRefKeyMap;
        });
        return clazzFieldRefPropKeyMap.get(userClass);
    }

    public static <T> T of(Function<String, String> env, String prefix, Class<T> clazz) {

        final Map<String, Object> fieldRefValueMap = new HashMap<>(16);
        get(clazz).entrySet().stream().forEach(entry -> {
                    String fullKey = prefix == null ? entry.getValue() : (prefix + "." + entry.getValue());
                    fieldRefValueMap.put(entry.getKey(), env.apply(fullKey));
                }
        );
        return JSON.to(clazz, fieldRefValueMap);
    }

    public static Map<String, Object> toMap(Object object) {
        if (object == null) {
            return Collections.emptyMap();
        }
        JSONObject json = (JSONObject) JSON.toJSON(object);
        final Map<String, Object> resultMap = new HashMap<>(16);
        get(ClassUtils.getUserClass(object)).entrySet()
                .forEach(entry -> {
                    resultMap.put(entry.getValue(), json.get(entry.getKey()));
                });

        return resultMap;
    }


    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Key {
        /**
         * prop key name
         *
         * @return
         */
        String value();
    }

}
