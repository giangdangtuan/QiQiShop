package com.app85soft.qiqishop.other_service.rate_limit;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Log4j2
@Service
@RequiredArgsConstructor
public class RateLimitService {
    private final StringRedisTemplate redisTemplate;

    public boolean isAllowed(String clientCode, int limitTps) {
        final long timeWindowInSeconds = 1;
        String redisKey = "rate_limit:" + clientCode;
        Long currentCount = redisTemplate.opsForValue().increment(redisKey);
        if (currentCount == null) {
            return true;
        }
        if (currentCount == 1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(timeWindowInSeconds));
        }
        return currentCount <= limitTps;
    }

    public boolean isNotAllowed(String clientCode, int limitTps) {
        return !isAllowed(clientCode, limitTps);
    }

}
