package com.kh.even.back.cash.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.cash.model.service.CashService;
import com.kh.even.back.common.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/cash")
@RequiredArgsConstructor
public class CashController {

	private final CashService cashService;
	
	@GetMapping("/me")
	public ResponseEntity<ResponseData<Long>> getMyCash(@AuthenticationPrincipal CustomUserDetails user) {
		
		long balance = cashService.getMyCash(user);
		
		return ResponseData.ok(balance, "보유한 캐시 조회에 성공했습니다.");
	}
	
}
