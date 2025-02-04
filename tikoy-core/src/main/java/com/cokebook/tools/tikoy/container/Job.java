package com.cokebook.tools.tikoy.container;

import com.cokebook.tools.tikoy.support.Lifecycle;

/**
 * @date 2025/1/26
 */
@FunctionalInterface
public interface Job extends Lifecycle, Runnable {

    /**
     * default start
     */
    @Override
    default void start() {
        run();
    }
}
