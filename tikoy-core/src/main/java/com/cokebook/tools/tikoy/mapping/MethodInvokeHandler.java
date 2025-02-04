package com.cokebook.tools.tikoy.mapping;

import com.cokebook.tools.tikoy.dispatcher.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @date 2024/11/11
 */
public class MethodInvokeHandler implements
        Handler {

    private Object target;
    private Method method;

    public MethodInvokeHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public void handle(Log record) {
        try {
            method.invoke(target, record);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }


}
