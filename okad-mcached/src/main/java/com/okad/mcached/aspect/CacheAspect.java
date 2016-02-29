package com.okad.mcached.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.okad.mcached.annotation.DistCacheEvict;
import com.okad.mcached.annotation.DistCachePut;
import com.okad.mcached.annotation.DistCacheable;
import com.okad.mcached.key.KeyGenerator;
import com.okad.mcached.service.ServiceCache;

@Component
@Aspect
@Order(6)
public class CacheAspect {

    private final Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);

    @Autowired
    private ServiceCache serviceCache;

    @Autowired
    private KeyGenerator keyGenerator;

    @Around("@annotation(distCacheable)")
    @Order(7)
    public Object checkCacheableAnnotation(ProceedingJoinPoint joinPoint, DistCacheable distCacheable)
            throws Throwable {

        Object result = null;

        if (!serviceCache.useCache()) {
            result = joinPoint.proceed();

        } else {

            // build a key
            String key = buildKey(joinPoint, distCacheable.name(), distCacheable.keys());

            // look into the cache
            result = serviceCache.get(key);

            // resource not found in the cache
            if (result == null) {
                LOGGER.info("I did not find an item in the cache, looking base, key : [" + key + "]");
                // get the resource from the db and add it to the cache
                result = joinPoint.proceed();

                serviceCache.add(key, distCacheable.ttl(), result);

            } else {
                LOGGER.info("I recovered objects from the cache, key : [" + key + "]");
            }

        }

        return result;
    }

    @Around("@annotation(distCachePut)")
    @Order(8)
    public Object checkCachePutAnnotation(ProceedingJoinPoint joinPoint, DistCachePut distCachePut) throws Throwable {

        Object result = null;

        if (!serviceCache.useCache()) {
            result = joinPoint.proceed();

        } else {

            // build a key
            String key = buildKey(joinPoint, distCachePut.name(), distCachePut.keys());

            result = joinPoint.proceed();

            serviceCache.set(key, distCachePut.ttl(), result);
        }

        return result;
    }

    @Around("@annotation(distCacheEvict)")
    @Order(8)
    public Object checkCacheEvictAnnotation(ProceedingJoinPoint joinPoint, DistCacheEvict distCacheEvict)
            throws Throwable {

        if (!serviceCache.useCache()) {
            return joinPoint.proceed();

        } else {

            if (distCacheEvict.AllEntries()) {
                serviceCache.clear();
            } else {
                serviceCache.evict(buildKey(joinPoint, distCacheEvict.name(), distCacheEvict.keys()));
            }
        }

        return joinPoint.proceed();
    }

    private String buildKey(ProceedingJoinPoint joinPoint, String cache, int[] index) {
        return keyGenerator.generate(((MethodSignature) joinPoint.getStaticPart().getSignature()).getMethod(), cache,
                index, joinPoint.getArgs());
    }
}
