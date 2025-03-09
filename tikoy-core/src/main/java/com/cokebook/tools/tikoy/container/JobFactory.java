package com.cokebook.tools.tikoy.container;

import com.cokebook.tools.tikoy.dispatcher.LogDispatcher;

/**
 * Job Factory
 *
 * @date 2025/1/26
 */
public interface JobFactory {
    /**
     * 创建一个Job
     *
     * @param id         job id
     * @param dispatcher log dispatcher
     * @return Job
     */
    Job get(String id, LogDispatcher dispatcher);
}
