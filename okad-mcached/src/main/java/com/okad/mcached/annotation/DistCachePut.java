package com.okad.mcached.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistCachePut {

    /**
     * Cache name
     */
    String name();

    /**
     * Cache Keys element
     */
    int[] keys() default {};

    /**
     * Define the time-to-live of the given entry in the cache
     * 
     * @return
     */
    int ttl() default 600;

}
