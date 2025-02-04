package com.cokebook.tools.tikoy.dispatcher;

import com.cokebook.tools.tikoy.mapping.Handler;
import com.cokebook.tools.tikoy.mapping.HandlerMapping;

/**
 * @date 2025/2/3
 */
public class LogDispatcherImpl implements LogDispatcher {

    private HandlerMapping handlerMapping;
    private Handler handlerNotFoundHandler = ((record) -> {
    });

    public LogDispatcherImpl(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public LogDispatcherImpl(HandlerMapping handlerMapping, Handler handlerNotFoundHandler) {
        this.handlerMapping = handlerMapping;
        this.handlerNotFoundHandler = handlerNotFoundHandler;
    }

    @Override
    public void handle(String group, Log record) {
        Handler handler = handlerMapping.getHandler(group, record, handlerNotFoundHandler);
        handler.handle(record);
    }
}
