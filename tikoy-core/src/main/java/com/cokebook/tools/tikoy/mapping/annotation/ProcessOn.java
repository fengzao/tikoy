package com.cokebook.tools.tikoy.mapping.annotation;


import com.cokebook.tools.tikoy.mapping.Op;

import java.lang.annotation.*;


/**
 * @date 2024/11/1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface ProcessOn {

    /**
     * db
     *
     * @return
     */
    String[] db() default {JobMapping.ALL};


    /**
     * table
     *
     * @return tables
     */
    String[] value() default {};


    Op[] type() default {};


}
