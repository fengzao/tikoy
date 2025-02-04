package com.cokebook.tools.tikoy.travelling.extractor;

import com.cokebook.tools.tikoy.travelling.ParamExtractor;

import java.util.List;
import java.util.Map;

/**
 * @date 2025/1/25
 */
public class CacheParamExtractor implements ParamExtractor {


    /**
     * cache extractor
     *
     * @param extractor
     * @return
     */
    public static CacheParamExtractor of(ParamExtractor extractor) {
        return new CacheParamExtractor(extractor);
    }


    ParamExtractor target;
    private Map<String, Object> cache;

    private CacheParamExtractor(ParamExtractor target) {
        this.target = target;
    }

    @Override
    public Map<String, Object> apply(List<Map<String, Object>> rows) {
        cache = target.apply(rows);
        return cache;
    }

    public Map<String, Object> get() {
        return cache;
    }
}