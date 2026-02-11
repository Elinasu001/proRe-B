package com.kh.even.back.mail.model.service;

import com.kh.even.back.mail.model.dto.EmailVerificationResult;

public interface EmailAuthService {
	
	/**
	 * 인증코드 발송
	 * @param email
	 */
	void sendCode(String email);
	
	/**
	 * 인증코드 검증
	 * @param email
	 * @param code
	 * @return 인증 결과 DTO 반환
	 */
	EmailVerificationResult verify(String email, String code);
	
	/**
	 * 임시비밀번호 발송
	 * @param email
	 */
	void sendTempPassword(String email);
	
	/**
	 * 비밀번호 초기화를 위해 인증코드 발송
	 * @param email
	 */
	void sendCodeForResetPwd(String email);
}
