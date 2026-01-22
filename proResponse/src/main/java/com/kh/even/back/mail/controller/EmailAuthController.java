package com.kh.even.back.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.common.ResponseData;
import com.kh.even.back.mail.model.dto.EmailSendRequestDTO;
import com.kh.even.back.mail.model.dto.EmailVerificationResult;
import com.kh.even.back.mail.model.dto.EmailVerifyRequestDTO;
import com.kh.even.back.mail.model.service.EmailAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class EmailAuthController {
	
	private final EmailAuthService emailAuthService;
	
	/**
	 * 인증코드 발송
	 * @param request 이메일
	 * @return 공통 응답 메세지
	 */
	@PostMapping("/verification-requests")
	public ResponseEntity<ResponseData<Void>> sendVerificationCode(@Valid @RequestBody EmailSendRequestDTO request) {
		
		emailAuthService.sendCode(request.getEmail());
		
		return ResponseData.ok(null, "인증번호가 발송됐습니다.");
	}
	
	/**
	 * 인증코드 검증
	 * @param request 이메일 + 인증번호
	 * @return 인증 결과 DTO 반환 (true / false)
	 */
	@PostMapping("/verifications")
	public ResponseEntity<ResponseData<EmailVerificationResult>> verifyCode(@Valid @RequestBody EmailVerifyRequestDTO request) {
		
		EmailVerificationResult result = emailAuthService.verify(request.getEmail(), request.getCode());
		
		String message = result.isVerified()
				? "인증에 성공했습니다."
				: result.getMessage();
		
		return ResponseData.ok(result, message);
	}
	
	
}
