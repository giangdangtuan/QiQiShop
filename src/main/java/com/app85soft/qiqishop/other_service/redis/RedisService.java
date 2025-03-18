package com.app85soft.qiqishop.other_service.redis;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void addCache(String key, Object value);
    void addCache(String key, Object value, long timeout);
    void addCache(String key, Object value, long timeout, TimeUnit timeUnit);
    <T> T getCache(String key);
    void flushCache(String key);
    void flushAllCache();
}
