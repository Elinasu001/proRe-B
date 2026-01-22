package com.kh.even.back.redis;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
	
	/**
	 * 스프링이 Redis 연결정보(.yml)를 보고 자동으로 생성해주는 템플릿
	 * Redis Cloud에 붙는다.
	 */
    private final StringRedisTemplate redisTemplate;

    @Override
    public void setValues(String key, String value, Duration duration) {
        
    	// opsForValue(): Redis의 String(Value) 자료구조를 다루는 연산자
    	// set(key, value, duration): 저장과 동시에 TTL 설정
    	redisTemplate.opsForValue().set(key, value, duration);
    }
    
    /**
     * key에 해당하는 value 조회
     * key가 없거나 만료되면 null 반환
     */
    @Override
    public String getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 조회결과(value) 유효여부 체크
     */
    @Override
    public boolean checkExistsValue(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * 인증성공 또는 시도횟수 초기화 시에 key 삭제
     */
    @Override
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
    
    /**
     * key 존재여부 확인
     */
    @Override
    public boolean hasKey(String key) {
    	Boolean has = redisTemplate.hasKey(key);
    	return has != null & has;
    }
    
    /**
     * 숫자값 증가(increment)
     * key가 없을 경우 Redis가 "1"로 만들어서 반환
     */
    @Override
    public Long increment(String key) {
    	return redisTemplate.opsForValue().increment(key);
    }
    
    /**
     * TTL 설정/갱신
     * 시도횟수 키를 인증코드와 비슷한 시간에 자동 삭제하는 용도
     */
    @Override
    public void expire(String key, Duration duration) {
    	redisTemplate.expire(key, duration);
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
