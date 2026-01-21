package com.kh.even.back.redis;

import java.time.Duration;

public interface RedisService {
	
	void setValues(String key, String value, Duration duration);
	
    String getValues(String key);
    
    boolean checkExistsValue(String value);
    
    void deleteValues(String key); // 인증 성공 시 삭제하려면 필요(권장)

}
