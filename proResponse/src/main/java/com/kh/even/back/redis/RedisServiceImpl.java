package com.kh.even.back.redis;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void setValues(String key, String value, Duration duration) {
        stringRedisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public String getValues(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean checkExistsValue(String value) {
        // 코드가 "값이 null/빈값이 아니면 존재" 의미로 쓰고 있으니 이렇게 구현
        return value != null && !value.isBlank();
    }

    @Override
    public void deleteValues(String key) {
        stringRedisTemplate.delete(key);
    }
}
