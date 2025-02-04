package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;

import java.util.List;

/**
 * @date 2024/11/12
 */
public class ChainHandler implements Handler {
    private List<Handler> handlerList;

    public ChainHandler(List<Handler> handlerList) {
        this.handlerList = handlerList;
    }

    @Override
    public void handle(Log record) {
        handlerList.forEach(handler -> handler.handle(record));
    }
}
