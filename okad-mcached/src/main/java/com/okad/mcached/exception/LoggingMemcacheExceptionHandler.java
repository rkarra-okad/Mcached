package com.okad.mcached.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingMemcacheExceptionHandler implements MemcacheExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(LoggingMemcacheExceptionHandler.class);

    @Override
    public void handleErrorOnGet(String key, Exception e) {
        LOGGER.warn("Cache 'get' failed for key [" + key + "]");
    }

    @Override
    public void handleErrorOnSet(String key, int cacheTimeSeconds, Object o, Exception e) {
        LOGGER.warn("Cache 'set' failed for key [" + key + "]");
    }

    @Override
    public void handleErrorOnDelete(String key, Exception e) {
        LOGGER.warn("Cache 'delete' failed for key [" + key + "]");
    }

    @Override
    public void handleErrorOnIncr(String key, int factor, int startingValue, Exception e) {
        LOGGER.warn("Cache 'incr' failed for key [" + key + "]");
    }

    @Override
    public void handleErrorOnAdd(String key, int cacheTimeSeconds, Object o, Exception e) {
        LOGGER.warn("Cache 'add' failed for key [" + key + "]");
    }

    @Override
    public void handleErrorOnFlush(Exception e) {
        LOGGER.warn("Cache 'flush' failed");
    }

    @Override
    public void handleErrorOnShutdown() {
        LOGGER.warn("Cache 'shutdown' failed");
    }
}
