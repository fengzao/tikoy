package com.cokebook.tools.tikoy.support;

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
