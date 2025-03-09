package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;

import java.util.List;

/**
 * @date 2024/11/12
 */
public class HandlerChain implements Handler {
    private final List<Handler> handlerList;

    public HandlerChain(List<Handler> handlerList) {
        this.handlerList = handlerList;
    }

    @Override
    public void handle(Log opLog) {
        handlerList.forEach(handler -> handler.handle(opLog));
    }
}
