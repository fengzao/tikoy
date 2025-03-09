package com.cokebook.tools.tikoy.dispatcher.log;

/**
 * 字符串反序列化
 *
 * @param <T>
 */
public interface Deserializer<T> {

    /**
     * 对字符串进行反序列化
     *
     * @param str 字符串内容
     * @return 反序列化后对象
     */
    T apply(String str);
}
