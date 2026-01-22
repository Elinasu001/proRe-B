package com.kh.even.back.mail.model.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kh.even.back.exception.EmailAuthCooltimeException;
import com.kh.even.back.mail.model.dto.EmailVerificationResult;
import com.kh.even.back.redis.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAuthServiceImpl implements EmailAuthService {
	
	private final RedisService redisService;
	private final MailSendService mailSendService;
	
	private static final String CODE_KEY_PREFIX = "email:code:"; 	 	 // 인증코드 저장
	private static final String TRY_KEY_PREFIX = "email:try:";   	 	 // 시도횟수 저장
	private static final String COOLDOWN_PREFIX = "email:cooldown:"; 	 // 재전송 쿨타임
	private static final String VERIFIED_KEY_PREFIX = "email:verified:"; // 이메일 인증완료 상태 저장

	
	@Value("${app.email-auth.ttl-seconds:180}") // 3분
	private long ttlSeconds; 
	
	@Value("${app.email-auth.max-tries:5}") // 최대 5회
	private long maxTries;
	
	@Value("${app.email-auth.resend-cooldown-seconds:30}") // 재전송 30초
	private long resendCooldownSeconds;
	
	/**
	 * 인증코드 발송
	 */
	@Override
	public void sendCode(String email) {
		
		// 재전송 쿨타임 체크
		String cooldownKey = COOLDOWN_PREFIX + email;
		if(redisService.hasKey(cooldownKey)) {
			throw new EmailAuthCooltimeException("30초 후에 다시 시도해주세요.");
		}
		
		// 인증코드 생성(6자리)
		String code = generate6Digits();
		
		// Redis에 인증코드 저장 + TTL 설정
		// - key : email:code:{email}
		// - value : 6자리 코드
		String codeKey = CODE_KEY_PREFIX + email;
		redisService.setValues(codeKey, code, Duration.ofSeconds(ttlSeconds));
		
		// 새 코드 발급 시 시도횟수  초기화
		String tryKey = TRY_KEY_PREFIX + email;
		redisService.deleteValues(tryKey);
		
		// 쿨타임 키 세팅 -- cooldownKey가 존재하는 동안에는 재전송 불가능
		redisService.setValues(cooldownKey, "1", Duration.ofSeconds(resendCooldownSeconds));
		
		// 메일 발송
		String title = "이메일 인증번호";
		try {
			mailSendService.sendVerificationEmail(email, title, code, ttlSeconds);
		} catch(RuntimeException e) {
			// 메일 발송 실패하면 Redis에 코드가 남지 않도록 삭제해주고 예외처리
			redisService.deleteValues(codeKey);
			redisService.deleteValues(tryKey);
			throw e;
		}
		
	}
	
	/**
	 * 인증코드 검증
	 */
	@Override
	public EmailVerificationResult verify(String email, String inputCode) {
		
		// Redis에 저장된 인증코드 조회
		String codeKey = CODE_KEY_PREFIX + email;
		String savedCode = redisService.getValues(codeKey);
		
		// 코드가 없으면 만료 또는 미발급 상태
		if(!redisService.checkExistsValue(savedCode)) {
			return EmailVerificationResult.fail("인증번호가 만료되었거나 존재하지 않습니다.");
		}
		
		// 시도횟수 증가
		String tryKey = TRY_KEY_PREFIX + email;
		Long tries = redisService.increment(tryKey);
		
		// tryKey에 TTL을 걸어주기
		if(tries != null && tries == 1L) {
			redisService.expire(tryKey, Duration.ofSeconds(ttlSeconds));
		}
		
		// 시도횟수 초과 처리
		if(tries != null && tries > maxTries) {
			redisService.deleteValues(codeKey);
			redisService.deleteValues(tryKey);
			return EmailVerificationResult.fail("인증번호 검사는 5회까지만 가능합니다.");
		}
		
		// 인증코드 비교
		boolean match = savedCode.equals(inputCode);
		
		// 인증코드 성공 시 코드 삭제 + 시도횟수 삭제
		if(!match) {
			return EmailVerificationResult.fail("이메일 인증에 실패했습니다.");
		}
		redisService.deleteValues(codeKey);
		redisService.deleteValues(tryKey);
		
		String verifiedKey = VERIFIED_KEY_PREFIX + email;
		redisService.setValues(verifiedKey, "1", Duration.ofMinutes(10));
		
		// 결과 DTO 반환
		return EmailVerificationResult.success();		
	}
	
	/**
	 * 6자리 인증코드 생성 (000000 ~ 999999)
	 */
	private String generate6Digits() {
	    int n = java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 1_000_000);
	    return String.format("%06d", n);
	}
	
}
