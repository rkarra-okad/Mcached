package com.okad.mcached.core;

import java.util.Map;

public interface Memcache {

    Object get(String key);

    Map<String, Object> getMulti(String... keys);

    void set(String key, int cacheTimeSeconds, Object o);

    void add(String key, int cacheTimeSeconds, Object o);

    void delete(String key);

    void incr(String key, int factor, int startingValue);

    void shutdown();

    void clear();
}
