package com.okad.mcached.core;

import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.okad.mcached.exception.LoggingMemcacheExceptionHandler;
import com.okad.mcached.exception.MemcacheExceptionHandler;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

@Component
public class MemcacheRepository implements Memcache {

    private final Logger LOGGER = LoggerFactory.getLogger(MemcacheRepository.class);

    private final MemcacheExceptionHandler exceptionHandler = new LoggingMemcacheExceptionHandler();

    @Autowired
    private MemcachedClient memcachedClient;

    @Override
    public Object get(String key) {

        try {

            return memcachedClient.get(key);

        } catch (Exception e) {
            exceptionHandler.handleErrorOnGet(key, e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getMulti(String... keys) {
        try {
            return memcachedClient.getBulk(keys);
        } catch (Exception e) {
            exceptionHandler.handleErrorOnGet(StringUtils.join(keys, ", "), e);
            return null;
        }
    }

    @Override
    public void set(String key, int cacheTimeSeconds, Object o) {
        try {
            memcachedClient.set(key, cacheTimeSeconds, o);
        } catch (Exception e) {
            exceptionHandler.handleErrorOnSet(key, cacheTimeSeconds, o, e);
        }
    }

    @Override
    public void add(String key, int cacheTimeSeconds, Object o) {
        OperationFuture<Boolean> of = memcachedClient.add(key, cacheTimeSeconds, o);
        try {
            of.get();
        } catch (Exception ex) {
            exceptionHandler.handleErrorOnAdd(key, cacheTimeSeconds, o, ex);
            of.cancel();
        }
    }

    @Override
    public void delete(String key) {
        OperationFuture<Boolean> of = memcachedClient.delete(key);
        try {
            of.get();
        } catch (Exception e) {
            exceptionHandler.handleErrorOnDelete(key, e);
            of.cancel();
        }
    }

    @Override
    public void incr(String key, int factor, int startingValue) {
        try {
            memcachedClient.incr(key, factor, startingValue);
        } catch (Exception e) {
            exceptionHandler.handleErrorOnIncr(key, factor, startingValue, e);
        }
    }

    @PreDestroy
    @Override
    public void shutdown() {
        try {
            LOGGER.debug("Shutting down spy MemcachedClient");
            memcachedClient.shutdown();
        } catch (Exception e) {
            exceptionHandler.handleErrorOnShutdown();
        }
    }

    @Override
    public void clear() {
        OperationFuture<Boolean> of = memcachedClient.flush();
        try {
            of.get();
        } catch (Exception ex) {
            exceptionHandler.handleErrorOnFlush(ex);
            of.cancel();
        }

    }
}
