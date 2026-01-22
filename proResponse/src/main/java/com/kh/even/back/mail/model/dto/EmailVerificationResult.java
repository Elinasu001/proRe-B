package com.kh.even.back.mail.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 이메일 인증 "결과" DTO
 * EmailAuthController의 verifyCode() API의 응답 data
 */
@Getter
@RequiredArgsConstructor
public class EmailVerificationResult {
	
	private final boolean verified;
	private final String message; // 실패 사유 (성공이면 null)
	
	public static EmailVerificationResult success() {
		return new EmailVerificationResult(true, null);
	}
	
	public static EmailVerificationResult fail(String message) {
		return new EmailVerificationResult(false, message);
	}
}
