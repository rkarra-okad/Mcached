package com.okad.mcached.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistCacheEvict {

    /**
     * Cache name
     */
    String name();

    /**
     * Cache Keys element
     */
    int[] keys() default {};

    boolean AllEntries() default false;

}
