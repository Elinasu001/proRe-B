package com.kh.even.back.redis;

import java.time.Duration;

public interface RedisService {
	
	/**
	 * key에 value를 저장하고 TTL(만료시간)을 설정
	 * @param key 레디스 키
	 * @param value 인증코드
	 * @param duration 만료시간
	 */
	void setValues(String key, String value, Duration duration);
	
	/**
	 * key에 저장된 코드 조회
	 * @param key 레디스 키
	 * @return 저장된 코드 또는 NULL
	 */
    String getValues(String key);
    
    /**
     * value가 존재하는지 체크
     * @param value 인증코드와 이메일
     * @return
     */
    boolean checkExistsValue(String value);
    
    /**
     * key를 삭제 
     * @param key 레디스 키
     */
    void deleteValues(String key); // 인증 성공 시 삭제하려면 필요(권장)
    
    /**
     * Key 존재여부 확인
     * 재전송 쿨타임 키가 존재하는지 확인하여 재전송 가능 여부 판단
     * @param key 레디스 키
     * @return true / false
     */
    boolean hasKey(String key);
    
    /**
     * Key에 저장된 숫자값을 1증가
     * @param key 시도횟수 + 이메일
     * @return 증가 후 값
     */
    Long increment(String key);
    
    /**
     * 시도횟수에 유효시간 걸어주기
     * @param key 시도횟수 + 이메일
     * @param duration 만료시간
     */
    void expire(String key, Duration duration);

}
