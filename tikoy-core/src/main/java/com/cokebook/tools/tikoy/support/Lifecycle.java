package com.cokebook.tools.tikoy.support;

/**
 * @date 2025/1/26
 */
public interface Lifecycle {

    /**
     * init
     */
    default void init() {
    }

    /**
     * start
     */
    default void start() {
    }

    /**
     * stop
     */
    default void stop() {
    }

}
