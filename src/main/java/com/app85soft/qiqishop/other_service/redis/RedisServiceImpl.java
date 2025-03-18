package com.app85soft.qiqishop.other_service.redis;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Log4j2
@Service
class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void addCache(String key, Object value) {
        addCache(key, value, 1);
    }

    @Override
    public void addCache(String key, Object value, long timeout) {
        addCache(key, value, timeout, TimeUnit.HOURS);
    }

    @Override
    public void addCache(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            if (value == null) {
                flushCache(key);
            } else {
                redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCache(String key) {
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public void flushCache(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void flushAllCache() {
    }
}
