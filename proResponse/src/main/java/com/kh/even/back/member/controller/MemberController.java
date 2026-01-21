package com.kh.even.back.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.common.ResponseData;
import com.kh.even.back.member.model.dto.ChangePasswordDTO;
import com.kh.even.back.member.model.dto.MemberSignUpDTO;
import com.kh.even.back.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping
	public ResponseEntity<ResponseData<Void>> signUp(@Valid @ModelAttribute MemberSignUpDTO member, @RequestParam(name = "profileImg", required = false) MultipartFile file) {
		// log.info("회원가입 요청 진위여부 : {}", member);
		
		memberService.signUp(member, file);
		
		return ResponseData.created(null, "회원가입에 성공했습니다.");
	}
	
	@PutMapping("/me/password")
	public ResponseEntity<ResponseData<Void>> changePassword(@Valid @RequestBody ChangePasswordDTO password) {
		// log.info("비밀번호 정보 : {}", password);
		
		memberService.changePassword(password);
		
		return ResponseData.ok(null, "비밀번호 변경에 성공했습니다.");
	}
	
}
