package com.cokebook.tools.tikoy.mapping.annotation;

import com.cokebook.tools.tikoy.mapping.Op;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @date 2024/11/1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ProcessOn(type = Op.INSERT)
public @interface OnInsert {

    /**
     * database name
     *
     * @return
     */
    @AliasFor(annotation = ProcessOn.class)
    String[] db() default {JobMapping.ALL};

    /**
     * table name
     *
     * @return tables
     */
    @AliasFor(annotation = ProcessOn.class)
    String[] value() default {};

}
