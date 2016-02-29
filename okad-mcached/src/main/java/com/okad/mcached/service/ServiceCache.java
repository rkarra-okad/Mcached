package com.okad.mcached.service;

import java.util.Map;

public interface ServiceCache {

    boolean useCache();

    Object get(String key);

    Map<String, Object> getMulti(String... keys);

    void set(String key, int cacheTimeSeconds, Object o);

    void add(String key, int cacheTimeSeconds, Object o);

    void evict(String key);

    void incr(String key, int factor, int startingValue);

    void shutdown();

    void clear();
}
