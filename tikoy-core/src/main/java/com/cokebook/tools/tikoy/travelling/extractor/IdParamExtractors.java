package com.cokebook.tools.tikoy.travelling.extractor;

import com.cokebook.tools.tikoy.travelling.ParamExtractor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @date 2025/1/25
 */
public class IdParamExtractors {

    public static ParamExtractor max(Supplier<Long> initValue) {

        return ParamExtractor.of(rows -> {
            Long id = rows.stream().map(map -> ((Number) map.get("id")).longValue()).max(Long::compareTo).get();
            Map<String, Object> params = new HashMap<>(4);
            params.put("id", id);
            return params;
        }, () -> {
            Map<String, Object> params = new HashMap<>(4);
            params.put("id", initValue.get());
            return params;
        });
    }

    public static ParamExtractor min(Supplier<Long> initValue) {

        return ParamExtractor.of(rows -> {
            Long id = rows.stream().map(map -> ((Number) map.get("id")).longValue()).min(Long::compareTo).get();
            Map<String, Object> params = new HashMap<>(4);
            params.put("id", id);
            return params;
        }, () -> {
            Map<String, Object> params = new HashMap<>(4);
            params.put("id", initValue.get());
            return params;
        });
    }


}
