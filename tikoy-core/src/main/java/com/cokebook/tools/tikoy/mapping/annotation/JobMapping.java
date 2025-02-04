package com.cokebook.tools.tikoy.mapping.annotation;

import com.cokebook.tools.tikoy.container.JobFactory;

import java.lang.annotation.*;

/**
 * @date 2024/11/1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
public @interface JobMapping {

    String ALL = "*";

    String id() default ALL;

    String[] db() default {ALL};

    Class<? extends JobFactory> factory() default JobFactory.class;


}
