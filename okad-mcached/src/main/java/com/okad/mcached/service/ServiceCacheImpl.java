package com.okad.mcached.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.okad.mcached.core.Memcache;

@Service
public class ServiceCacheImpl implements ServiceCache {

    @Autowired
    protected Memcache cacheRepository;

    @Value("${epermits.memcache.usecache}")
    protected boolean useCache;

    @Override
    public boolean useCache() {
        return useCache;
    }

    @Override
    public Object get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return cacheRepository.get(key);
    }

    @Override
    public Map<String, Object> getMulti(String... keys) {
        return cacheRepository.getMulti(keys);
    }

    @Override
    public void set(String key, int cacheTimeSeconds, Object o) {
        if (!StringUtils.isEmpty(key) && o != null) {
            cacheRepository.set(key, cacheTimeSeconds, o);
        }
    }

    @Override
    public void add(String key, int cacheTimeSeconds, Object o) {
        if (!StringUtils.isEmpty(key) && o != null) {
            cacheRepository.add(key, cacheTimeSeconds, o);
        }
    }

    @Override
    public void evict(String key) {
        if (!StringUtils.isEmpty(key)) {
            cacheRepository.delete(key);
        }
    }

    @Override
    public void incr(String key, int factor, int startingValue) {
        if (!StringUtils.isEmpty(key)) {
            cacheRepository.incr(key, factor, startingValue);
        }
    }

    @Override
    public void shutdown() {
        cacheRepository.shutdown();
    }

    @Override
    public void clear() {
        cacheRepository.clear();
    }

}
