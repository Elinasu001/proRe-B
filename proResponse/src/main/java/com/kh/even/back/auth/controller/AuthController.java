package com.kh.even.back.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.service.AuthService;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.exception.UsernameNotFoundException;
import com.kh.even.back.member.model.dto.LoginResponseDTO;
import com.kh.even.back.member.model.dto.MemberLoginDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<ResponseData<LoginResponseDTO>> login(@Valid @RequestBody MemberLoginDTO member ) {
		
		LoginResponseDTO loginResponse = authService.login(member);
		
		return ResponseData.ok(loginResponse, "로그인에 성공했습니다.");
	}

}
