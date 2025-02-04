package com.cokebook.tools.tikoy.travelling;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @date 2025/1/25
 */
public interface ParamExtractor extends Function<List<Map<String, Object>>, Map<String, Object>> {


    /**
     * of
     *
     * @param target
     * @param initValue
     * @return
     */
    static ParamExtractor of(ParamExtractor target, Supplier<Map<String, Object>> initValue) {
        return mapList -> {
            if (mapList == null || mapList.isEmpty()) {
                return initValue.get();
            }
            return target.apply(mapList);
        };
    }

}
