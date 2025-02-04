package com.cokebook.tools.tikoy.mapping;

import java.util.Arrays;

/**
 * @date 2024/11/1
 */
public enum Op {
    INSERT, UPDATE, DELETE;


    public static Op of(String type) {
        return Arrays.stream(Op.values()).filter(op -> op.name().equalsIgnoreCase(type))
                .findFirst().orElse(null);
    }

}
