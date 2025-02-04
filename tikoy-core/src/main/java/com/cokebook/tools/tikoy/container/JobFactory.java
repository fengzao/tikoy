package com.cokebook.tools.tikoy.container;

import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;

/**
 * @date 2025/1/26
 */
public interface JobFactory {
    /**
     * create a jod for <code>id</code>
     *
     * @param id
     * @param dispatcher
     * @return
     */
    Job get(String id, LogDispatcher dispatcher);
}
