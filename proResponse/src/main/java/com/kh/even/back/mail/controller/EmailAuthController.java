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
import com.kh.even.back.mail.model.service.EmailAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mails")
public class EmailAuthController {
	
	private final EmailAuthService emailAuthService;
	
	/**
	 * 인증코드 발송
	 * @param request
	 * @return
	 */
	@PostMapping("/verification-requests")
	public ResponseEntity<ResponseData<Void>> sendVerificationCode(@Valid @RequestBody EmailSendRequestDTO request) {
		
		// 이메일을 줄테니 코드를 발송해라
		emailAuthService.sendCode(request.getEmail());
		
		return ResponseData.ok(null, "인증번호가 발송됐습니다.");
	}
	
	/**
	 * 인증코드 검증
	 * - 인증코드 "검증" 성공 시 Redis에 저장된 코드를 삭제
	 */
	@PostMapping("/verifications")
	public ResponseEntity<ResponseData<EmailVerificationResult>> verifyCode(@Valid @RequestBody EmailVerifyRequestDTO request) {
		
		EmailVerificationResult result = emailAuthService.verify(request.getEmail(), request.getCode());
		
		return ResponseData.ok(result, "성공");
	}
	
	
}
