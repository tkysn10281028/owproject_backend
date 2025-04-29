package com.oysterworld.portfolio.owproject_backend.common;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    private static StringRedisTemplate redisTemplate;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(5);
    @Autowired
    public RedisUtil(StringRedisTemplate template) {
        RedisUtil.redisTemplate = template;
    }
 
    public static void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value, DEFAULT_TIMEOUT);
    }

    public static String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
