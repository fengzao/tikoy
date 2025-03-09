package com.cokebook.tools.tikoy.mapping;

import java.util.function.Function;

/**
 * Text Resolver
 * express : "${a.b.c}"
 */
@FunctionalInterface
public interface TextResolver {
    /**
     * string placeholder replace
     *
     * @param str str text
     * @return placeholder replaced str
     */
    String apply(String str);
}
