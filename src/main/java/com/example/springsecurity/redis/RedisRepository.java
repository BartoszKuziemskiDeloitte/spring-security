package com.example.springsecurity.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public String save(String token) {
        redisTemplate.opsForValue().set(token, token);
        redisTemplate.expire(token, Duration.ofSeconds(30));
        return token;
    }

    public String findToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

}
