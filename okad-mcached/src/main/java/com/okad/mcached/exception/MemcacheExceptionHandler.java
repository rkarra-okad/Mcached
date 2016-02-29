package com.okad.mcached.exception;

public interface MemcacheExceptionHandler {

    void handleErrorOnGet(String key, Exception e);

    void handleErrorOnSet(String key, int cacheTimeSeconds, Object o, Exception e);

    void handleErrorOnAdd(String key, int cacheTimeSeconds, Object o, Exception e);

    void handleErrorOnDelete(String key, Exception e);

    void handleErrorOnIncr(String key, int factor, int startingValue, Exception e);

    void handleErrorOnFlush(Exception e);

    void handleErrorOnShutdown();

}
