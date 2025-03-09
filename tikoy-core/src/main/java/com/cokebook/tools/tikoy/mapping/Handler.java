package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;

/**
 * @date 2024/11/11
 */
public interface Handler {

    /**
     * 记录处理
     *
     * @param opLog database operate log
     */
    void handle(Log opLog);

}
